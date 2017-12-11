package compgc01;

import java.io.*;
import java.security.GeneralSecurityException;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * The controller for the Edit Info Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 07.12.2017
 */
public class EditInfoController {

    @FXML
    Button backButton;
    @FXML
    Label windowTitleLabel, firstNameLabel, lastNameLabel, titleLabel, emailLabel;
    @FXML
    Label firstNameLabelNew, lastNameLabelNew, titleLabelNew, emailLabelNew;
    @FXML
    TextField updateFirstName, updateLastName, updateEmail, updatePassword;

    @FXML
    void initialize() throws IOException {

        personaliseScene();
    }

    @FXML
    public void editUpdateText(KeyEvent e) {

        TextField field = (TextField) e.getSource();
        if (field.getText().length() > 30)
            field.setEditable(false);

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

    @FXML
    public void saveClick(ActionEvent event) throws IOException, GeneralSecurityException {

        String userType = Main.getCurrentUser().getType();

        if (!updateFirstName.getText().trim().isEmpty()) {
            Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "firstName", updateFirstName.getText());
            Main.getCurrentUser().setFirstName(updateFirstName.getText());
        }
        if (!updateLastName.getText().trim().isEmpty()) {
            Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "lastName", updateLastName.getText());
            Main.getCurrentUser().setLastName(updateLastName.getText());
        }
        if (!updateEmail.getText().trim().isEmpty() && emailValidator()) {
            Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "email", updateEmail.getText());
            Main.getCurrentUser().setEmail(updateEmail.getText());
        } else {
        	Alert alert = new Alert(AlertType.WARNING, "The email must be of this format: \"example01@ucl.com\"", ButtonType.OK);
        	alert.showAndWait();
        	if(alert.getResult() == ButtonType.OK){
        		return;
        	}
        }
        if (!updatePassword.getText().trim().isEmpty()) {
            String encryptedPassword = Encryption.encrypt(updatePassword.getText());
            Main.modifyJSONFile(userType + "sJSON.txt", Main.getCurrentUser().getUsername(), "password", encryptedPassword);
            Main.getCurrentUser().setPassword(updatePassword.getText());
        }

        SceneCreator.launchScene("UserScene.fxml");
    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("UserScene.fxml");
    }

    protected void personaliseScene() throws IOException {

        // personalising page based on logged-in user
        firstNameLabel.setText(Main.getCurrentUser().getFirstName());
        lastNameLabel.setText(Main.getCurrentUser().getLastName());
        if (!Main.isEmployee()) {
            titleLabel.setText("Customer");
            windowTitleLabel.setText("Edit " + titleLabel.getText() + " Profile");
        }
        emailLabel.setText(Main.getCurrentUser().getEmail());
        titleLabelNew.setText(titleLabel.getText());
    }
    
    @FXML
    boolean emailValidator () {
    	return Pattern.matches("[A-Za-z0-9]+([/@])[A-Za-z0-9]+[/.][A-Za-z]+", (CharSequence) updateEmail.getText());
    }
}