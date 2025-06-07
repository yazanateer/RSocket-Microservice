package employees.util;

import employees.model.Employee;
import employees.model.boundary.EmployeeBoundary;

public class EmployeeConverter {
    public static EmployeeBoundary toBoundary(Employee employee) {
        return new EmployeeBoundary(
                employee.getEmail(),
                employee.getName(),
                "******",
                employee.getBirthdate(),
                employee.getRoles()
        );
    }
}
