package compgc01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
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
 * @since 09.12.2017
 */
public class ManageFilmsController {

	private File selectedImage;

	@FXML
	Button backButton;
	@FXML
	Text newFilmTitle, newFilmDescription, newFilmStartDate, newFilmEndDate, newFilmTime1, newFilmTime2, newFilmTime3;
	@FXML
	TextArea filmDescription;
	@FXML
	DatePicker filmStartDate, filmEndDate;
	@FXML
	TextField filmTitle;
	@FXML
	ComboBox<String> filmTime1, filmTime2, filmTime3;
	@FXML
	ImageView uploadedFilmPoster;

	@FXML
	void initialize() throws IOException {

		ObservableList<String> obsList = FXCollections.observableArrayList("13:00", "14:00", "15:00", "16:00", "17:00",
				"18:00", "19:00", "20:00", "21:00", "22:00", "23:00", "00:00", "01:00", "02:00", "03:00");
		filmTime1.setItems(obsList);
		filmTime2.setItems(obsList);
		filmTime3.setItems(obsList);
		File file = new File(
				URLDecoder.decode(Main.getPath() + "res/images/backgroundImages/DefaultFilmPoster.png", "UTF-8"));
		Image img = SwingFXUtils.toFXImage(ImageIO.read(file), null);
		uploadedFilmPoster.setImage(img);
	}

	@FXML
	public void launchViewFilms(ActionEvent event) throws IOException {

		SceneCreator.launchScene("ViewFilmsScene.fxml");
	}

	@FXML
	public void backToPrevScene(ActionEvent event) throws IOException {

		SceneCreator.launchScene("UserScene.fxml");
	}

