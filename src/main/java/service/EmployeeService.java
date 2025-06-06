package service;

import model.Employee;
import model.boundary.EmployeeBoundary;
import model.boundary.EmployeeLoginInputBoundary;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import repository.EmployeeRepository;

import java.util.regex.Pattern;

import static org.bson.types.ObjectId.isValid;

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













    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    private boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@[\\w-]+\\.[a-z]{2,}$");
    }

    private boolean isValidPassword(String password) {
        // At least 3 characters, 1 digit, 1 uppercase letter
        return password != null &&
                password.length() >= 3 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*");
    }


}