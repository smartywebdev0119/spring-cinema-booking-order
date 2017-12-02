package compgc01;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * The controller for our GUI.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 26.11.2017
 */
public class MainController {

    @FXML
    TextField usernameBox;
    @FXML
    PasswordField passwordBox;
    @FXML
    Button logInButton, logOutButton, backButton;
    @FXML
    MaterialIconView a1;
    
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