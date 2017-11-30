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

    FXMLLoader loader;
    
    protected String sceneName;

    // launching the new scene based on the .fxml file name passed in the argument as a String variable
    // building the scene and setting the value for the instance variable loader
    public void launchScene (String sceneName, ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(sceneName));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        this.sceneName = sceneName;
        this.loader = loader;
        stage.show();
    }

    // creating a controller to be called in the child class 'MainController' to customize the scene elements
    public MainController getController(FXMLLoader loader){
        MainController controller = (MainController)loader.getController();
        return controller;
    }
}