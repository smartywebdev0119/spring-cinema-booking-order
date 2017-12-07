package compgc01;

import java.io.*;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * The controller for the Bookings Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 07.12.2017
 */
public class ManageBookingsController {

    boolean gridSeatsStartVisibility = true;
    String selectedSeat = "";
    ArrayList<Integer> redFixedSeats = new ArrayList<Integer>(5);

    @FXML
    GridPane gridSeats;
    @FXML
    Button backButton;
    @FXML
    DatePicker datePicker;
    @FXML
    ComboBox<String> filmDropDownList, timeDropDownList;

    @FXML
    void initialize() throws IOException {

        personaliseScene();
    }

    // getting the index of the seats
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

    }

    @FXML
    private void turnSeatRed(MouseEvent e) {

        ((Node) e.getSource()).setStyle("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");

        // Alert for booked seat
        /*
        if(((Node) e.getSource()).getStyle().length() == 55){
            Alert alert = new Alert(AlertType.WARNING, "The seat " + ((Node) e.getSource()).getId()  + " is booked already!", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }

        for (Node node : gridSeats.getChildren()) {
            node.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        }        

        if (((Node) e.getSource()).getStyle().length() == 55) {
            ((Node) e.getSource()).setStyle("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        } else {
            ((Node) e.getSource()).setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        }

        //		 BOOKED SEATS COLORS CAN'T TURN BLACK!!
        //		 for(Integer id : redFixedSeats){
        //		 gridSeats.getChildren().get(id).setStyle("-fx-fill:#c2a9a9; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        //		 }

         */
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

        ObservableList<String> filmScreeningTimes = FXCollections.observableArrayList("14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        timeDropDownList.setItems(filmScreeningTimes);

        /*
        timeDropDownList.getSelectionModel().selectedItemProperty().addListener( (options, oldValue, newValue) -> {
            System.out.println(newValue);
        });
         */
    }

    @FXML
    private void populateFilmDropDownList (ActionEvent event) throws IOException, ParseException {

        ObservableList<String> filmTitles = FXCollections.observableArrayList();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        for (Film film : Main.getFilmList()) {
            if (LocalDate.parse(film.getStartDate(), formatter).isBefore(datePicker.getValue()) && LocalDate.parse(film.getEndDate(), formatter).isAfter(datePicker.getValue()))
                filmTitles.add(film.getTitle());
        }

        filmDropDownList.setItems(filmTitles);
    }

    @FXML
    private void populateTimeDropDownList (ActionEvent event) throws IOException {

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

    @FXML
    private void populateSeats (ActionEvent event) throws IOException {

        Film selectedFilm = null;
        String selectedFilmTitle = filmDropDownList.getValue();
        for (Film film : Main.getFilmList()) {
            if (film.getTitle().equals(selectedFilmTitle)) {
                selectedFilm = film;
                break;
            }
        }

        // System.out.println(selectedFilm.getTitle());
        // System.out.println(selectedFilm.getTime());
        ArrayList<String> bookedSeats = new ArrayList<String>();
        for (BookingHistoryItem booking : Main.getBookingList()) {
            if (booking.getFilmTitle().equals(selectedFilm.getTitle()) && booking.getStatus().equals("booked")) {
                String seat = booking.getSeat();
                bookedSeats.add(seat);
            }
        }        
    }
}