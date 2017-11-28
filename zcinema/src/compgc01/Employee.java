package compgc01;

/**
 * A class represeting an employee
 * that inherits from user.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 07.11.2017
 */
public class Employee extends User {
    
    public Employee(String firstName, String lastName, String username, String password, String email) {
        super(firstName, lastName, username, password, email);  
    }
}