package compgc01;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Class that creates new scenes.
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 02.12.2017
 */
public class SceneCreator {

    // launching the new scene based on the .fxml file name passed in the argument as a String variable
    // building the scene and setting the value for the instance variable loader
    public static void launchScene (String sceneName, ActionEvent actionEvent) throws IOException {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource(sceneName));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage stage = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
        stage.setScene(scene);
        stage.show();
    }
}