	@FXML
	public void updateDateAndTime(ActionEvent e) {

		try {
			switch (((Node) e.getSource()).getId()) {
			case "filmStartDate":
				newFilmStartDate.setText(filmStartDate.getValue().toString());
				break;
			case "filmEndDate":
				newFilmEndDate.setText(filmEndDate.getValue().toString());
				break;
			case "filmTime1":
				newFilmTime1.setText(filmTime1.getValue().toString());
				break;
			case "filmTime2":
				newFilmTime2.setText(filmTime2.getValue().toString());
				break;
			case "filmTime3":
				newFilmTime3.setText(filmTime3.getValue().toString());
				break;
			}
		} catch (NullPointerException ex) {
			ex.getMessage();
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
			selectedImage = fc.showOpenDialog(null);
			// checking that input file is not null and handling the exception
			if (selectedImage == null)
				return;
			else {
				Image img = SwingFXUtils.toFXImage(ImageIO.read(selectedImage), null);
				uploadedFilmPoster.setImage(img);
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	@SuppressWarnings("unchecked")
	@FXML
	public void storeFilmInfo(ActionEvent event) throws ParseException {

		try {
			/*
			 * System.out.println(filmTitle.getText());
			 * System.out.println(filmDescription.getText());
			 * System.out.println(newFilmStartDate.getText());
			 * System.out.println(newFilmEndDate.getText());
			 * System.out.println(newFilmTime.getText());
			 */

			validateFilmInput();

			// creating JSON objects
			JSONObject films = Main.readJSONFile("filmsJSON.txt");
			JSONObject filmToAdd = new JSONObject();
			filmToAdd.put("description", filmDescription.getText());
			filmToAdd.put("startDate", newFilmStartDate.getText());
			filmToAdd.put("endDate", newFilmEndDate.getText());
			filmToAdd.put("time1", newFilmTime1.getText());
			filmToAdd.put("time2", newFilmTime2.getText());
			filmToAdd.put("time3", newFilmTime3.getText());
			films.put(filmTitle.getText(), filmToAdd);
			// System.out.println(films.toJSONString());

			// storing film in JSON file
			String path = URLDecoder.decode(Main.getPath() + "res/filmsJSON.txt", "UTF-8");
			// System.out.println(path);
			PrintWriter writer = new PrintWriter(new File(path));
			writer.print(films.toJSONString());
			writer.close();

			// storing film poster in film images folder
			String folderPath = URLDecoder.decode(Main.getPath() + "res/images/filmImages/", "UTF-8");
			File uploads = new File(folderPath);
			File file = new File(uploads, filmTitle.getText() + ".png");
			InputStream input = Files.newInputStream(selectedImage.toPath());
			Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
			// confirmation alert to inform the employee of the newly added film
			Alert alert = new Alert(AlertType.INFORMATION, "The film " + filmTitle.getText() + " has been added!",
					ButtonType.OK);
			alert.showAndWait();

			// reloading film list to include the recently added film, and
			// restoring all fields to empty
			// and closing alert on click
			if (alert.getResult() == ButtonType.OK) {
				Main.resetFilmList();
				Main.readJSONFile("filmsJSON.txt");
				filmDescription.setText("");
				filmTitle.setText("");
				filmStartDate.setPromptText("yyyy-mm-dd");
				filmEndDate.setPromptText("yyyy-mm-dd");
				filmTime1.setPromptText("hh:mm");
				filmTime2.setPromptText("hh:mm");
				filmTime3.setPromptText("hh:mm");
				alert.close();
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

	// custom exception to be thrown if at least one field is empty or if end
	// date is before start date
	@SuppressWarnings("unlikely-arg-type")
	void validateFilmInput() throws InvalidFilmInputException, ParseException {

		try {
			if (filmTitle.getText().equals("") || 
				filmDescription.getText().equals("") || 
				filmStartDate.getValue().equals("yyyy-mm-dd") || 
				filmEndDate.getValue().equals("yyyy-mm-dd") || 
				(filmTime1.getValue().equals("hh:mm") && 
				 filmTime2.getValue().equals("hh:mm") && 
				 filmTime3.getValue().equals("hh:mm"))) 
				throw new InvalidFilmInputException("Please complete all fields!");
			else if (selectedImage == null)
				throw new InvalidFilmInputException("Please add the film poster!");
			else if (filmStartDate.getValue().compareTo(LocalDate.now()) < 0)
				throw new InvalidFilmInputException("Start date cannot be before today!");
			else if (filmStartDate.getValue().compareTo(filmEndDate.getValue()) == 0)
				throw new InvalidFilmInputException("Screenings cannot start and end on the same day!");
			else if (filmStartDate.getValue().compareTo(filmEndDate.getValue()) > 0)
				throw new InvalidFilmInputException("End date cannot be before start date!");

			 else if ((filmTime1.getValue().equals(filmTime2.getValue()) ||
					   filmTime1.getValue().equals(filmTime3.getValue()) ||
					   filmTime2.getValue().equals(filmTime3.getValue())) &&
					 !(filmTime1.getValue().equals("hh:mm") &&
					   filmTime2.getValue().equals("hh:mm")))
			 throw new InvalidFilmInputException("You cannot choose the same screening time more than once!");

			// looping through the films to find date and time conflicts
			for (Film c : Main.getFilmList()) {

				// converting movie start and end dates to LocalDate for
				// comparison
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				LocalDate startDateFilms = LocalDate.parse(c.getStartDate(), formatter);
				LocalDate endDateFilms = LocalDate.parse(c.getEndDate(), formatter);

				// if the dates overlap
				if (!(filmStartDate.getValue().compareTo(endDateFilms) > 0
						|| filmEndDate.getValue().compareTo(startDateFilms) < 0)) {

					// System.out.println("startDate loop: " + startDateFilms);
					// System.out.println("endDate loop: " + endDateFilms);

					// ... and the time overlaps as well
					String[] times = c.getTimes();
					if (Arrays.asList(times).contains(filmTime1.getValue())
							|| Arrays.asList(times).contains(filmTime2.getValue())
							|| Arrays.asList(times).contains(filmTime3.getValue())) {
						throw new InvalidFilmInputException("The screening time(s) of your film: " + filmTitle.getText()
								+ " overlaps with the film: " + c.getTitle().toString());
					}
				}
			}
		} catch (NullPointerException e) {
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