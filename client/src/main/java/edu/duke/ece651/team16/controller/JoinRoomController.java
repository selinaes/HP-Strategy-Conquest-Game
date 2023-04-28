package edu.duke.ece651.team16.controller;

import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import java.util.ResourceBundle;

import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class JoinRoomController {
    @FXML
    private AnchorPane mainRoot;
    @FXML
    private TextField roomIDField;
    @FXML
    private Button joinRoom;
    @FXML
    private Button searchRoom;
    @FXML
    private ChoiceBox<Integer> chooseNum;
    @FXML
    private Label prompt;

    private Client client;
    private boolean isGuest;
    private AlertBox alert = new AlertBox();

    public void setClient(Client c) {
        this.client = c;
    }

    @FXML
    public void initialize() throws Exception {
        chooseNum.getItems().addAll(2, 3, 4);
        chooseNum.setVisible(false);
        prompt.setVisible(false);
        joinRoom.setDisable(true);
        isGuest = true;
    }

    /**
     * Function to search room for the p
     * 
     * @param ae
     * @throws Exception
     */
    @FXML
    public void searchRoom(ActionEvent ae) throws Exception {
        String roomID = roomIDField.getText();
        System.out.println("Room ID: " + roomID);
        String res = client.playerChooseRoom(roomID);
        if (res.equals("Room created.")) {
            isGuest = false;
            chooseNum.setVisible(true);
            prompt.setVisible(true);
            joinRoom.setDisable(false);
            searchRoom.setDisable(true);
            client.setRoomID(roomID);
        } else if (res.equals("Room joined.")) {
            joinRoom.setDisable(false);
            searchRoom.setDisable(true);
            client.setRoomID(roomID);
        } else {// "Room exceeded player number, game already started"
            // alert
            alert.displayImageAlert("Fail to join room", "/img/texts/capacity.png");
        }
    }

    /**
     * This method is called when the user clicks on the "join Room" button in the
     * UI.It gets the entered Room ID, password, it then loads the FXML file for the
     * waiting room UI.
     * 
     * @param ae The ActionEvent object representing the button click event.
     * @throws Exception if there is any exception while performing socket
     *                   communication or loading the waiting room FXML file.
     */
    @FXML
    public void joinRoom(ActionEvent ae) throws Exception {
        try {
            if (!isGuest) {
                if (chooseNum.getValue() == null) { // show alert "Please choose player number"
                    alert.displayImageAlert("Set player number", "/img/texts/firstPlayer.png");
                    return;
                } else { // first player already set the playerNum
                    client.playerChooseNum(String.valueOf(chooseNum.getValue()));
                }
            } else {
                client.playerChooseNum(null);
            }
            // go to waiting room
            URL xmlResource = getClass().getResource("/ui/WaitingRoom.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
            AnchorPane pane = fxmlLoader.load(); // Load the FXML file
            WaitingRoomController waitingRoomController = fxmlLoader.getController();
            waitingRoomController.setClient(client);
            mainRoot.getChildren().setAll(pane);

        } catch (

        Exception e) {
            e.printStackTrace();
        }
    }
}
