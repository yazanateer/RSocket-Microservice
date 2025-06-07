package employees.repository;

import employees.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
    Flux<Employee> findAllBy(Pageable pageable);
    Flux<Employee> findAllByEmailEndingWith(String domain, Pageable pageable);
    Flux<Employee> findAllByRolesContaining(String role, Pageable pageable);


}
