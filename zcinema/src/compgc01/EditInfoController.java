package compgc01;

import java.io.*;
import java.util.ArrayList;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/**
 * The controller for our GUI.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 26.11.2017
 */
public class EditInfoController extends UserSceneController {

    public boolean gridSeatsStartVisibility = true;
    static ArrayList<Booking> bookings;
    static ArrayList<Integer> redFixedSeats = new ArrayList<Integer>(5);

    @FXML
    TextField usernameBox;
    @FXML
    PasswordField passwordBox;
    @FXML
    Button logInButton, logOutButton, manageMoviesButton, manageBookingsButton, backButton, sendEmailButton;
    @FXML
    Label windowTitleLabel, firstNameLabel, lastNameLabel, titleLabel, emailLabel;
    @FXML
    Label firstNameLabelNew, lastNameLabelNew, titleLabelNew, emailLabelNew;
    @FXML
    TextField updateFirstName, updateLastName, updateEmail, updatePassword, titleMovie;
    @FXML
    Text titleMovieNew, aboutMovieNew, dateFromMovieNew, dateToMovieNew, timeMovieNew;
    @FXML
    TextArea aboutMovie;
    @FXML
    DatePicker dateFromMovie, dateToMovie;
    @FXML
    ImageView uploadedUserIcon, uploadedMoviePicture;
    @FXML
    GridPane gridSeats;
    @FXML
    ComboBox<String> timeMovie;
    @FXML
    MaterialIconView a1;
    
    @FXML
    void initialize() throws IOException{
        personaliseScene();
    }
    
    @FXML
    public void exitButton(MouseEvent event) {
        System.exit(0);
    }
    
    @FXML
    public void logOutClick(ActionEvent event) throws IOException {
        
        // resetting current user for security purposes
        Main.setCurrentUser(new User("default", "default", "default", "default", "default"));
        Main.employeeMode = false;
        bookings = new ArrayList<Booking>();

        // loading login stage
        SceneCreator.launchScene("LoginScene.fxml", event);
    }

    // Edit Info Scene
    @FXML
    public void editUpdateText(KeyEvent e) {

        TextField field = (TextField) e.getSource();
        if (field.getText().length() > 30)
            field.setEditable(false);

        // Pattern.matches("[A-Za-z]*", (CharSequence) updateName);

        if (e.getCode().equals(KeyCode.BACK_SPACE))
            field.setEditable(true);

        switch (((Node) e.getSource()).getId()) {
        case "updateFirstName":
            firstNameLabelNew.setText(updateFirstName.getText());
            break;
        case "updateLastName":
            lastNameLabelNew.setText(updateLastName.getText());
            break;
        case "updateEmail":
            emailLabelNew.setText(updateEmail.getText());
            break;
        }
    }

    // Add Movie Scene
    @FXML
    public void textUpdateMovie(KeyEvent e) {

        switch (((Node) e.getSource()).getId()) {

        case "titleMovie":
            if (titleMovie.getText().length() > 20) {
                titleMovie.setEditable(false);
            }
            break;
        case "aboutMovie":
            if (aboutMovie.getText().length() > 220) {
                aboutMovie.setEditable(false);
            }
            break;
        }

        if (e.getCode().equals(KeyCode.BACK_SPACE)) {
            titleMovie.setEditable(true);
            aboutMovie.setEditable(true);

        }

        switch (((Node) e.getSource()).getId()) {
        case "titleMovie":
            titleMovieNew.setText(titleMovie.getText());
            break;
        case "aboutMovie":
            aboutMovieNew.setText(aboutMovie.getText());
            break;
        }
    }

    @FXML
    public void saveClick(ActionEvent event) throws IOException {

        String userType = Main.getCurrentUser().getType();

        if (!updateFirstName.getText().trim().isEmpty()) {
            Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "firstName", updateFirstName.getText());
            Main.getCurrentUser().setFirstName(updateFirstName.getText());
        }
        if (!updateLastName.getText().trim().isEmpty()) {
            Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "lastName", updateLastName.getText());
            Main.getCurrentUser().setLastName(updateLastName.getText());
        }
        if (!updateEmail.getText().trim().isEmpty()) {
            Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "email", updateEmail.getText());
            Main.getCurrentUser().setEmail(updateEmail.getText());
        }

        SceneCreator.launchScene("UserScene.fxml", event);
        personaliseScene();
    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("UserScene.fxml", event);
    }

    protected void personaliseScene() throws IOException {

        // personalising page based on logged-in user

       firstNameLabel.setText(Main.getCurrentUser().getFirstName());
        lastNameLabel.setText(Main.getCurrentUser().getLastName());

        if (!Main.employeeMode) {
            titleLabel.setText("Customer");
            windowTitleLabel.setText("Edit " + titleLabel.getText() + " Profile");
        }
            emailLabel.setText(Main.getCurrentUser().getEmail());
            titleLabelNew.setText(titleLabel.getText());
    }
}