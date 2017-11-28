package compgc01;

/**
 * A class representing a customer
 * that inherits from user.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 07.11.2017
 */
public class Customer extends User {

    private double accountBalance;
    
    public Customer(String firstName, String lastName, String username, String password, String email, double startingBalance) {
        super(firstName, lastName, username, password, email);
        this.accountBalance = startingBalance;
    }
    
    public double getAccountBalance() {
        return accountBalance;
    }
    
    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
    
    public void addToAccountBalance(double addAmount) {
        if (addAmount > 0)
            this.accountBalance += addAmount;
        else System.out.println("Invalid ammount. The amount to add has to be positive.");
    }
}