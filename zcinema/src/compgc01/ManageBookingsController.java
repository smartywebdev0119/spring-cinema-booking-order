package compgc01;

import java.io.*;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

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
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * The controller for the Bookings Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 08.12.2017
 */
public class ManageBookingsController implements Initializable {

    int bookedSeatsCount;
    ArrayList<String> selectedSeats;

    @FXML
    GridPane gridSeats;
    @FXML
    Button backButton;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<String> filmDropDownList, timeDropDownList;
    @FXML
    Label bookedSeatsLabel, availableSeatsLabel, totalSeatsLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            personaliseScene();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // setting the total number of seats to a value of 18
        totalSeatsLabel.setText("Total seats: 18");
        bookedSeatsCount = 0;
        selectedSeats = new ArrayList<String>();

        // getting the most recent version of the bookings file
        Main.resetBookingList();
        Main.readJSONFile("bookingsJSON.txt");

        if (!Main.isEmployee()) {
            bookedSeatsLabel.setVisible(false);
            availableSeatsLabel.setVisible(false);
            totalSeatsLabel.setVisible(false);
        }
        
        // action that is fired whenever the time is changed
        timeDropDownList.setOnAction((event) -> {

            // resetting the number of booked seats for every date, film, and time
            bookedSeatsCount = 0;

            // resetting all seats to black every time the user selects a new screening time
            for (int i = 0; i < 18; i++) {
                gridSeats.getChildren().get(i)
                .setStyle("-fx-fill:black; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
            }

            // spotting the booked seats for a specific film, date, and time and turning their colour to grey
            for (BookingHistoryItem booking : Main.getBookingList()) {
                // making sure we do not include the cancelled bookings
                if (booking.getStatus().equals("booked")) {
                    // checking if the booking's film, date, and time match the user's choice
                    if (booking.getDate().equals(datePicker.getValue().toString())
                            && booking.getFilm().equals(filmDropDownList.getValue())
                            && booking.getTime().equals(timeDropDownList.getValue())) {
                        // turning the booked seat grey
                        for (int i = 0; i < 18; i++) {
                            if (gridSeats.getChildren().get(i).getId().equals(booking.getSeat())) {
                                gridSeats.getChildren().get(i).setStyle(
                                        "-fx-fill:#c9b3b3; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                                // incrementing the count of the booked seats
                                bookedSeatsCount++;
                            }
                        }
                    }
                }
            }

            // setting the number of the booked seats and empty seats every time there is an action
            // and the specific screening (film, date, and time) changes
            bookedSeatsLabel.setText("Booked seats: " + bookedSeatsCount);
            availableSeatsLabel.setText("Available seats: " + (18 - bookedSeatsCount));
        });
    }

    @FXML
    private void selectSeat(MouseEvent e) {

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
            // turning seat back to black if it is red - unselecting it
            if (((Node) e.getSource()).getStyle()
                    .equals("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;")) {
                ((Node) e.getSource())
                .setStyle("-fx-fill:black; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                selectedSeats.remove(((Node) e.getSource()).getId());
            }
            // turning seat red if it is black - selecting it
            else {
                ((Node) e.getSource())
                .setStyle("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
                selectedSeats.add(((Node) e.getSource()).getId());
            }
        }
    }

    @FXML
    private void bookSeat(MouseEvent e) {

        if (selectedSeats.size() == 0)
            return;

        // getting the latest booking id and incrementing by one
        int newBookingId = Main.getBookingList().size() + 1;
        // System.out.println(newBookingId);

        for (int i = newBookingId; i < (newBookingId + selectedSeats.size()); i++) {
            Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "username", Main.getCurrentUser().getUsername());
            Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "date", datePicker.getValue().toString());
            Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "seat", selectedSeats.get(i - newBookingId));
            Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "time", timeDropDownList.getValue());
            Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "film", filmDropDownList.getValue());
            Main.modifyJSONFile("bookingsJSON.txt", Integer.toString(i), "status", "booked");
        }

        selectedSeats.clear();
        Main.resetBookingList();
        Main.readJSONFile("bookingsJSON.txt");
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
        String selectedFilmTitle = "";

        try {
            selectedFilmTitle = filmDropDownList.getValue();
            for (Film film : Main.getFilmList()) {
                if (film.getTitle().equals(selectedFilmTitle)) {
                    selectedFilm = film;
                    break;
                }
            }

            timeDropDownList.setValue(selectedFilm.getTime());
            timeDropDownList.setDisable(true);
        }
        catch (NullPointerException ex) {
            return;
        }
    }
}