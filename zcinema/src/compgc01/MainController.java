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

import javax.imageio.ImageIO;

import de.jensd.fx.glyphs.materialicons.MaterialIconView;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;

/**
 * The controller for our GUI.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 26.11.2017
 */
public class MainController extends SceneCreator {

    static User currentUser;
    static Boolean employeeMode = false;
    public boolean gridSeatsStartVisibility = true;
    static ArrayList<Booking> bookings;
    static ArrayList<Integer> redFixedSeats = new ArrayList<Integer>(5);

    @FXML
    TextField usernameBox;
    @FXML
    PasswordField passwordBox;
    @FXML
    Button logInButton, logOutButton, manageMoviesButton, manageBookingsButton, backButton, sendEmailButton;
    @FXML
    Label windowTitleLabel, firstNameLabel, lastNameLabel, titleLabel, emailLabel;
    @FXML
    Label firstNameLabelNew, lastNameLabelNew, titleLabelNew, emailLabelNew;
    @FXML
    TextField updateFirstName, updateLastName, updateEmail, updatePassword, titleMovie;
    @FXML
    Text titleMovieNew, aboutMovieNew, dateFromMovieNew, dateToMovieNew, timeMovieNew;
    @FXML
    TextArea aboutMovie;
    @FXML
    DatePicker dateFromMovie, dateToMovie;
    @FXML
    ImageView uploadedUserIcon, uploadedMoviePicture;
    @FXML
    GridPane gridSeats;
    @FXML
    ComboBox<String> timeMovie;
    @FXML
    MaterialIconView a1;

    @FXML
    public void exitButton(MouseEvent event) {
        System.exit(0);
    }

    @FXML
    public void loginClick(ActionEvent event) throws IOException {

        ArrayList<User> users = new ArrayList<User>();
        users.addAll(Main.getEmployeeList());
        users.addAll(Main.getCustomerList());

        for (User u : users) {
            if (usernameBox.getText().equals(u.getUsername()) && passwordBox.getText().equals(u.getPassword())) {
                currentUser = u;
                if (u.getType().equals("employee")) {
                    employeeMode = true;
                }

                // loading new scene
                launchScene("UserScene.fxml", event);

                // personalising page based on logged-in user
                personaliseScene();
            }
        }
    }

    @FXML
    public void logOutClick(ActionEvent event) throws IOException {
        
        // resetting current user for security purposes
        currentUser = new User("default", "default", "default", "default", "default");
        employeeMode = false;
        bookings = new ArrayList<Booking>();

        // loading login stage
        launchScene("LoginScene.fxml", event);
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
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        launchScene("UserScene.fxml", event);
        personaliseScene();
    }

    @FXML
    public void manageBookingsClick(ActionEvent event) throws IOException {

        // calling the scene from parent class and avoiding code duplication
        launchScene("ManageBookingsScene.fxml", event);

        //		 IMPORTANT FOR LATER TO SHOW SEATS ALREADY BOOKED!!!
        //		 for(Node node : gridSeats.getChildren()) {
        //			 if(node.getStyle().length() > 55){
        //				 String getId = node.getId();
        //				 System.out.println(getId);
        //				 redFixedSeats.add(getId);
        //			 }
        //		 }
    }

    //////////////////////////////////////////////
    // getting the index of the seats!
    @FXML
    public void getSeatIndex(MouseEvent e) {

        try {
            Node target = (Node) e.getTarget();
            int colIndex = GridPane.getColumnIndex(target);
            int rowIndex = GridPane.getRowIndex(target);
            System.out.print(rowIndex + ",");
            System.out.print(colIndex + "\n");
        } catch (NullPointerException ex) {
            System.out.println("Please click on a seat!");
        }
    }

