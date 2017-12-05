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
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;

/**
 * The controller for the View Films Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 05.12.2017
 */
public class ViewFilmsController implements Initializable {

    ArrayList<File> fileList = new ArrayList<File>();
    HBox hb = new HBox();

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

            int rows = (fileList.size() / 4) + 1;
            int columns = 4;
            int numberOfLastRowItems = fileList.size() % 4;

            int imageIndex = 0;

            for (int i = 0 ; i < columns; i++) {
                for (int j = 0; j < rows; j++) {
                    if (imageIndex < fileList.size()) {
                        addImage(imageIndex, i, j);
                        imageIndex++;
                    }
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }}

    private void addImage(int index, int colIndex, int rowIndex) {

        String idToCut = fileList.get(index).getName();
        String id = idToCut.substring(0, (idToCut.length() - 4));
        // System.out.println(id);
        // System.out.println(fileList.get(i).getName());
        image = new Image(fileList.get(index).toURI().toString());
        pic = new ImageView();
        pic.setFitWidth(160);
        pic.setFitHeight(220);
        pic.setImage(image);
        pic.setId(id);
        hb.getChildren().add(pic);
        GridPane.setConstraints(pic, colIndex, rowIndex, 1, 1, HPos.CENTER, VPos.CENTER);
        // grid.add(pic, imageCol, imageRow);
        grid.getChildren().addAll(pic);

        pic.setOnMouseClicked(e -> {
            // System.out.printf("Mouse clicked cell [%d, %d]%n", rowIndex, colIndex);
            System.out.println("Film Title: " + id);
            try {
                SceneCreator.launchScene("ViewSelectedFilm.fxml");
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        if (Main.isEmployee())
            SceneCreator.launchScene("ManageFilmsScene.fxml");
        else
            SceneCreator.launchScene("UserScene.fxml");           
    }

    @FXML
    public String getId () {

        return id;
    }
}