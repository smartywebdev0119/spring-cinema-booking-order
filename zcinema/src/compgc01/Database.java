package compgc01;

import java.sql.Connection ;
import java.sql.DriverManager ;
import java.sql.SQLException ;
import java.sql.Statement ;
import java.sql.ResultSet ;

import java.util.List ;
import java.util.ArrayList ;

public class Database {

    // in real life, use a connection pool....
    private Connection connection;

    public Database(String dbURL, String user, String password) throws SQLException, ClassNotFoundException {
        connection = DriverManager.getConnection(dbURL, user, password);
    }

    public void shutdown() throws SQLException {
        if (connection != null) {
            connection.close();
        }
    }

    public List<User> getUserList() throws SQLException {
        try (
            Statement stmnt = connection.createStatement();
            ResultSet rs = stmnt.executeQuery("select * from user");
        ){
            List<User> userList = new ArrayList<>();
            while (rs.next()) {
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                String username = rs.getString("username");
                String password = rs.getString("password");
                String email = rs.getString("email");
                User person = new User(firstName, lastName, username, password, email);
                userList.add(person);
            }
            return userList ;
        } 
    }

    // other methods, eg. addPerson(...) etc
}