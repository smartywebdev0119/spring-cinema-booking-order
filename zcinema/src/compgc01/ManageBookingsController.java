package compgc01;

import java.io.*;
import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/**
 * The controller for the Bookings Scene.
 * 
 * @author Team 3: Filippos Zofakis and Lucio D'Alessandro
 * @since 04.12.2017
 */
public class ManageBookingsController {
    
    @FXML
    GridPane gridSeats;

    public boolean gridSeatsStartVisibility = true;
    static ArrayList<Integer> redFixedSeats = new ArrayList<Integer>(5);

    @FXML
    void initialize() throws IOException{
    }

    @FXML
    public void exitButton(MouseEvent event) {
        System.exit(0);
    }

    //////////////////////////////////////////////
    // getting the index of the seats!
    @FXML
    public void getSeatIndex(MouseEvent e) {

        try {
            Node target = (Node) e.getTarget();
            int colIndex = GridPane.getColumnIndex(target);
            int rowIndex = GridPane.getRowIndex(target);
            System.out.print(rowIndex + ",");
            System.out.print(colIndex + "\n");
        } catch (NullPointerException ex) {
            System.out.println("Please click on a seat!");
        }
    }

    @FXML
    public void turnSeatRed(MouseEvent e) {

        if(((Node) e.getSource()).getStyle().length() == 55){
            Alert alert = new Alert(AlertType.WARNING, "The seat " + ((Node) e.getSource()).getId()  + " is booked already!", ButtonType.OK);
            alert.showAndWait();
            if (alert.getResult() == ButtonType.OK) {
                alert.close();
            }
        }

        for (Node node : gridSeats.getChildren()) {
            node.setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        }

        if (((Node) e.getSource()).getStyle().length() == 55) {
            ((Node) e.getSource()).setStyle("-fx-fill:red; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        } else {
            ((Node) e.getSource()).setStyle("-fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        }

        //		 BOOKED SEATS COLORS CAN'T TURN BLACK!!
        //		 for(Integer id : redFixedSeats){
        //		 gridSeats.getChildren().get(id).setStyle("-fx-fill:#c2a9a9; -fx-font-family: 'Material Icons'; -fx-font-size: 40.0;");
        //		 }

        // Alert for booked seat

    }
    ///////////////////////////////////////////

    @FXML
    public void showBookingHistoryOnClick(ActionEvent event) throws IOException {

    		SceneCreator.launchScene("BookingHistoryScene.fxml");
    }

    @FXML
    public void backToPrevScene(ActionEvent event) throws IOException {

        SceneCreator.launchScene("UserScene.fxml");
    }

    protected void personaliseScene() throws IOException {
        
    }
}