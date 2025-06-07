package employees.repository;

import employees.model.Employee;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
    Mono<Employee> findByEmailAndPassword(String email, String password);
    Flux<Employee> findAllBy(Pageable pageable);


}
