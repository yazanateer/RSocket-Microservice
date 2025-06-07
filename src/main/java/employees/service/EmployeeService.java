package employees.service;

import employees.model.Birthdate;
import employees.model.Employee;
import employees.util.EmployeeConverter;
import employees.model.boundary.EmployeeBoundary;
import employees.model.boundary.SearchCriterionBoundary;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import employees.repository.EmployeeRepository;
import static employees.util.EmployeeUtils.*;



@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;

    public EmployeeService(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Mono<EmployeeBoundary> create(EmployeeBoundary boundary, String rawPassword) {
        if (boundary == null ||
                !isValidEmail(boundary.getEmail()) ||
                isBlank(boundary.getName()) ||
                !isValidPassword(rawPassword) ||
                boundary.getBirthdate() == null ||
                boundary.getRoles() == null || boundary.getRoles().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Invalid employee input"));
        }
        Employee employee = new Employee(
                boundary.getEmail(),
                boundary.getName(),
                rawPassword,
                boundary.getBirthdate(),
                boundary.getRoles()
        );
        // Save only if not exists
        return employeeRepo.findById(boundary.getEmail())
                .<EmployeeBoundary>flatMap(existing ->
                        Mono.error(new IllegalArgumentException("Employee already exists")))
                .switchIfEmpty(
                        employeeRepo.save(employee)
                                .map(EmployeeConverter::toBoundary)

                );
    }

    public Mono<EmployeeBoundary> login(String email, String password) {
        return employeeRepo.findById(email)
                .filter(emp -> emp.getPassword().equals(password)) // השוואה פשוטה
                .map(emp -> new EmployeeBoundary(
                        emp.getEmail(),
                        emp.getName(),
                        null, // לא מחזירים סיסמה
                        emp.getBirthdate(),
                        emp.getRoles()
                ));
    }

    public Flux<EmployeeBoundary> getAllEmployees(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return employeeRepo.findAllBy(pageable)
                .map(emp -> new EmployeeBoundary(
                        emp.getEmail(),
                        emp.getName(),
                        null,
                        emp.getBirthdate(),
                        emp.getRoles()
                ));
    }

    public Mono<Void> cleanup() {
        return employeeRepo.deleteAll();
    }

    public Flux<EmployeeBoundary> findByCriterion(SearchCriterionBoundary criterion) {
        int page = criterion.getPage();
        int size = criterion.getSize();
        Pageable pageable = PageRequest.of(page, size);

        String searchType = criterion.getSearchCriteria();
        String value = criterion.getValue();

        return switch (searchType) {
            case "byEmailDomain" -> this.employeeRepo
                    .findAllByEmailEndingWith("@" + value, pageable)
                    .map(EmployeeConverter::toBoundary);

            case "byRole" -> this.employeeRepo
                    .findAllByRolesContaining(value, pageable)
                    .map(EmployeeConverter::toBoundary);

            case "byAge" -> {
                try {
                    int targetAge = Integer.parseInt(value);
                    yield this.employeeRepo
                            .findAllBy(pageable)
                            .filter(employee -> {
                                try {
                                    int employeeAge = calculateAge(employee.getBirthdate());
                                    return employeeAge == targetAge;
                                } catch (Exception e) {
                                    return false; // Skip invalid birthdates
                                }
                            })
                            .map(EmployeeConverter::toBoundary);

                } catch (NumberFormatException e) {
                    yield Flux.empty(); // If age is not a number
                }
            }
            default -> Flux.empty(); // Unknown criteria
        };
    }





}