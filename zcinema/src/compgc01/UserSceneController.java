package compgc01;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Pair;

/**
 * The controller for the User Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 08.12.2017
 */
public class UserSceneController {

    @FXML
    Button logOutButton, manageFilmsButton, manageBookingsButton, sendEmailButton, exportFilmList;
    @FXML
    Label windowTitleLabel, firstNameLabel, lastNameLabel, titleLabel, emailLabel;
    @FXML
    ImageView uploadedUserIcon;

    @FXML
    void initialize() throws IOException{

        personaliseScene();
    }

    @FXML
    public void logOutClick(ActionEvent event) throws IOException {

        // resetting current user for security purposes
        Main.setCurrentUser(null);
        Main.setEmployeeMode(false);
        Main.resetEmployeeList();
        Main.resetCustomerList();
        Main.resetFilmList();
        Main.resetBookingList();

        // loading login scene
        SceneCreator.launchScene("LoginScene.fxml");
    }

    @FXML
    public void manageBookingsClick(ActionEvent event) throws IOException {

        // calling the scene from parent class and avoiding code duplication
        SceneCreator.launchScene("ManageBookingsScene.fxml");
    }

    @FXML
    public void manageMoviesClick(ActionEvent event) throws IOException {

        if (Main.isEmployee())
            SceneCreator.launchScene("ManageFilmsScene.fxml");
        else
            SceneCreator.launchScene("ViewFilmsScene.fxml");
    }

    @FXML
    public void sendEmailClick(ActionEvent event) throws IOException {

        Main m = Main.getMainApplication();
        m.getHostServices().showDocument("mailto:" + Main.getCurrentUser().getEmail());
    }

    @FXML
    public void editInfoClick(ActionEvent event) throws IOException {

        SceneCreator.launchScene("EditInfoScene.fxml");
    }

    protected void personaliseScene() throws IOException {

        // personalising page based on logged-in user
        firstNameLabel.setText(Main.getCurrentUser().getFirstName());
        lastNameLabel.setText(Main.getCurrentUser().getLastName());

        if (!Main.isEmployee()) {
            titleLabel.setText("Customer");

            windowTitleLabel.setText("Customer View");
            manageFilmsButton.setText("View Films");
            manageBookingsButton.setText("View Bookings");
            exportFilmList.setVisible(false);

        }

        String path = URLDecoder.decode(Main.getPath() + "res/images/userImages/", "UTF-8");
        File file = new File(path + Main.getCurrentUser().getUsername() + ".png");
        Image img = SwingFXUtils.toFXImage(ImageIO.read(file), null);
        uploadedUserIcon.setImage(img);
    }

