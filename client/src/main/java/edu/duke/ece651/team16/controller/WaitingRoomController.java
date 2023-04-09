package edu.duke.ece651.team16.controller;

import java.net.URL;
import java.util.Map;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class WaitingRoomController {
    private Client client;
    @FXML
    private AnchorPane waitingRoot;
    @FXML
    private Button nextWaiting;

    /**
     * Set the client for the controller.
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * This method is called when the user clicks on the "Next" button in the
     * UI.It receive the initial map. it then loads the FXML file for
     * the chooseGroup UI.
     * 
     * @param ae The ActionEvent object representing the button click event.
     * @throws Exception if there is any exception while performing socket
     *                   communication or loading the waiting room FXML file.
     */
    @FXML
    public void nextWaiting(ActionEvent ae) throws Exception {
        try {
            client.waitEveryoneDone(); // socket receive "stage Complete"
            Views view = new Views(System.out);

            String map = client.recvMsg();// receive initial map from server
            MapParser mp = new MapParser(map);

            URL xmlResource = getClass().getResource("/ui/ChooseGroup.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
            AnchorPane pane = fxmlLoader.load(); // Load the FXML file

            ChooseGroupController chooseGroup = fxmlLoader.getController();
            chooseGroup.setClient(client);// send client to next page
            chooseGroup.setMapParser(mp);// send map to next page

            waitingRoot.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
