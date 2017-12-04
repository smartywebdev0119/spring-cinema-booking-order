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
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class SeeMoviesController implements Initializable {

    @FXML
    ScrollPane scrollPane;
    @FXML
    GridPane grid;
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
		File file = new File(path);
		// pushing single path files in the array filelist1
		File[] filelist1 = file.listFiles();
		ArrayList<File> filelist2 = new ArrayList<>();
		HBox hb = new HBox();
		
		for (int i = 0; i < filelist1.length; i++) {
			System.out.println(filelist1[i].toString());

			// there's some weird ass file in the folder that is not a picture but I cannot delete.. the path is /Users/Lucio/Desktop/cinema_booking_app-master/res/images/filmImages/.DS_Store
			if(filelist1[i].toString().equals("/Users/Lucio/Desktop/cinema_booking_app-ma"
					+ "ster/res/images/filmImages/defaultFilmPoster.png") || filelist1[i].toString().equals("/Users/Lucio/Desktop/cinema_booking_app-master/res/images/filmImages/.DS_Store")){
				continue;
			}
			filelist2.add(filelist1[i]);

		}
		
		// GRIDPANE SETTINGS
		// setting padding grid
		grid.setPadding(new Insets(10,10,10,10));
		// setting pane interior padding
		grid.setHgap(10);
		grid.setVgap(10);
//		grid.setGridLinesVisible(true);

		int imageCol = 0;
		int imageRow = 0;

		for (int i = 0; i < filelist2.size(); i++) {
		    System.out.println(filelist2.get(i).getName());
		    image = new Image(filelist2.get(i).toURI().toString());
		    pic = new ImageView();
		    pic.setFitWidth(160);
		    pic.setFitHeight(220);
		    pic.setImage(image);
		    hb.getChildren().add(pic);
			GridPane.setConstraints(pic, imageCol, imageRow, 1, 1, HPos.CENTER, VPos.CENTER);
//		    grid.add(pic, imageCol, imageRow);
			grid.getChildren().addAll(pic);
		    imageCol++;

		    // To check if all the 4 images of a row are completed
		    if(imageCol > 3){
		      // Reset Column
		      imageCol=0;
		      // Next Row
		      imageRow++;
		    }		
		}
	}
    
    catch(Exception e){
    	System.out.println(e.getMessage());
    }	
}
	
    @FXML
	void GoToMovie (ActionEvent e) {
		
	}
    
    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {
        SceneCreator.launchScene("ManageFilmsScene.fxml", event);
    }
}
