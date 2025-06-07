package employees.controller;

import employees.model.boundary.EmployeeBoundary;
import employees.model.boundary.EmployeeLoginInputBoundary;
import employees.model.boundary.PaginationBoundary;
import employees.model.boundary.SearchCriterionBoundary;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import employees.service.EmployeeService;


@Controller
public class EmployeeRSocketController {

    private final EmployeeService employeeService;

    public EmployeeRSocketController(EmployeeService employeeService) {
        System.out.println(">>> EmployeeRSocketController initialized!");
        this.employeeService = employeeService;
    }

    @MessageMapping("RequestResponseCreateEmployee")
    public Mono<EmployeeBoundary> createEmployee(EmployeeBoundary input) {
        System.out.println(">>> createEmployee Visited!");
        String rawPassword = input.getPassword();
        return employeeService.create(input, rawPassword);
    }

    @MessageMapping("RequestResponseLoginEmployee")
    public Mono<EmployeeBoundary> loginEmployee(EmployeeLoginInputBoundary input) {
        return employeeService.login(input.getEmail(), input.getPassword());
    }

    @MessageMapping("RequestStreamGetEmployees")
    public Flux<EmployeeBoundary> getEmployees(PaginationBoundary input) {
        int page = input.getPage() >= 0 ? input.getPage() : 0;
        int size = input.getSize() > 0 ? input.getSize() : 10;
        return employeeService.getAllEmployees(page, size);
    }

    @MessageMapping("ChannelOfEmployees")
    public Flux<EmployeeBoundary> getEmployeesByChannel(Flux<PaginationBoundary> inputFlux) {
        return inputFlux
                .flatMap(pagination -> {
                    int page = pagination.getPage() >= 0 ? pagination.getPage() : 0;
                    int size = pagination.getSize() > 0 ? pagination.getSize() : 10;
                    return employeeService.getAllEmployees(page, size);
                });
    }

    @MessageMapping("FireAndFotgetCleanup")
    public Mono<Void> cleanup() {
        return employeeService.cleanup(); // Delegate to service
    }

    @MessageMapping("ChannelOfEmployeesByCriteria")
    public Flux<EmployeeBoundary> channelOfEmployeesByCriteria(Flux<SearchCriterionBoundary> criteriaFlux) {
        return criteriaFlux
                .flatMap(criterion -> employeeService.findByCriterion(criterion));
    }


}
