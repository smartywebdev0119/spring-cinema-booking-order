package compgc01.bookinghistory;

import javafx.beans.property.SimpleStringProperty;

public class MovieList {

	private final SimpleStringProperty status, name, lastname, movie, date, time, price;

	public MovieList (String status, String name, String lastname, String movie, String date, String time, String price){
		this.status = new SimpleStringProperty(status);
		this.name = new SimpleStringProperty(name);
		this.lastname = new SimpleStringProperty(lastname);
		this.movie = new SimpleStringProperty(movie);
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleStringProperty(time);
		this.price = new SimpleStringProperty(price);
	}
	
	public String getStatus(){
		return status.get();
	}
	public String getName(){
		return name.get();
	}
	public String getLastname(){
		return lastname.get();
	}
	public String getMovie(){
		return movie.get();
	}
	public String getDate(){
		return date.get();
	}
	public String getTime(){
		return time.get();
	}
	public String getPrice(){
		return price.get();
	}


}
