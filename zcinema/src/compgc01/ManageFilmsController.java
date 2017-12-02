package compgc01;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.time.LocalDate;

import org.json.simple.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;

/**
 * The controller for the Films Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 02.12.2017
 */
public class ManageFilmsController extends UserSceneController {

    @FXML
    Text titleMovieNew, aboutMovieNew, dateFromMovieNew, dateToMovieNew, timeMovieNew;
    @FXML
    TextArea aboutMovie;
    @FXML
    DatePicker dateFromMovie, dateToMovie;
    @FXML
    TextField updateFirstName, updateLastName, updateEmail, updatePassword, titleMovie;
    @FXML
    ComboBox<String> timeMovie;

    @FXML
    void initialize() throws IOException{

        ObservableList<String> obsList = FXCollections.observableArrayList("14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        timeMovie.setItems(obsList);
    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("UserScene.fxml", event);
    }

    @FXML
    public void getDateTime(ActionEvent e) {

        switch (((Node) e.getSource()).getId()) {
        case "dateFromMovie":
            LocalDate ld1 = dateFromMovie.getValue();
            dateFromMovieNew.setText(ld1.toString());
            break;
        case "dateToMovie":
            LocalDate ld2 = dateToMovie.getValue();
            dateToMovieNew.setText(ld2.toString());
            break;
        case "timeMovie":
            timeMovieNew.setText(timeMovie.getValue().toString());
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
    public void uploadImageClick(ActionEvent event) throws IOException {

        /*
         * Inspired by:
         * https://stackoverflow.com/questions/17850191/why-am-i-getting-java-
         * lang-illegalstateexception-not-on-fx-application-thread
         * Avoid throwing IllegalStateException by running from a non-JavaFX thread.
         * Beautiful lambda expression version.
         */

        super.changeImage();

        // super.launchScene("ManageFilmsScene.fxml", event);
    }

    @SuppressWarnings("unchecked")
    @FXML
    public void storeMovieInfo(ActionEvent event) {

        try {
            System.out.println(titleMovie.getText());
            System.out.println(aboutMovie.getText());
            System.out.println(dateFromMovieNew.getText());
            System.out.println(dateToMovieNew.getText());
            System.out.println(timeMovieNew.getText());

            validateMovieInput();
            // save the input in the json file for the movies!

            JSONObject movieTitle = new JSONObject();
            JSONObject movie = new JSONObject();

            movie.put("description", aboutMovie.getText());
            movie.put("from", dateFromMovieNew.getText());
            movie.put("to", dateToMovieNew.getText());
            movie.put("time", timeMovieNew.getText());

            movieTitle.put(titleMovie.getText(), movie);

            String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();

            path = URLDecoder.decode(Main.getPath() + "res/" + "filmsJSON.txt", "UTF-8");

            // Adding a new object instead of overwriting the file content
            PrintWriter writer = new PrintWriter(new FileWriter(path, true));

            writer.print(movieTitle.toJSONString());
            writer.close();

            // Comfirmation Alert to inform the employee of the new added
            Alert alert = new Alert(AlertType.INFORMATION,
                    "The Movie " + titleMovie.getText() + " has been added!", ButtonType.OK);
            alert.showAndWait();

            // Close alert on click and restore all fields to empty
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
                aboutMovie.setText("");
                titleMovie.setText("");
                // RESTORE THEM LIKE THIS yyyy-mm-dd hh:mm
                dateFromMovie.setPromptText("yyyy-mm-dd");
                dateToMovie.setPromptText("yyyy-mm-dd");
                timeMovie.setPromptText("hh:mm");

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
        } catch (InvalidMovieInputException e) {
            Alert alert = new Alert(AlertType.WARNING, e.getMessage(), ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }
    }

    // creating exception if at least one field is empty
    void validateMovieInput() throws InvalidMovieInputException {

        if (titleMovie.getText().equals("") || 
                aboutMovie.getText().equals("") || 
                // super.dateFromMovie.getValue().equals("yyyy-mm-dd") || 
                // super.dateToMovie.getValue().equals("yyyy-mm-dd")|| 
                // super.timeMovie.getValue().equals("hh:mm") ||
                dateFromMovie.getValue().compareTo(dateFromMovie.getValue()) < 0)
        {
            throw new InvalidMovieInputException("All fields must be completed!");
        }
    }
}

class InvalidMovieInputException extends Exception {
    private static final long serialVersionUID = 1L;

    InvalidMovieInputException(String s) {
        super(s);
    }
}