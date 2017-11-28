package compgc01;

import java.time.LocalDate;
import java.time.LocalTime;

/**
 * A class represeting a movie theatre booking.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 28.11.2017
 */
public class Booking {

    private Customer customer;
    private Film film;
    private LocalDate date;
    private LocalTime time;
    private Seat seat;
    private boolean status;

    public Booking(Customer customer, Film film, LocalDate date, LocalTime time, Seat seat) {
        this.customer = customer;
        this.film = film;
        this.date = date;
        this.time = time;
        this.seat = seat;
        status = true;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Film getFilm() {
        return film;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public Seat getSeat() {
        return seat;
    }
    
    public boolean getStatus() {
        return status;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setFilm(Film film) {
        this.film = film;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setSeat(Seat seat) {
        this.seat = seat;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
}