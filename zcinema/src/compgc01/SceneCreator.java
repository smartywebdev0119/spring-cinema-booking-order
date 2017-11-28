package compgc01;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class that creates new scenes.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 18.11.2017
 */

public class SceneCreator {

    String sceneName;
	
	@FXML
	FXMLLoader loader;
	
	// launching the new scene based on the .fxml file name passed in the argument as a String variable
	public void launchScene (String sceneName, ActionEvent event) throws IOException{
	    this.sceneName = sceneName;
	    createNewScene(event);
	 }
	
	  // building the scene and setting the value for the instance variable loader
	  @FXML
	  public void createNewScene (ActionEvent event) throws IOException {
	      FXMLLoader loader = new FXMLLoader(getClass().getResource(this.sceneName));
	      Parent root = loader.load();
	      Scene scene = new Scene(root);
	      Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
	      stage.setScene(scene);
	      stage.show();
	      this.loader = loader;
	 }
	
	  // creating a controller to be called in the child class 'MainController' to customize the scene elements
	  public MainController getController(FXMLLoader loader){
	      MainController controller = (MainController)loader.getController();
	      return controller;
	  }
}