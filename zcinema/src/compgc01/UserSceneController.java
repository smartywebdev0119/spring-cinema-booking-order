package compgc01;

import java.io.*;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

/**
 * The controller for the User Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 04.12.2017
 */
public class UserSceneController {

    @FXML
    Button logOutButton, manageMoviesButton, manageBookingsButton, sendEmailButton, exportFilmList;
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

        // loading login stage
        SceneCreator.launchScene("LoginScene.fxml");
    }

    @FXML
    public void manageBookingsClick(ActionEvent event) throws IOException {

        // calling the scene from parent class and avoiding code duplication
        SceneCreator.launchScene("ManageBookingsScene.fxml");

        //		 IMPORTANT FOR LATER TO SHOW SEATS ALREADY BOOKED!!!
        //		 for(Node node : gridSeats.getChildren()) {
        //			 if(node.getStyle().length() > 55){
        //				 String getId = node.getId();
        //				 System.out.println(getId);
        //				 redFixedSeats.add(getId);
        //			 }
        //		 }
    }

    // NEED TO CREATE VIEW ALL FILMS SCENE FOR CUSTOMERS
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
            manageMoviesButton.setText("View Films");
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
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        SceneCreator.launchScene("UserScene.fxml");
        personaliseScene();
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
    
    /////////////////////////////////////////////////////////////////////
    @FXML
    void exportFilmList (ActionEvent e) {

        ArrayList <BookingHistoryItem> exportableList = Main.getBookingList();

        for(BookingHistoryItem c : exportableList) {
            System.out.println(c.getDate());
            System.out.println(c.getTime());
            System.out.println(c.getSeat());
        }
    }
}