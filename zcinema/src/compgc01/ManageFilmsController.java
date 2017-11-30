package compgc01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.time.LocalDate;

import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyEvent;

////// THIS CLASS NEEDS TO BE REFINED... YET EVERYTHING WORKS GREAT!
// we make the MainController abstract and implements the methods that we need in all children being the controllers for every scene
// just to keep everything more organized....let me know what you think about it..
public class ManageFilmsController extends MainController {

	@FXML
	public void backToPrevScene(ActionEvent event) throws IOException {
		super.backToPrevScene(event);
	}

	@FXML
	public void uploadImageClick(ActionEvent event) throws IOException {
		super.uploadImageClick(event);
	}

	@FXML
	public void getDateTime(ActionEvent event) {
		super.getDateTime(event);
	}

	@FXML
	public void textUpdateMovie(KeyEvent event) {
		super.textUpdateMovie(event);
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void storeMovieInfo(ActionEvent event) {

		try {
			System.out.println(super.titleMovie.getText());
			System.out.println(super.aboutMovie.getText());
			System.out.println(super.dateFromMovieNew.getText());
			System.out.println(super.dateToMovieNew.getText());
			System.out.println(super.timeMovieNew.getText());

			validateMovieInput();
			// save the input in the json file for the movies!

			JSONObject movieTitle = new JSONObject();
			JSONObject movie = new JSONObject();

			movie.put("description", super.aboutMovie.getText());
			movie.put("from", super.dateFromMovieNew.getText());
			movie.put("to", super.dateToMovieNew.getText());
			movie.put("time", super.timeMovieNew.getText());

			movieTitle.put(super.titleMovie.getText(), movie);

			String path = ClassLoader.getSystemClassLoader().getResource(".").getPath();

			path = URLDecoder.decode(Main.getPath() + "res/" + "filmsJSON.txt", "UTF-8");

			// Adding a new object instead of overwriting the file content
			PrintWriter writer = new PrintWriter(new FileWriter(path, true));

			writer.print(movieTitle.toJSONString());
			writer.close();

			// Comfirmation Alert to inform the employee of the new added
			Alert alert = new Alert(AlertType.INFORMATION,
					"The Movie " + super.titleMovie.getText() + " has been added!", ButtonType.OK);
			alert.showAndWait();

			// Close alert on click and restore all fields to empty
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
				super.aboutMovie.setText("");
				super.titleMovie.setText("");
				// RESTORE THEM LIKE THIS yyyy-mm-dd hh:mm
				super.dateFromMovie.getValue();
				super.dateToMovie.getValue();
				super.timeMovie.getValue();

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

		if (super.titleMovie.getText().equals("") || 
			super.aboutMovie.getText().equals("") || 
			super.dateFromMovieNew.equals("yyyy-mm-dd") || 
			super.dateToMovieNew.getText().equals("yyyy-mm-dd")|| 
			super.timeMovieNew.getText().equals("hh:mm")) 
		{
			throw new InvalidMovieInputException("All fields must be completed!");

		}
	}
}

class InvalidMovieInputException extends Exception {
	InvalidMovieInputException(String s) {
		super(s);
	}
}
