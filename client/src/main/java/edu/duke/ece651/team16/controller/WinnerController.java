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

public class WinnerController {
    @FXML
    private AnchorPane winnerRoot;
    @FXML
    private Button exit;



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
    public void onExit(ActionEvent ae) throws Exception {
        // Socket clientSocket = client.getClientSocket();
        // clientSocket.close();
        URL xmlResource = getClass().getResource("/ui/login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
        AnchorPane pane = fxmlLoader.load(); // Load the FXML file
        winnerRoot.getChildren().setAll(pane);
    }

}
