package controller;

import model.boundary.EmployeeBoundary;
import org.springframework.messaging.handler.annotation.MessageMapping;
import reactor.core.publisher.Mono;
import service.EmployeeService;

public class EmployeeRSocketController {

    private final EmployeeService employeeService;

    public EmployeeRSocketController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @MessageMapping("RequestResponseCreateEmployee")
    public Mono<EmployeeBoundary> createEmployee(EmployeeBoundary input) {
        String rawPassword = input.getPassword();
        return employeeService.create(input, rawPassword);
    }
}
