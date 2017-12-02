package compgc01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.json.simple.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * The controller for the Films Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 02.12.2017
 */
public class ManageFilmsController {

    @FXML
    Button backButton;
    @FXML
    Text newFilmTitle, newFilmDescription, newFilmStartDate, newFilmEndDate, newFilmTime;
    @FXML
    TextArea filmDescription;
    @FXML
    DatePicker filmStartDate, filmEndDate;
    @FXML
    TextField filmTitle;
    @FXML
    ComboBox<String> filmTime;
    @FXML
    ImageView uploadedFilmPoster;

    @FXML
    void initialize() throws IOException {

        ObservableList<String> obsList = FXCollections.observableArrayList("14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        filmTime.setItems(obsList);
    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("UserScene.fxml", event);
    }

    @FXML
    public void updateDateAndTime(ActionEvent e) {

        switch (((Node) e.getSource()).getId()) {
        case "filmStartDate":
            newFilmStartDate.setText(filmStartDate.getValue().toString());
            break;
        case "filmEndDate":
            newFilmEndDate.setText(filmEndDate.getValue().toString());
            break;
        case "filmTime":
            newFilmTime.setText(filmTime.getValue().toString());
            break;
        }
    }

    @FXML
    public void updateFilmText(KeyEvent e) {

        switch (((Node) e.getSource()).getId()) {
        case "filmTitle":
            if (filmTitle.getText().length() > 20) {
                filmTitle.setEditable(false);
            }
            break;
        case "filmDescription":
            if (filmDescription.getText().length() > 220) {
                filmDescription.setEditable(false);
            }
            break;
        }

        if (e.getCode().equals(KeyCode.BACK_SPACE)) {
            filmTitle.setEditable(true);
            filmDescription.setEditable(true);
        }

        switch (((Node) e.getSource()).getId()) {
        case "filmTitle":
            newFilmTitle.setText(filmTitle.getText());
            break;
        case "filmDescription":
            newFilmDescription.setText(filmDescription.getText());
            break;
        }
    }

    @FXML
    public void uploadImageClick(ActionEvent event) throws IOException {

        try {
            FileChooser fc = new FileChooser();
            File selectedFile = fc.showOpenDialog(null);
            // checking that input file is not null and handling the exception
            if (selectedFile == null)
                return;
            else {
                Image img = SwingFXUtils.toFXImage(ImageIO.read(selectedFile), null);

                uploadedFilmPoster.setImage(img);

                String path = Main.getPath();
                String folderPath = URLDecoder.decode(path + "res/images/filmImages/", "UTF-8");
                File uploads = new File(folderPath);
                // NEED TO MAKE SURE THIS STORES THE CORRECT TITLE IN THE END
                File file = new File(uploads, filmTitle.getText() + ".png");
                InputStream input = Files.newInputStream(selectedFile.toPath());
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void storeFilmInfo(ActionEvent event) {

        try {
            System.out.println(filmTitle.getText());
            System.out.println(filmDescription.getText());
            System.out.println(newFilmStartDate.getText());
            System.out.println(newFilmEndDate.getText());
            System.out.println(newFilmTime.getText());

            validateFilmInput();
            // save the input in the json file for the movies!

            JSONObject movieTitle = new JSONObject();
            JSONObject movie = new JSONObject();

            movie.put("description", filmDescription.getText());
            movie.put("from", newFilmStartDate.getText());
            movie.put("to", newFilmEndDate.getText());
            movie.put("time", newFilmTime.getText());

            movieTitle.put(filmTitle.getText(), movie);

            String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();

            path = URLDecoder.decode(Main.getPath() + "res/" + "filmsJSON.txt", "UTF-8");

            // Appending a new JSON object to the end of the text file instead of overwriting all the content
            // DOES NOT PRODUCE VALID JSON TEXT SO NEED TO CHANGE THIS AND MAKE IT THE SAME AS USERS
            PrintWriter writer = new PrintWriter(new FileWriter(path, true));

            writer.print(movieTitle.toJSONString());
            writer.close();

            // Comfirmation Alert to inform the employee of the new added
            Alert alert = new Alert(AlertType.INFORMATION,
                    "The Film " + filmTitle.getText() + " has been added!", ButtonType.OK);
            alert.showAndWait();

            // close alert on click and restore all fields to empty
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
                filmDescription.setText("");
                filmTitle.setText("");
                filmStartDate.setPromptText("yyyy-mm-dd");
                filmEndDate.setPromptText("yyyy-mm-dd");
                filmTime.setPromptText("hh:mm");
            }
        } catch (FileNotFoundException e) {
            Alert alert = new Alert(AlertType.WARNING, "File Not Found!", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        } catch (IOException e) {
            Alert alert = new Alert(AlertType.WARNING, "Error: " + e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        } catch (InvalidFilmInputException e) {
            Alert alert = new Alert(AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
    }

    // creating exception if at least one field is empty
    @SuppressWarnings("unlikely-arg-type")
    void validateFilmInput() throws InvalidFilmInputException {

        try {
            if (filmTitle.getText().equals("") || 
                    filmDescription.getText().equals("") || 
                    filmStartDate.getValue().equals("yyyy-mm-dd") || 
                    filmEndDate.getValue().equals("yyyy-mm-dd") || 
                    filmTime.getValue().equals("hh:mm"))
                throw new InvalidFilmInputException("Please complete all fields!");

            if (filmStartDate.getValue().compareTo(filmEndDate.getValue()) > 0)
                throw new InvalidFilmInputException("End date cannot be before start date!");
        }
        catch (NullPointerException e) {
            throw new InvalidFilmInputException("Please complete all fields!");
        }
    }
}

class InvalidFilmInputException extends Exception {

    private static final long serialVersionUID = 1L;

    InvalidFilmInputException(String s) {
        super(s);
    }
}