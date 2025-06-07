package employees.util;

import employees.model.Birthdate;

import java.time.LocalDate;
import java.time.Period;

public class EmployeeUtils {

    public static int calculateAge(Birthdate birthdate) {
        try {
            int day = Integer.parseInt(birthdate.getDay());
            int month = Integer.parseInt(birthdate.getMonth());
            int year = Integer.parseInt(birthdate.getYear());

            LocalDate birth = LocalDate.of(year, month, day);
            LocalDate now = LocalDate.now();

            return Period.between(birth, now).getYears();
        } catch (Exception e) {
            throw new IllegalArgumentException("Invalid birthdate format");
        }
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    public static boolean isValidEmail(String email) {
        return email != null && email.matches("^[\\w-.]+@[\\w-]+(\\.[a-z]{2,})+$");
    }

    public static boolean isValidPassword(String password) {
        return password != null &&
                password.length() >= 3 &&
                password.matches(".*\\d.*") &&
                password.matches(".*[A-Z].*");
    }
}
