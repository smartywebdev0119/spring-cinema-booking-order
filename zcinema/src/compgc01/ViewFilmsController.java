package compgc01;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * The controller for the View Films Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 04.12.2017
 */
public class ViewFilmsController implements Initializable {

    @FXML
    ScrollPane scrollPane;
    @FXML
    GridPane grid;
    @FXML
    Button backButton;
    @FXML
    ImageView pic;
    @FXML
    Image image;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try{    
            // getting folder path
            String path = URLDecoder.decode(Main.getPath() + "res/images/filmImages/", "UTF-8");
            // creating file object passing in the constructor the folder path
            File folder = new File(path);
            // pushing single path files in the array filelist1
            ArrayList<File> fileList = new ArrayList<>();
            HBox hb = new HBox();
            for (File file : folder.listFiles()) {
                if (!file.toString().contains("defaultFilmPoster.png") && !file.toString().contains("DS_Store"))
                    fileList.add(file);
            }

            // gridpane settings
            // setting exterior grid padding
            grid.setPadding(new Insets(7,7,7,7));
            // setting interior grid padding
            grid.setHgap(10);
            grid.setVgap(10);
            // grid.setGridLinesVisible(true);

            int imageCol = 0;
            int imageRow = 0;

            for (int i = 0; i < fileList.size(); i++) {
                // System.out.println(fileList.get(i).getName());
                image = new Image(fileList.get(i).toURI().toString());
                pic = new ImageView();
                pic.setFitWidth(160);
                pic.setFitHeight(220);
                pic.setImage(image);
                hb.getChildren().add(pic);
                GridPane.setConstraints(pic, imageCol, imageRow, 1, 1, HPos.CENTER, VPos.CENTER);
                //		    grid.add(pic, imageCol, imageRow);
                grid.getChildren().addAll(pic);
                imageCol++;

                // to check if all the 4 images of a row are completed
                if (imageCol > 3) {
                    // Reset Column
                    imageCol=0;
                    // Next Row
                    imageRow++;
                }		
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }	
    }

    @FXML
    void goToMovie (ActionEvent e) {

    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {
        if (Main.isEmployee())
            SceneCreator.launchScene("ManageFilmsScene.fxml", event);
        else
            SceneCreator.launchScene("UserScene.fxml", event);           
    }
}