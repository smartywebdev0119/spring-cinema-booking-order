package compgc01;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.ResourceBundle;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import compgc01.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

// implementing the interface Initializable so that the method initialize gets read at load time
public class BookingHistoryController implements Initializable {
	
	@FXML 
	private TableView <BookingHistory> table;
	@FXML 
	private TableColumn <BookingHistory, String> status, name, lastname, movie, date, time, seat;
	@FXML 
    private static ArrayList<BookingHistory> listRows = new ArrayList<BookingHistory>();
	@FXML
	public ObservableList<BookingHistory> populateTable;
	
	//end instance variables
	//////////////////////////////////
	
	// mothod that gets executed at load time
	@Override
	public void initialize(URL location, ResourceBundle resources){
		
		// specifying how to populate the columns of the table
		status.setCellValueFactory(new PropertyValueFactory<BookingHistory, String>("status"));
		name.setCellValueFactory(new PropertyValueFactory<BookingHistory, String>("name"));
		lastname.setCellValueFactory(new PropertyValueFactory<BookingHistory, String>("lastname"));
		movie.setCellValueFactory(new PropertyValueFactory<BookingHistory, String>("movie"));
		date.setCellValueFactory(new PropertyValueFactory<BookingHistory, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<BookingHistory, String>("time"));
		seat.setCellValueFactory(new PropertyValueFactory<BookingHistory, String>("seat"));		
		
		// object of the class Main
		Main m = new Main();
		JSONParser parser = new JSONParser();
		 
		 try {
			 	// getting the right path for the booking history json file
	            String path = m.getPath();
	            path = URLDecoder.decode(path + "bookingsJSON.txt", "UTF-8");
		        System.out.println(path);

		        // creating JSONObject containing all content of the json file
	            JSONObject user =  (JSONObject) parser.parse(new FileReader(path));
	            System.out.println(user);
	            
	           // looping through json file keys
	           // creating a variable to temporarily store all values of the keys
	           // pushing the values in the ArrayList populated by BookingHistory objects
	            for (Object s : user.keySet()) {
	            	JSONObject bookingsRow = (JSONObject) user.get(s);
	                listRows.add( new BookingHistory ((String) bookingsRow.get("status"), (String) bookingsRow.get("name"), (String) bookingsRow.get("lastname"), (String) (s), (String) bookingsRow.get("date"), (String) bookingsRow.get("time"), (String) bookingsRow.get("seat")));
	            }
	            
	         // storing elements in the ObservableLisr
	         // populating the table with the elements stored in ObservableList
	         // the rows of the list are automatically sorted by key value length! Sort them by date istead!
	            populateTable = FXCollections.observableArrayList(getList());
	            table.getItems().addAll(populateTable); 
	    
	            // reading from array
//				  JSONArray arr =  (JSONArray) jsonObject.get("array");
//				  for (Object s : arr) {   
//				  list.add((String) s);
//				  }
//				  for(String s : list){
//					  System.out.println(s);
//				  }
				  ///////////////////////////////
		 }  
		 catch(FileNotFoundException e) {e.printStackTrace();}
		 catch(IOException e) {e.printStackTrace();}
		 catch(ParseException e) {e.printStackTrace();}
		 catch(Exception e) {e.printStackTrace();}	
	}
	
	//returning objects of ArrayList to be called in ObservableList
	@FXML
	public ArrayList<BookingHistory> getList(){
		return listRows;
	}
	
	
	//removing items from the table -- to be finished. It needs to remove the items from the json file as well!
	@FXML
	public void ButtonClicked(MouseEvent e) {
	ObservableList<BookingHistory> row , allRows;
	  allRows = table.getItems();
	  row = table.getSelectionModel().getSelectedItems();         
	  row.forEach(allRows::remove);
	 
	 
	  if(table.getItems().get(0).getDate().equals("")) {
	  }
	}
	
	@FXML
	public void backToPrevScene(ActionEvent event) throws IOException {
	    MainController controller = new MainController();
		controller.launchScene("ManageBookingsScene.fxml", event);
	}
}
