package compgc01;

/**
 * A class represeting a film.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 02.12.2017
 */
public class Film {

    private String title = "Default Title", description = "Default Description",
            startDate = "yyyy-mm-dd", endDate = "yyyy-mm-dd", time = "hh:mm";

    public Film(String title, String description, String startDate, String endDate, String time) {

        if (!title.isEmpty() && !description.isEmpty() && !startDate.isEmpty() && !endDate.isEmpty() && !time.isEmpty())
            this.title = title;
        this.description = description;
        this.startDate = startDate;
        this.endDate = endDate;
        this.time = time;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        if (!title.isEmpty())
            this.title = title;
    }

    public String getDescription() {

        return description;
    }

    public void setDescription(String description) {

        if (!description.isEmpty())
            this.description = description;
    }

    public String getStartDate() {

        return startDate;
    }

    public void setStartDate(String startDate) {

        if (!startDate.isEmpty())
            this.startDate = startDate;
    }

    public String getEndDate() {

        return endDate;
    }

    public void setEndDate(String endDate) {

        if (!endDate.isEmpty())
            this.endDate = endDate;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {

        if (!time.isEmpty())
            this.time = time;
    }
}