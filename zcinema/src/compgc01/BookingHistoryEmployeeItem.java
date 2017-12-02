package compgc01;

import javafx.beans.property.SimpleStringProperty;

/**
 * A class represeting a booking history item.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 28.11.2017
 */
public class BookingHistoryEmployeeItem {

	private final SimpleStringProperty status, firstName, lastName, film, date, time, seat;

	public BookingHistoryEmployeeItem (String status, String firstName, String lastName, String film, String date, String time, String price){
		this.status = new SimpleStringProperty(status);
		this.firstName = new SimpleStringProperty(firstName);
		this.lastName = new SimpleStringProperty(lastName);
		this.film = new SimpleStringProperty(film);
		this.date = new SimpleStringProperty(date);
		this.time = new SimpleStringProperty(time);
		this.seat = new SimpleStringProperty(price);
	}
	
	public String getStatus(){
		return status.get();
	}
	public String getFirstName(){
		return firstName.get();
	}
	public String getLastName(){
		return lastName.get();
	}
	public String getFilm(){
		return film.get();
	}
	public String getDate(){
		return date.get();
	}
	public String getTime(){
		return time.get();
	}
	public String getSeat(){
		return seat.get();
	}
}