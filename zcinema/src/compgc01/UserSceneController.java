package compgc01;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.util.Pair;

/**
 * The controller for the User Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 12.12.2017
 */
public class UserSceneController {

    @FXML
    Button logOutButton, manageFilmsButton, manageBookingsButton, sendEmailButton, exportFilmList;
    @FXML
    Label windowTitleLabel, firstNameLabel, lastNameLabel, titleLabel, emailLabel;
    @FXML
    ImageView userSceneBackground, uploadedUserIcon;

    @FXML
    void initialize() throws IOException{

        personaliseScene();

        String path = URLDecoder.decode(Main.getPath() + "res/images/backgroundImages/", "UTF-8");

        if (Main.isChristmasSeason() && !Main.isEmployee()) {
            File file = new File(path + "christmas-02.png");
            Image img = SwingFXUtils.toFXImage(ImageIO.read(file), null);
            userSceneBackground.setImage(img);
        }
        else {
            File file = new File(path + "backgroundUserScene-02.png");
            Image img = SwingFXUtils.toFXImage(ImageIO.read(file), null);
            userSceneBackground.setImage(img);
        }
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
        m.getHostServices().showDocument("mailto:" + "uclcinemaapp@gmail.com");
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
            File selectedImage = fc.showOpenDialog(null);
            // checking that input file is not null and handling the exception
            if (selectedImage == null)
                return;
            else if (ImageIO.read(selectedImage) == null) {
                Alert alert = new Alert(AlertType.WARNING, "Please upload an image in PNG or JPG format!", ButtonType.OK);
                alert.showAndWait();
                if(alert.getResult() == ButtonType.OK) {
                    return;
                }
            }
            else {
                Image img = SwingFXUtils.toFXImage(ImageIO.read(selectedImage), null);

                uploadedUserIcon.setImage(img);

                String folderPath = URLDecoder.decode(Main.getPath() + "res/images/userImages/", "UTF-8");
                File uploads = new File(folderPath);
                File file = new File(uploads, Main.getCurrentUser().getUsername() + ".png");
                InputStream input = Files.newInputStream(selectedImage.toPath());
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (FileNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
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

                    if (booking.getFilm().equals(filmTitle) && ("date: " + booking.getDate() + ", time: " + booking.getTime()).equals(dateAndTime) && !booking.getStatus().equals("cancelled")) {
                        numberOfBookingsAtSpecificDateAndTime++;
                    }

                    // System.out.println(numberOfBookingsAtSpecificDateAndTime);
                }
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