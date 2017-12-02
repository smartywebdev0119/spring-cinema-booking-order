package compgc01;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
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
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

// implementing the interface Initializable so that the method initialize gets read at load time
public class BookingHistoryEmployeeController extends UserSceneController implements Initializable {

    @FXML 
    private TableView <BookingHistoryCustomerItem> table;
    @FXML 
    private TableColumn <BookingHistoryCustomerItem, String> status, firstName, lastName, film, date, time, seat;
    @FXML
    private ArrayList<BookingHistoryCustomerItem> listRows = new ArrayList<BookingHistoryCustomerItem>();
    @FXML
    public ObservableList<BookingHistoryCustomerItem> populateTable;
    @FXML
    private Button backButton;

    // mothod that gets executed at load time
    @Override
    public void initialize(URL location, ResourceBundle resources){

//        setBookingHistoryList("bookingsJSON.txt");
    }

//    private JSONObject setBookingHistoryList(String bookingsFile) {
//
//        // specifying how to populate the columns of the table
//        status.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("status"));
//        firstName.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("firstName"));
//        lastName.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("lastName"));
//        film.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("film"));
//        date.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("date"));
//        time.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("time"));
//        seat.setCellValueFactory(new PropertyValueFactory<BookingHistoryCustomerItem, String>("seat"));     
//
//        JSONParser parser = new JSONParser();
//
//        try {
//            // getting the path of the booking history .json file
//            String path = URLDecoder.decode(Main.getPath() + "res/" + bookingsFile, "UTF-8");
//            // System.out.println(path);
//
//            // creating JSONObject containing all contents of the JSON file
//            JSONObject user =  (JSONObject) parser.parse(new FileReader(path));
//            // System.out.println((String)user.get("firstName"));
//
//            // looping through .json file keys
//            // creating a variable to temporarily store all values of the keys
//            // pushing the values in the ArrayList populated by BookingHistory objects
//            for (Object s : user.keySet()) {
//                JSONObject bookingRow = (JSONObject) user.get(s);
//                // System.out.println(bookingRow.get("lastName"));
//                listRows.add( new BookingHistoryCustomerItem ((String) bookingRow.get("status"), (String) bookingRow.get("firstName"), (String) bookingRow.get("lastName"), (String) (s), (String) bookingRow.get("date"), (String) bookingRow.get("time"), (String) bookingRow.get("seat")));
//            }
//
//            // storing elements in the ObservableLisr
//            // populating the table with the elements stored in ObservableList
//            // the rows of the list are automatically sorted by key value length! Sort them by date istead!
//            populateTable = FXCollections.observableArrayList(getBookingHistoryList());
//            table.getItems().addAll(populateTable); 
//
//            return user;
//
//            // reading from array
//            //                JSONArray arr =  (JSONArray) jsonObject.get("array");
//            //                for (Object s : arr) {   
//            //                list.add((String) s);
//            //                }
//            //                for(String s : list){
//            //                    System.out.println(s);
//            //                }
//            ///////////////////////////////
//        }  
//        catch(FileNotFoundException e) {e.printStackTrace();}
//        catch(IOException e) {e.printStackTrace();}
//        catch(ParseException e) {e.printStackTrace();}
//        catch(Exception e) {e.printStackTrace();}
//
//        return new JSONObject();
//    }

    // returning objects of ArrayList to be called in ObservableList
    @FXML
    public ArrayList<BookingHistoryCustomerItem> getBookingHistoryList() {
        return listRows;
    }

    // removing items from the table -- to be finished. It needs to remove the items from the json file as well!
    @FXML
    public void ButtonClicked(MouseEvent e) {
        
    	try 
    	{
    		ObservableList<BookingHistoryCustomerItem> row , allRows;
        	allRows = table.getItems();
        	row = table.getSelectionModel().getSelectedItems();         
        	row.forEach(allRows::remove);
    	}
    	catch (IndexOutOfBoundsException ex){
    		System.out.println(ex.getMessage());
    	}

        if(table.getItems().get(0).getDate().equals("")) {
        }

    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("ManageBookingsScene.fxml", event);
    }
}