    @FXML
    public void uploadImageClick(ActionEvent event) throws IOException {

        /*
         * Inspired by:
         * https://stackoverflow.com/questions/17850191/why-am-i-getting-java-
         * lang-illegalstateexception-not-on-fx-application-thread
         * Avoid throwing IllegalStateException by running from a non-JavaFX thread.
         * Beautiful lambda expression version.
         */
        Platform.runLater(() -> {
            try {
                changeImage();
                SceneCreator.launchScene("UserScene.fxml");
                personaliseScene();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    protected void changeImage() throws IOException {

        try {
            FileChooser fc = new FileChooser();
            File selectedFile = fc.showOpenDialog(null);
            // checking that input file is not null and handling the exception
            if (selectedFile == null)
                return;
            else {
                Image img = SwingFXUtils.toFXImage(ImageIO.read(selectedFile), null);

                uploadedUserIcon.setImage(img);

                String folderPath = URLDecoder.decode(Main.getPath() + "res/images/userImages/", "UTF-8");
                File uploads = new File(folderPath);
                File file = new File(uploads, Main.getCurrentUser().getUsername() + ".png");
                InputStream input = Files.newInputStream(selectedFile.toPath());
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }

    // maybe implement a clock if we have time
    protected LocalDateTime testLocalDateTime() {

        // Get the current date and time
        LocalDateTime currentTime = LocalDateTime.now();
        System.out.println("Current DateTime: " + currentTime);

        LocalDate date1 = currentTime.toLocalDate();
        System.out.println("date1: " + date1);

        Month month = currentTime.getMonth();
        int day = currentTime.getDayOfMonth();
        int seconds = currentTime.getSecond();

        System.out.println("Month: " + month +"day: " + day +"seconds: " + seconds);

        LocalDateTime date2 = currentTime.withDayOfMonth(10).withYear(2012);
        System.out.println("date2: " + date2);

        //12 december 2014
        LocalDate date3 = LocalDate.of(2014, Month.DECEMBER, 12);
        System.out.println("date3: " + date3);

        //22 hour 15 minutes
        LocalTime date4 = LocalTime.of(22, 15);
        System.out.println("date4: " + date4);

        //parse a string
        LocalTime date5 = LocalTime.parse("20:15:30");
        System.out.println("date5: " + date5);

        return currentTime;
    }

    @FXML
    void exportFilmList (ActionEvent e) throws IOException {

        Main.resetFilmList();
        Main.resetBookingList();

        // reading the films and bookings from the JSON file to ensure we are working with the most recent version
        // and there is no duplication
        Main.readJSONFile("filmsJSON.txt");
        Main.readJSONFile("bookingsJSON.txt");

        // clearing the export file, in case it exists from before
        PrintWriter pw = new PrintWriter(new FileOutputStream(
                new File(Main.getPath() + "bookings.csv")));
        pw.close();

        // creating the printwriter using the append option now
        pw = new PrintWriter(new FileOutputStream(
                new File(Main.getPath() + "bookings.csv"), 
                true));

        // mapping film titles to a set of strings representing screening dates and times
        TreeMap<String, TreeSet<String>> map = new TreeMap<String, TreeSet<String>>();
        for (Film film: Main.getFilmList()) {
            for (BookingHistoryItem booking : Main.getBookingList()) {
                if (film.getTitle().equals(booking.getFilm())) {
                    if (!map.containsKey(film.getTitle()))
                        map.put(booking.getFilm(), new TreeSet<String>());

                    map.get(booking.getFilm()).add("date: " + booking.getDate() + ", time: " + booking.getTime());
                }
            }
        }

        // counting number of bookings for each screening (date and time) of each film
        for (String filmTitle : map.keySet()) {
            TreeSet<String> setOfDatesAndTimes = map.get(filmTitle);
            // System.out.println(filmTitle + ":" + setOfDatesAndTimes.size());

            for (String dateAndTime : setOfDatesAndTimes) {
                int numberOfBookingsAtSpecificDateAndTime = 0;

                for (BookingHistoryItem booking : Main.getBookingList()) {
                	if(booking.getStatus().equals("booked")){
                    if (booking.getFilm().equals(filmTitle) && ("date: " + booking.getDate() + ", time: " + booking.getTime()).equals(dateAndTime)){
                        numberOfBookingsAtSpecificDateAndTime++;
                    System.out.println(booking.getFilm() + ": " + dateAndTime);}
                }}

                // System.out.println(numberOfBookingsAtSpecificDateAndTime);

                // printing to export.csv file
                pw.append("title: " + filmTitle + ", " + dateAndTime + ", booked seats: " + numberOfBookingsAtSpecificDateAndTime + ", available seats: " + (18 - numberOfBookingsAtSpecificDateAndTime) + "\n");
            }
        }

        pw.close();

        // creating a custom dialog to inform the user
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Successful Export!");
        dialog.setHeaderText("Exported to 'bookings.csv' file.");

        // setting the icon
        // image credit: https://thenounproject.com/term/csv-file/56841/
        File file = new File(URLDecoder.decode(Main.getPath() + "res/images/backgroundImages/csv.png", "UTF-8"));
        Image img = SwingFXUtils.toFXImage(ImageIO.read(file), null);
        dialog.setGraphic(new ImageView(img));

        // setting the button type
        ButtonType okButtonType = new ButtonType("OK", ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType);

        dialog.showAndWait();
    }
}