package compgc01;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URLDecoder;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;

public class BookingHistoryController {

	
	public void readBookingHistory (ActionEvent action){
		
		Main m = new Main();
//		 JSONParser parser = new JSONParser();
		 
		 try 
		 {
			 
			 String path = m.getPath();
             path = URLDecoder.decode(path + "bookingsJSON.txt", "UTF-8");
             
             
             System.out.println("Finding the file: \n" + path);
			 
//			 Object obj = parser.parse(new FileReader(path));
//			 JSONObject jsonObject = (JSONObject) obj;
			 
//			 Object name = (Object) jsonObject.get("Titanic");

			  JSONParser parser = new JSONParser();
	            JSONObject users =  (JSONObject) parser.parse(new FileReader(path));
	            for (Object s : users.keySet()) {
	                // System.out.println((String) s);
	                JSONObject user = (JSONObject) users.get(s);
			 
			 
			 
			 System.out.println("Finding the object: \n" + user);
	            }
		 
		 } 
		 
		 catch(FileNotFoundException e) {e.printStackTrace();}
		 catch(IOException e) {e.printStackTrace();}
		 catch(ParseException e) {e.printStackTrace();}
		 catch(Exception e) {e.printStackTrace();}


		 
		 
		
	}
	
	
	public void out(MouseEvent e){
		System.exit(0);
	}
	
}
