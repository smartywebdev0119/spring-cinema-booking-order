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

public class ViewSelectedFilmController implements Initializable {

    Film selectedFilm = null;

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
    Button backButton, bookButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {		
        
        selectedFilm = getFilmByTitle(Main.getSelectedFilmTitle());
        // System.out.println(Main.getSelectedFilmTitle());
        try {
            String path = URLDecoder.decode(Main.getPath() + "res/images/filmImages/", "UTF-8");
            File file = new File(path + selectedFilm.getTitle() + ".png");
            Image img = SwingFXUtils.toFXImage(ImageIO.read(file), null);
            selectedFilmPoster.setImage(img);
        } catch (IOException e) {
            e.printStackTrace();
        }
        title.setText(selectedFilm.getTitle());
        description.setText(selectedFilm.getDescription());
        startDate.setText(selectedFilm.getStartDate());
        endDate.setText(selectedFilm.getEndDate());
        time.setText(selectedFilm.getTime());
    }

    private Film getFilmByTitle (String title) {

        for (Film film : Main.getFilmList()) {
            if (film.getTitle().equals(title))
                return film;
        }
        
        return null;
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