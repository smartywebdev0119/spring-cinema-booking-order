package compgc01;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ToggleButton;
import javafx.scene.text.Text;

public class BookingSummaryController implements Initializable {

    @FXML
    Text nameSummary, filmSummary, dateSummary, timeSummary, seatSummary;
    @FXML
    ToggleButton closeButton, emailButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nameSummary.setText((Main.getCurrentUser().getFullName()));		
        filmSummary.setText(Main.getSelectedFilmTitle());
        dateSummary.setText(Main.getSelectedDate());
        timeSummary.setText(Main.getSelectedTime());
        for (int i = 0; i < Main.getSelectedSeats().size(); i++) {
            seatSummary.setText(seatSummary.getText() + Main.getSelectedSeats().get(i) + " ");
        }
    }

    @FXML
    private void closeStage(ActionEvent event) throws IOException {

        SceneCreator.launchScene("ManageBookingsScene.fxml");
        Main.getStage().centerOnScreen();
    }


    @FXML
    private void emailReminder(ActionEvent event) {

        // SendEmail.sendEmail(Main.getCurrentUser().getEmail());
        SendEmail.sendEmail("lucio.d'alessandro.17@ucl.ac.uk");
    }
}