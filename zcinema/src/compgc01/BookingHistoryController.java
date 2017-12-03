package compgc01;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;
import org.json.simple.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

// implementing the interface Initializable so that the method initialize gets read at load time
public class BookingHistoryController implements Initializable {

    @FXML
    private TableView<BookingHistoryItem> table;
    @FXML
    private TableColumn<BookingHistoryItem, String> idNumber, status, firstName, lastName, film, date, time, seat;
    @FXML
    BookingHistoryItem tableDate;
    @FXML
    private ArrayList<BookingHistoryItem> listRows = new ArrayList<BookingHistoryItem>();
    @FXML
    public ObservableList<BookingHistoryItem> populateTableList;
    @FXML
    private Button backButton, cancelBookingButton;
    @FXML
    private String selectedRowId;
    @FXML
    JSONObject user;

    // method that gets executed at load time
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        setTableColumns();
        Main.readJSONFile("bookingsJSON.txt");
        // storing elements in the ObservableList

        if (Main.isEmployee())
            populateTableList = FXCollections.observableArrayList(Main.getBookingList());
        else {
            ArrayList<BookingHistoryItem> currentCustomerBookings = new ArrayList<BookingHistoryItem>();
            for (BookingHistoryItem booking : Main.getBookingList()) {
                if (booking.getUsername().equals(Main.getCurrentUser().getUsername()))
                    currentCustomerBookings.add(booking);
            }
            populateTableList = FXCollections.observableArrayList(currentCustomerBookings);
        }

        // populating the table with the elements stored in ObservableList
        table.getItems().addAll(populateTableList);
        // the rows of the list are automatically sorted by key value
        // length! Sort them by date istead!
        changeColor();
    }

    private void setTableColumns() {

        // specifying how to populate the columns of the table
        status.setCellValueFactory(new PropertyValueFactory<BookingHistoryItem, String>("status"));
        firstName.setCellValueFactory(new PropertyValueFactory<BookingHistoryItem, String>("firstName"));
        lastName.setCellValueFactory(new PropertyValueFactory<BookingHistoryItem, String>("lastName"));
        film.setCellValueFactory(new PropertyValueFactory<BookingHistoryItem, String>("film"));
        date.setCellValueFactory(new PropertyValueFactory<BookingHistoryItem, String>("date"));
        time.setCellValueFactory(new PropertyValueFactory<BookingHistoryItem, String>("time"));
        seat.setCellValueFactory(new PropertyValueFactory<BookingHistoryItem, String>("seat"));
        idNumber.setCellValueFactory(new PropertyValueFactory<BookingHistoryItem, String>("idNumber"));
    }

    @FXML
    public boolean deleteBookingValidator() {

        // getting current date
        LocalDate currentDate = LocalDate.now(); // for reference

        // retrieving date from the table and saving it as String
        String selectedBookingDate = table.getSelectionModel().getSelectedItem().getDate();

        // creating array of Strings with year, month, and day indices
        // coverting String array into int array
        // creating Date object from int array
        int[] dateToInt = Arrays.stream(selectedBookingDate.split("-")).mapToInt(Integer::parseInt).toArray();
        LocalDate bookingDate = LocalDate.of(dateToInt[0], dateToInt[1], dateToInt[2]);

        // comparing current date with booking date
        int dateComparison = bookingDate.compareTo(currentDate);
        // System.out.println(dateComparison);
        return dateComparison > 0 ? true : false;
    }

    @FXML
    void changeColor() {

        // Inspired by https://stackoverflow.com/questions/30889732/javafx-tableview-change-row-color-based-on-column-value
        table.setRowFactory(row -> {
            return new TableRow<BookingHistoryItem>() {
                @Override
                public void updateItem(BookingHistoryItem item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) { // if the row is empty
                        setText(null);
                        setStyle("");
                    } else { // if the row is not empty
                        // we get here all the info of the booking of this row
                        BookingHistoryItem bookingInfo = getTableView().getItems().get(getIndex());
                        // style all rows whose status is set to "cancelled"
                        if (bookingInfo.getStatus().equals("cancelled")) {
                            // set the background colour of the row to gray
                            setStyle("-fx-background-color: #D3D3D3");
                        } else {
                            // if the table is reordered the colors update accordingly
                            setStyle(table.getStyle());
                        }
                    }
                }
            };
        });
    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        Main.resetBookingList();
        SceneCreator.launchScene("ManageBookingsScene.fxml", event);
    }

    @FXML
    void getRowId(MouseEvent e) {

        // storing the id number of the selected row in a String
        if (table.getSelectionModel().getSelectedItem() != null)
            this.selectedRowId = table.getSelectionModel().getSelectedItem().getIdNumber();
    }

    boolean isSelectedRowValid(String selectedRowId) {

        return selectedRowId != null ? true : false;
    }

    @FXML
    void deleteBooking(ActionEvent event) throws IOException {

        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you wish to cancel this booking?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        // if the user clicks OK
        if (alert.getResult() == ButtonType.YES) {
            if (isSelectedRowValid(selectedRowId)) {
                if (table.getSelectionModel().getSelectedItem().getStatus().equals("booked")) {
                    if(deleteBookingValidator()){ // comparing booking's date with the current date
                        Main.modifyJSONFile("bookingsJSON.txt", selectedRowId, "status", "cancelled");
                        Main.resetBookingList();
                        SceneCreator.launchScene("BookingHistory.fxml", event);
                        alert.close();
                    }
                    else {
                        Alert alert2 = new Alert(AlertType.ERROR, "You cannot cancel an old booking!", ButtonType.CLOSE);
                        alert2.showAndWait();
                        if (alert2.getResult() == ButtonType.CLOSE)
                            alert2.close();
                    }
                }
                else {
                    Alert alert2 = new Alert(AlertType.ERROR, "Booking already canceled!", ButtonType.CLOSE);
                    alert2.showAndWait();
                    if (alert2.getResult() == ButtonType.CLOSE)
                        alert2.close();
                }
            }
        }
        else if (alert.getResult() == ButtonType.NO) {
            alert.close();
        }
    }
}