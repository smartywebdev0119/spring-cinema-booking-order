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
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

/**
 * The controller for the View Films Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 05.12.2017
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
    @FXML
    String id;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        // System.out.println(id);

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

                // creating id names for the pictures
                String idToCut = fileList.get(i).getName();
                String id = idToCut.substring(0, (idToCut.length() - 4));
                System.out.println(id);
                // System.out.println(fileList.get(i).getName());
                image = new Image(fileList.get(i).toURI().toString());
                pic = new ImageView();
                pic.setFitWidth(160);
                pic.setFitHeight(220);
                pic.setImage(image);
                pic.setId(id);
                hb.getChildren().add(pic);
                GridPane.setConstraints(pic, imageCol, imageRow, 1, 1, HPos.CENTER, VPos.CENTER);
                // grid.add(pic, imageCol, imageRow);
                grid.getChildren().addAll(pic);

                imageCol++;
                // checking if all 4 images of a row have been filled in
                if (imageCol > 3) {
                    // reset Column
                    imageCol=0;
                    // next Row
                    imageRow++;
                }		
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }

        // getting id of clicked image (when page scrolls the id's are messed up... only row 0 - 1
        grid.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
            
            for( Node node: grid.getChildren()) {
                if( node instanceof ImageView) {
                    if( node.getBoundsInParent().contains(e.getSceneX(),  e.getSceneY())) {
                        id = node.getId();
                        System.out.println( "Node: " + node + " at " + GridPane.getRowIndex( node) + "/" + GridPane.getColumnIndex( node));
                    } 
                }
            }
            System.out.println(id);
        });
    }

    //    @FXML
    //    public void backToPrevScene(ActionEvent event) throws IOException {
    //        
    //        if (Main.isEmployee())
    //            SceneCreator.launchScene("ManageFilmsScene.fxml", event);
    //        else
    //            SceneCreator.launchScene("UserScene.fxml", event);           
    //    }

    // using this to test the ViewSelectedFilm page
    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("ViewSelectedFilm.fxml", event);
    }

    @FXML
    public String getId () {
        
        return id;
    }
}