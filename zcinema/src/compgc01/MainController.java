package compgc01;

import java.io.*;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

/**
 * The controller for the Login Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 02.12.2017
 */
public class MainController {

    @FXML
    TextField usernameBox;
    @FXML
    PasswordField passwordBox;
    @FXML
    Button logInButton, logOutButton;

    @FXML
    public void exitButton(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    public void loginClick(ActionEvent event) throws IOException {

        ArrayList<User> users = new ArrayList<User>();
        users.addAll(Main.getEmployeeList());
        users.addAll(Main.getCustomerList());

        for (User u : users) {
            if (usernameBox.getText().equals(u.getUsername()) && passwordBox.getText().equals(u.getPassword())) {
                Main.setCurrentUser(u);
                if (u.getType().equals("employee")) {
                    Main.employeeMode = true;
                }

                // loading new scene
                SceneCreator.launchScene("UserScene.fxml", event);
            }
        }
    }
}