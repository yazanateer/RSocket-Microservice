package repository;

import model.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface EmployeeRepository extends ReactiveMongoRepository<Employee, String> {
    Mono<Employee> findByEmailAndPassword(String email, String password);

}
