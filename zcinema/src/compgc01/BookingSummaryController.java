package compgc01;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class BookingSummaryController extends ManageBookingsController implements Initializable {
	
	@FXML
	Text nameSummary, filmSummary, dateSummary, timeSummary, seatSummary;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {

		nameSummary.setText((Main.getCurrentUser().getFullName()));
//		filmSummary.setText(super.filmDropDownList.getValue().toString());
//		dateSummary.setText(super.datePicker.getValue().toString());
//		timeSummary.setText(super.timeDropDownList.getValue().toString());
//		seatSummary.setText(selectedSeats.get(0).toString());
		
	}
	
	
	@FXML
    private void closeStage(ActionEvent event) throws IOException {
		ManageBookingsController.getStage().close();
        SceneCreator.launchScene("ManageBookingsScene.fxml");
	}

	
	
	
}
