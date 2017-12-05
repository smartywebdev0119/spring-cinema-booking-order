package compgc01;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class ViewSelectedFilmController implements Initializable {

	@FXML
	Text title;
	@FXML
	Text description;
	@FXML
	Text startDate;
	@FXML
	Text endDate;
	@FXML
	Text time;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {		
		
		
		//still working on this page
		  
		  for (Film film : Main.getFilmList()) {
			  title.setText(film.getTitle());
			  description.setText(film.getDescription());
			  startDate.setText(film.getStartDate());
			  endDate.setText(film.getEndDate());
			  time.setText(film.getTime());
          }
		
	}
	
	
	@FXML
	void GetMovieInfo () {
		
		
	}
	
	@FXML
    public void backToPrevScene(ActionEvent event) throws IOException {
        
            SceneCreator.launchScene("ViewFilmsScene.fxml", event);
         
    }
	
	
	
	

}
