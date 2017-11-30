package compgc01;

/**
 * A class represeting a film.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 09.11.2017
 */
public class Film {
    
    private String title = "No Title Chosen", description = "No Description Provided";
    private double duration = 0;

    public Film(String title, double duration, String description) {
        this(title, duration);
        if (!title.isEmpty() && duration > 0 && !description.isEmpty()) {
            this.description = description;
        }
    }
    
    public Film(String title, double duration) {
        if (!title.isEmpty() && duration > 0) {
            this.title = title;
            this.duration = duration;
        }
    }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        if (!title.isEmpty())
            this.title = title;
    }
    
    public double getDuration() {
        return duration;
    }
    
    public void setDuration(double duration) {
        if (duration > 0)
            this.duration = duration;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        if (!description.isEmpty())
            this.description = description;
    }
}