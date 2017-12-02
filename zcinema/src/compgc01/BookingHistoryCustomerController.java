package compgc01;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

// implementing the interface Initializable so that the method initialize gets read at load time
public class BookingHistoryCustomerController extends UserSceneController implements Initializable {

	@FXML
	private TableView<BookingHistoryCustomerItem> table;
	@FXML
	private TableColumn<BookingHistoryCustomerItem, String> idNumber, status, firstName, lastName, film, date, time, seat;
	@FXML
	private ArrayList<BookingHistoryCustomerItem> listRows = new ArrayList<BookingHistoryCustomerItem>();
	@FXML
	public ObservableList<BookingHistoryCustomerItem> populateTable;
	@FXML
	private Button backButton;

	// mothod that gets executed at load time
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		setBookingHistoryList("bookingsJSON.txt");
		changeColor();
		
	}

	private JSONObject setBookingHistoryList(String bookingsFile) {

		// specifying how to populate the columns of the table
		status.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("status"));
		firstName.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("lastName"));
		film.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("film"));
		date.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("time"));
		seat.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("seat"));
		idNumber.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("idNumber"));



		JSONParser parser = new JSONParser();

		try {
			// getting the path of the booking history .json file
			String path = URLDecoder.decode(Main.getPath() + "res/" + bookingsFile, "UTF-8");
//			 System.out.println(path);

			// creating JSONObject containing all contents of the JSON file
			JSONObject user = (JSONObject) parser.parse(new FileReader(path));


			// looping through .json file keys
			// creating a variable to temporarily store all values of the keys
			// pushing the values in the ArrayList populated by BookingHistory
			for (Object s : user.keySet()) {
//				System.out.println(s);
				JSONObject bookingRow = (JSONObject) user.get(s);

				listRows.add(new BookingHistoryCustomerItem
						(
						(String) bookingRow.get("status"),
						(String) bookingRow.get("firstName"), 
						(String) bookingRow.get("lastName"), 
						(String) bookingRow.get("film"), 
						(String) bookingRow.get("date"), 
						(String) bookingRow.get("time"),
						(String) bookingRow.get("seat"),
						(String) (s)
						)
						);
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
			BookingHistoryCustomerItem tableDate;
			tableDate = table.getSelectionModel().getSelectedItem();
			
			// formatting date -> 2017-11-28 -> String
			String rowDate = tableDate.getDate();

			// creating array of Strings with year, month, and day indexes
			// coverting String array into int array
			// creating Date object from int array
			String[] toArray = rowDate.split("-");
			int[] toInt = Arrays.stream(toArray).mapToInt(Integer::parseInt).toArray();
			LocalDate bookedDate = LocalDate.of(toInt[0], toInt[1], toInt[2]);
			
			//comparing current date with booked date
			int dateComparison = bookedDate.compareTo(currentDate);
			
			System.out.println(dateComparison);
			return dateComparison > 0 ? true : false;
	}
	
	@FXML
	void changeColor() {
		
//		PARTIAL CREDITS
//		https://stackoverflow.com/questions/30889732/javafx-tableview-change-row-color-based-on-column-value
		
		table.setRowFactory(row -> {
		    return new TableRow<BookingHistoryCustomerItem>() {
		        @Override
		        public void updateItem(BookingHistoryCustomerItem item, boolean empty){
		            super.updateItem(item, empty);
		            
		            if (item == null || empty) { //If the row is empty
		                setText(null);
		                setStyle("");
		            } else { //If the row is not empty
		            	
		                // We get here all the info of the Bookings of this row
		                BookingHistoryCustomerItem RowInfo = getTableView().getItems().get(getIndex());
		                
		                // Style all rows whose status is set to "cancelled"
		                if (RowInfo.getStatus().equals("cancelled")) {
		                	//The background of the row in gray
		                	setStyle("-fx-background-color: #D3D3D3"); 
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
	void deleteBooking(MouseEvent e){
	

		table.setRowFactory(tv -> {
            TableRow<ObservableList> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                
            });
            return row;
        });

		
		
		
		
		
		
		
		
		
		
		
		//	Alert alert = new Alert(AlertType.CONFIRMATION,
//            "Do you want to cancel this booking?", ButtonType.OK, ButtonType.NO);
//    alert.showAndWait();
//
//    // close alert on click and restore all fields to empty
//    if (alert.getResult() == ButtonType.OK) {
//        if(deleteBookingValidator())
//        	
//      find clicked row, get that name, 
        	
        	
        
    
}
	
	
	
	
	
}