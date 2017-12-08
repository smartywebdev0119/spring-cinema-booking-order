package compgc01;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

import org.json.simple.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * The controller for the Bookings Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 08.12.2017
 */
public class ManageBookingsController implements Initializable {

	String selectedSeat = "";

	@FXML
	GridPane gridSeats;
	@FXML
	Button backButton;
	@FXML
	DatePicker datePicker;
	@FXML
	ComboBox<String> filmDropDownList, timeDropDownList;
	@FXML
	TextField bookedSeats, emptySeats, totalSeats;
	@FXML
	int bookedSeatsInt;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		try {
			personaliseScene();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// seat values  not editable plus total seats fixed at 18.
		bookedSeats.setEditable(false);
		emptySeats.setEditable(false);
		totalSeats.setEditable(false);
		totalSeats.setText("18");

		Main.resetBookingList();
		Main.readJSONFile("bookingsJSON.txt");
		
		// Action that is fired whenever the time is changed. 
		timeDropDownList.setOnAction((event) -> {

			// resetting the number of booked seats for every date, movie, and time
			bookedSeatsInt = 0;

			// setting everything to black every time the user changes movie
			for (int i = 0; i < 18; i++) {
				gridSeats.getChildren().get(i)
						.setStyle("-fx-fill:black; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
			}

			// this for loop spots the booked seats for a specific film, date, and time and turn them into grey
			for (BookingHistoryItem c : Main.getBookingList()) {
				// making sure we do not include the cancelled bookings
				if (c.getStatus().equals("booked")) {
					// checking the bookings file film, date, and time with the user's choice
					if (c.getDate().equals(datePicker.getValue().toString())
							&& c.getFilm().equals(filmDropDownList.getValue().toString())
							&& c.getTime().equals(timeDropDownList.getValue().toString())) {
						//turning the booked seats gray
						for (int i = 0; i < 18; i++) {
							if (gridSeats.getChildren().get(i).getId().equals(c.getSeat())) {
								gridSeats.getChildren().get(i).setStyle(
										"-fx-fill:#c9b3b3; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
								// getting the number of the booked seats
								bookedSeatsInt++;
							}
						}

					}
				}
			}
			// setting the number of the booked seats and empty seats every time there is an action on the date
			bookedSeats.setText(Integer.toString(bookedSeatsInt));
			emptySeats.setText(Integer.toString(18 - bookedSeatsInt));
		});

	}

	// getting the index of a seat
	@FXML
	private void getSeatIndex(MouseEvent e) {

		try {
			Node target = (Node) e.getTarget();
			int colIndex = GridPane.getColumnIndex(target);
			int rowIndex = GridPane.getRowIndex(target);
			// System.out.print(rowIndex + ",");
			// System.out.print(colIndex + "\n");
			selectedSeat = "" + rowIndex + colIndex;
		} catch (NullPointerException ex) {
			System.out.println("Please click on a seat!");
		}
	}

	@FXML
	private void bookSeat(MouseEvent e) {
		// firing a pop up message if user clicks on already booked seat
		if (((Node) e.getSource()).getStyle()
				.equals("-fx-fill:#c9b3b3; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;")) {
			Alert alert = new Alert(AlertType.WARNING,
					"The seat " + ((Node) e.getSource()).getId() + " is booked already!", ButtonType.OK);
			alert.showAndWait();
			if (alert.getResult() == ButtonType.OK) {
				alert.close();
			}
		} else {
			// turning seat black if it is red
			if (((Node) e.getSource()).getStyle()
					.equals("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;")) {
				((Node) e.getSource())
						.setStyle("-fx-fill:black; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
				// turning seat red if it is black
			} else {
				((Node) e.getSource())
						.setStyle("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
			}
		}
	}

	@FXML
	private void showBookingHistoryOnClick(ActionEvent event) throws IOException {

		SceneCreator.launchScene("BookingHistoryScene.fxml");
	}

	@FXML
	private void backToPrevScene(ActionEvent event) throws IOException {

		SceneCreator.launchScene("UserScene.fxml");
	}

	private void personaliseScene() throws IOException {

		ObservableList<String> filmScreeningTimes = FXCollections.observableArrayList("14:00", "15:00", "16:00",
				"17:00", "18:00", "19:00", "20:00", "21:00", "22:00", "23:00");
		timeDropDownList.setItems(filmScreeningTimes);

		/*
		 * timeDropDownList.getSelectionModel().selectedItemProperty().
		 * addListener( (options, oldValue, newValue) -> {
		 * System.out.println(newValue); });
		 */
	}

	@FXML
	private void populateFilmDropDownList(ActionEvent event) throws IOException, ParseException {

		ObservableList<String> filmTitles = FXCollections.observableArrayList();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		for (Film film : Main.getFilmList()) {
			if (LocalDate.parse(film.getStartDate(), formatter).isBefore(datePicker.getValue())
					&& LocalDate.parse(film.getEndDate(), formatter).isAfter(datePicker.getValue()))
				filmTitles.add(film.getTitle());
		}

		filmDropDownList.setItems(filmTitles);
	}

	@FXML
	private void populateTimeDropDownList(ActionEvent event) throws IOException {

		Film selectedFilm = null;
		String selectedFilmTitle = filmDropDownList.getValue();
		for (Film film : Main.getFilmList()) {
			if (film.getTitle().equals(selectedFilmTitle)) {
				selectedFilm = film;
				break;
			}
		}

		timeDropDownList.setValue(selectedFilm.getTime());
		// timeDropDownList.setEditable(false);
		timeDropDownList.setDisable(true);
	}

	// I THINK WE NO LONGER NEED THIS

	// @FXML
	// private void populateSeats(ActionEvent event) throws IOException {
	//
	// Film selectedFilm = null;
	// String selectedFilmTitle = filmDropDownList.getValue();
	// for (Film film : Main.getFilmList()) {
	// if (film.getTitle().equals(selectedFilmTitle)) {
	// selectedFilm = film;
	// break;
	// }
	// }
	//
	// // System.out.println(selectedFilm.getTitle());
	// // System.out.println(selectedFilm.getTime());
	// ArrayList<String> bookedSeats = new ArrayList<String>();
	// for (BookingHistoryItem booking : Main.getBookingList()) {
	// if (booking.getFilm().equals(selectedFilm.getTitle()) &&
	// booking.getStatus().equals("booked")) {
	// String seat = booking.getSeat();
	// bookedSeats.add(seat);
	// }
	// }
	// }


}