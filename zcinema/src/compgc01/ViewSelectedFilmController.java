package compgc01;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ResourceBundle;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * The controller for the View Selected Film Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 09.12.2017
 */
public class ViewSelectedFilmController implements Initializable {

    Film selectedFilm = null;
    File imgFile = null;
    
    @FXML
    ImageView selectedFilmPoster;
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
    @FXML
    Button backButton, bookButton, deleteFilmButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {	
    	
    	if(Main.isEmployee())
    		bookButton.setText("Go to Bookings");

        selectedFilm = Main.getFilmByTitle(Main.getSelectedFilmTitle());
        // System.out.println(Main.getSelectedFilmTitle());
        try {
            String path = URLDecoder.decode(Main.getPath() + "res/images/filmImages/", "UTF-8");
            imgFile = new File(path + selectedFilm.getTitle() + ".png");
            Image img = SwingFXUtils.toFXImage(ImageIO.read(imgFile), null);
            selectedFilmPoster.setImage(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        title.setText(selectedFilm.getTitle());
        description.setText(selectedFilm.getDescription());
        startDate.setText(selectedFilm.getStartDate());
        endDate.setText(selectedFilm.getEndDate());
        time.setText(selectedFilm.getTimes()[0] + ", " + selectedFilm.getTimes()[1] + ", " + selectedFilm.getTimes()[2]);
        
        if (!Main.isEmployee())
            deleteFilmButton.setVisible(false);
    }

    @FXML
    public void deleteFilm(ActionEvent event) throws IOException {

        Main.modifyJSONFile("filmsJSON.txt", selectedFilm.getTitle(), "", "delete");
        imgFile.delete();
        
        Main.resetFilmList();
        Main.readJSONFile("filmsJSON.txt");
        
        backToPrevScene(event);
    }

    @FXML
    public void goToBookingScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("ManageBookingsScene.fxml");
    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("ViewFilmsScene.fxml");
    }
}