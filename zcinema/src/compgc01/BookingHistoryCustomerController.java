package compgc01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

// implementing the interface Initializable so that the method initialize gets read at load time
public class BookingHistoryCustomerController extends UserSceneController implements Initializable {

	@FXML
	private TableView<BookingHistoryCustomerItem> table;
	@FXML
	private TableColumn<BookingHistoryCustomerItem, String> idNumber, status, firstName, lastName, film, date, time, seat;
	@FXML
	private BookingHistoryCustomerItem tableDate;
	@FXML
	private ArrayList<BookingHistoryCustomerItem> listRows = new ArrayList<BookingHistoryCustomerItem>();
	@FXML
	public ObservableList<BookingHistoryCustomerItem> populateTable;
	@FXML
	private Button backButton, cancelBookingButton;
	@FXML
	private String selectedRowId;
	@FXML
	JSONObject user;
	@FXML
	String path;

	// mothod that gets executed at load time
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		setTableColumns();
		setBookingHistoryList("bookingsJSON.txt");
		changeColor();
	}

	private void setTableColumns() {
		// specifying how to populate the columns of the table
		status.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("status"));
		firstName.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("lastName"));
		film.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("film"));
		date.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("time"));
		seat.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("seat"));
		idNumber.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("idNumber"));
	}

	private JSONObject setBookingHistoryList(String bookingsFile) {

		JSONParser parser = new JSONParser();

		try {
			// getting the path of the booking history .json file
			path = URLDecoder.decode(Main.getPath() + "res/" + bookingsFile, "UTF-8");
			// System.out.println(path);

			// creating JSONObject containing all contents of the JSON file
			user = (JSONObject) parser.parse(new FileReader(path));

			// looping through .json file keys
			// creating a variable to temporarily store all values of the keys
			// pushing the values in the ArrayList populated by BookingHistory
			for (Object s : user.keySet()) {
				// System.out.println(s);
				JSONObject bookingRow = (JSONObject) user.get(s);

				listRows.add(new BookingHistoryCustomerItem((String) bookingRow.get("status"),
						(String) bookingRow.get("firstName"), (String) bookingRow.get("lastName"),
						(String) bookingRow.get("film"), (String) bookingRow.get("date"),
						(String) bookingRow.get("time"), (String) bookingRow.get("seat"), (String) (s)));
			}

			// storing elements in the ObservableLisr
			// populating the table with the elements stored in ObservableList
			// the rows of the list are automatically sorted by key value
			// length! Sort them by date istead!
			populateTable = FXCollections.observableArrayList(getBookingHistoryList());
			table.getItems().addAll(populateTable);

			return user;

			// reading from array
			// JSONArray arr = (JSONArray) jsonObject.get("array");
			// for (Object s : arr) {
			// list.add((String) s);
			// }
			// for(String s : list){
			// System.out.println(s);
			// }
			///////////////////////////////
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new JSONObject();
	}

	// returning objects of ArrayList to be called in ObservableList
	@FXML
	public ArrayList<BookingHistoryCustomerItem> getBookingHistoryList() {
		return listRows;
	}

	@FXML
	public boolean deleteBookingValidator() {

		// getting current date
		LocalDate currentDate = LocalDate.now();// For reference

		// retriving date from the table
		tableDate = table.getSelectionModel().getSelectedItem();

		// formatting date -> 2017-11-28 -> String
		String rowDate = tableDate.getDate();

		// creating array of Strings with year, month, and day indexes
		// coverting String array into int array
		// creating Date object from int array
		String[] toArray = rowDate.split("-");
		int[] toInt = Arrays.stream(toArray).mapToInt(Integer::parseInt).toArray();
		LocalDate bookedDate = LocalDate.of(toInt[0], toInt[1], toInt[2]);

		// comparing current date with booked date
		int dateComparison = bookedDate.compareTo(currentDate);

		System.out.println(dateComparison);
		return dateComparison > 0 ? true : false;
	}

	@FXML
	void changeColor() {

		// PARTIAL CREDITS
		// https://stackoverflow.com/questions/30889732/javafx-tableview-change-row-color-based-on-column-value

		table.setRowFactory(row -> {
			return new TableRow<BookingHistoryCustomerItem>() {
				@Override
				public void updateItem(BookingHistoryCustomerItem item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) { // If the row is empty
						setText(null);
						setStyle("");
					} else { // If the row is not empty

						// We get here all the info of the Bookings of this row
						BookingHistoryCustomerItem rowInfo = getTableView().getItems().get(getIndex());
						// Style all rows whose status is set to "cancelled"
						if (rowInfo.getStatus().equals("cancelled")) {
							// The background of the row in gray
							setStyle("-fx-background-color: #D3D3D3");
						} else {
							// if the table is reordered the colors update
							// accordingly
							setStyle(table.getStyle());
						}
					}
				}
			};
		});
	}

	@FXML
	public void backToPrevScene(ActionEvent event) throws IOException {

		SceneCreator.launchScene("ManageBookingsScene.fxml", event);
	}

	@FXML
	void getRowId(MouseEvent e) {
		// Creating an object of the class BookingHistoryCustomerItem to get the
		// selected row onClick
		BookingHistoryCustomerItem selectedRow = table.getSelectionModel().getSelectedItem();
		// storing in a String the idNumber of the selected row
		String selectedRowId = selectedRow.getIdNumber();

		this.selectedRowId = selectedRowId;
	}
	
	boolean verifySelectedRow(String selectedRowId, BookingHistoryCustomerItem tableDate){
		return selectedRowId != null && tableDate != null ? true : false;
	}
	

	@SuppressWarnings("unchecked")
	@FXML
	public void modifyStatusInJson() throws FileNotFoundException {

		// modifying the status of the json object to cancelled
		JSONObject jsonObjectToCancel = (JSONObject) user.get(selectedRowId);
		jsonObjectToCancel.replace("status", "cancelled");

		// printing all objects plus updates in the json file
		PrintWriter writer = new PrintWriter(new File(path));
		writer.print(user);
		writer.close();
		// updating the tableView
		setBookingHistoryList("bookingsJSON.txt");
	}

	@FXML
	void deleteBooking(ActionEvent event) throws IOException {
		// creating alert to delete booking
		Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to cancel this booking?", ButtonType.OK, ButtonType.NO);
		alert.showAndWait();

		// user click OK
		if (alert.getResult() == ButtonType.OK) {
			// comparing booking today with current date
			if (deleteBookingValidator() && verifySelectedRow(selectedRowId, tableDate)) {
				modifyStatusInJson();
				SceneCreator.launchScene("BookingHistoryCustomer.fxml", event);
				alert.close();
			} else {
				Alert alert2 = new Alert(AlertType.ERROR, "You cannot delete this booking!", ButtonType.CLOSE);
				alert2.showAndWait();
				if (alert2.getResult() == ButtonType.CLOSE) {
					alert2.close();
				}
			}

		} else if (alert.getResult() == ButtonType.NO) {
			alert.close();
		}

	}

}