    @FXML
    public void turnSeatRed(MouseEvent e) {

        if(((Node) e.getSource()).getStyle().length() == 55){
            Alert alert = new Alert(AlertType.WARNING, "The seat " + ((Node) e.getSource()).getId()  + " is booked already!", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }

        for (Node node : gridSeats.getChildren()) {
            node.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        }

        if (((Node) e.getSource()).getStyle().length() == 55) {
            ((Node) e.getSource()).setStyle("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        } else {
            ((Node) e.getSource()).setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        }

        //		 BOOKED SEATS COLORS CAN'T TURN BLACK!!
        //		 for(Integer id : redFixedSeats){
        //		 gridSeats.getChildren().get(id).setStyle("-fx-fill:#c2a9a9; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        //		 }

        // Alert for booked seat

    }
    ///////////////////////////////////////////

    @FXML
    public void manageMoviesClick(ActionEvent event) throws IOException {
        launchScene("ManageFilmsScene.fxml", event);

        // personalising page based on logged-in user
        MainController controller = super.getController(loader);

        ObservableList<String> obsList = FXCollections.observableArrayList("14:00", "15:00", "16:00", "17:00", "18:00",
                "19:00", "20:00", "21:00", "22:00", "23:00");
        controller.timeMovie.setItems(obsList);
    }

    @FXML
    public void sendEmailClick(ActionEvent event) throws IOException {
        Main m = Main.getMainApplication();
        m.getHostServices().showDocument("mailto:" + currentUser.getEmail());
    }

    @FXML
    public void showBookingHistoryOnClick(ActionEvent event) throws IOException {
        launchScene("BookingHistory.fxml", event);

    }


    // Edit Info Scene
    @FXML
    public void editUpdateText(KeyEvent e) {

        TextField field = (TextField) e.getSource();
        if (field.getText().length() > 30)
            field.setEditable(false);

        // Pattern.matches("[A-Za-z]*", (CharSequence) updateName);

        if (e.getCode().equals(KeyCode.BACK_SPACE))
            field.setEditable(true);

        switch (((Node) e.getSource()).getId()) {
        case "updateFirstName":
            firstNameLabelNew.setText(updateFirstName.getText());
            break;
        case "updateLastName":
            lastNameLabelNew.setText(updateLastName.getText());
            break;
        case "updateEmail":
            emailLabelNew.setText(updateEmail.getText());
            break;
        }
    }

    @FXML
    public void getDateTime(ActionEvent e) {

        switch (((Node) e.getSource()).getId()) {
        case "dateFromMovie":
            LocalDate ld1 = dateFromMovie.getValue();
            dateFromMovieNew.setText(ld1.toString());
            break;
        case "dateToMovie":
            LocalDate ld2 = dateToMovie.getValue();
            dateToMovieNew.setText(ld2.toString());
            break;
        case "timeMovie":
            timeMovieNew.setText(timeMovie.getValue().toString());
            break;
        }
    }

    // Add Movie Scene
    @FXML
    public void textUpdateMovie(KeyEvent e) {

        switch (((Node) e.getSource()).getId()) {

        case "titleMovie":
            if (titleMovie.getText().length() > 20) {
                titleMovie.setEditable(false);
            }
            break;
        case "aboutMovie":
            if (aboutMovie.getText().length() > 220) {
                aboutMovie.setEditable(false);
            }
            break;
        }

        if (e.getCode().equals(KeyCode.BACK_SPACE)) {
            titleMovie.setEditable(true);
            aboutMovie.setEditable(true);

        }

        switch (((Node) e.getSource()).getId()) {
        case "titleMovie":
            titleMovieNew.setText(titleMovie.getText());
            break;
        case "aboutMovie":
            aboutMovieNew.setText(aboutMovie.getText());
            break;
        }
    }

    @FXML
    public void editInfoClick(ActionEvent event) throws IOException {

        launchScene("EditInfoScene.fxml", event);
        personaliseScene();
    }

    @FXML
    public void saveClick(ActionEvent event) throws IOException {

        String userType = currentUser.getType();

        if (!updateFirstName.getText().trim().isEmpty()) {
            Main.modifyJSONFile(userType + "sJSON.txt", currentUser.getUsername(), "firstName", updateFirstName.getText());
            currentUser.setFirstName(updateFirstName.getText());
        }
        if (!updateLastName.getText().trim().isEmpty()) {
            Main.modifyJSONFile(userType + "sJSON.txt", currentUser.getUsername(), "lastName", updateLastName.getText());
            currentUser.setLastName(updateLastName.getText());
        }
        if (!updateEmail.getText().trim().isEmpty()) {
            Main.modifyJSONFile(userType + "sJSON.txt", currentUser.getUsername(), "email", updateEmail.getText());
            currentUser.setEmail(updateEmail.getText());
        }

        launchScene("UserScene.fxml", event);
        personaliseScene();
    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        launchScene("UserScene.fxml", event);
        personaliseScene();
    }

    void personaliseScene() throws IOException {

        // personalising page based on logged-in user
        MainController controller = super.getController(loader);

        controller.firstNameLabel.setText(currentUser.getFirstName());
        controller.lastNameLabel.setText(currentUser.getLastName());

        if (!employeeMode) {
            controller.titleLabel.setText("Customer");

            if (super.sceneName.equals("UserScene.fxml")) {
                controller.windowTitleLabel.setText("Customer View");
                controller.manageMoviesButton.setText("View Movies");
                controller.manageBookingsButton.setText("View Bookings");
            }
        }

        if (super.sceneName.equals("UserScene.fxml")) {
            String path = Main.getPath();
            Image img;
            path = URLDecoder.decode(path + "res/images/userImages/", "UTF-8");

            File file = new File(path + currentUser.getUsername() + ".png");
            img = SwingFXUtils.toFXImage(ImageIO.read(file), null);
            super.getController(loader).uploadedUserIcon.setImage(img);
        } else if (super.sceneName.equals("EditInfoScene.fxml")) {
            controller.windowTitleLabel.setText("Edit " + titleLabel.getText() + " Profile");
            controller.emailLabel.setText(currentUser.getEmail());
            controller.titleLabelNew.setText(titleLabel.getText());
        }
    }

    private void changeImage() throws IOException {

        try {
            FileChooser fc = new FileChooser();
            File selectedFile = fc.showOpenDialog(null);
            // checking that input file is not null and handling the exception
            if (selectedFile == null)
                return;
            else {
                Image img = SwingFXUtils.toFXImage(ImageIO.read(selectedFile), null);

                if (super.sceneName.equals("UserScene.fxml"))
                    super.getController(loader).uploadedUserIcon.setImage(img);
                else if (super.sceneName.equals("ManageFilmsScene.fxml"))
                    super.getController(loader).uploadedMoviePicture.setImage(img);

                String path = Main.getPath();
                String folderPath = URLDecoder.decode(path + "res/images/userImages/", "UTF-8");
                File uploads = new File(folderPath);
                File file = new File(uploads, currentUser.getUsername() + ".png");
                InputStream input = Files.newInputStream(selectedFile.toPath());
                Files.copy(input, file.toPath(), StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (FileNotFoundException ex) {
            // Logger.getLogger(MainController.class.getName()).log(Level.SEVERE,
            // null, ex);
        }
    }


    // maybe implement a clock if we have time
    public LocalDateTime testLocalDateTime() {

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
}