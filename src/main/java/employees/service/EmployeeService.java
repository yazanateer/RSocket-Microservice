package employees.service;

import employees.model.Employee;
import employees.model.boundary.EmployeeBoundary;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import employees.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepo;

    public EmployeeService(EmployeeRepository employeeRepo) {
        this.employeeRepo = employeeRepo;
    }

    public Mono<EmployeeBoundary> create(EmployeeBoundary boundary, String rawPassword) {
        // Validate input
        if (boundary == null ||
                !isValidEmail(boundary.getEmail()) ||
                isBlank(boundary.getName()) ||
                !isValidPassword(rawPassword) ||
                boundary.getBirthdate() == null ||
                boundary.getRoles() == null || boundary.getRoles().isEmpty()) {
            return Mono.error(new IllegalArgumentException("Invalid employee input"));
        }
        // Create the employee object ahead of time
        Employee employee = new Employee(
                boundary.getEmail(),
                boundary.getName(),
                rawPassword, // Storing as-is for this exercise
                boundary.getBirthdate(),
                boundary.getRoles()
        );
        // Save only if not exists - FIXED VERSION
        return employeeRepo.findById(boundary.getEmail())
                .<EmployeeBoundary>flatMap(existing ->
                        Mono.error(new IllegalArgumentException("Employee already exists")))
                .switchIfEmpty(
                        employeeRepo.save(employee)
                                .map(saved -> new EmployeeBoundary(
                                        saved.getEmail(),
                                        saved.getName(),
                                        saved.getPassword(),
                                        saved.getBirthdate(),
                                        saved.getRoles()
                                ))
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
                        null, // No password
                        emp.getBirthdate(),
                        emp.getRoles()
                ));
    }

    public Mono<Void> deleteAll() {
        return employeeRepo.deleteAll(); // Reactive MongoDB deletes all documents
    }










    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@[\\w-]+(\\.[a-z]{2,})+$");
    }

    private boolean isValidPassword(String password) {
        // At least 3 characters, 1 digit, 1 uppercase letter
        return password != null &&
                password.length() >= 3 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*");
    }


}