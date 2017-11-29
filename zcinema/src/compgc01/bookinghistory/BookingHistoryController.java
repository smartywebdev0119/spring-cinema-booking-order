package compgc01.bookinghistory;

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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

public class BookingHistoryController implements Initializable {
	
	@FXML private TableView <MovieList> table;
	
	@FXML 
	private TableColumn <MovieList, String> status, name, lastname, movie, date, time, price;
	
	ArrayList<String> list = new ArrayList<String>(3);

	
	
	@FXML
	public ObservableList<MovieList> populateTable = FXCollections.observableArrayList(
			
			
			new MovieList("er", "sd", "er", "sd", "er", "df", "df"),
			new MovieList("er", "sd", "er", "sd", "er", "df", "df")

			
			);
	
	@Override
	public void initialize(URL location, ResourceBundle resources){
		
		status.setCellValueFactory(new PropertyValueFactory<MovieList, String>("status"));
		name.setCellValueFactory(new PropertyValueFactory<MovieList, String>("name"));
		lastname.setCellValueFactory(new PropertyValueFactory<MovieList, String>("lastname"));
		movie.setCellValueFactory(new PropertyValueFactory<MovieList, String>("movie"));
		date.setCellValueFactory(new PropertyValueFactory<MovieList, String>("date"));
		time.setCellValueFactory(new PropertyValueFactory<MovieList, String>("time"));
		price.setCellValueFactory(new PropertyValueFactory<MovieList, String>("price"));

		table.setItems(populateTable);
	
	}

	
	@FXML
	public void readBookingHistory (ActionEvent action){

		Main m = new Main();
		 JSONParser parser = new JSONParser();
		 
		 try 
		 {
			 
			 String path = m.getPath();
             path = URLDecoder.decode(path + "bookingsJSON.txt", "UTF-8");
             
             
             
             System.out.println("Finding the file: \n" + path);
			 
			 Object obj = parser.parse(new FileReader(path));
			 JSONObject jsonObject = (JSONObject) obj;
			 
			 Object name = (Object) jsonObject.get("Titanic");

	         // reading from array! 
			  JSONArray arr =  (JSONArray) jsonObject.get("array");
			  Iterator<String> iterator = arr.iterator();
			  

			  for (Object s : arr) {   
			  list.add((String) s);
			  }
			  
			  for(String s : list){
				  System.out.println(s);
			  }
			  
		 } 
		 
		 catch(FileNotFoundException e) {e.printStackTrace();}
		 catch(IOException e) {e.printStackTrace();}
		 catch(ParseException e) {e.printStackTrace();}
		 catch(Exception e) {e.printStackTrace();}


		 
		 
		
	}
	
	@FXML
	public void out(MouseEvent e){
		System.exit(0);
	}
	
}
