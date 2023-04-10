package edu.duke.ece651.team16.controller;

import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import java.net.Socket;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.PrintStream;

import javafx.application.Platform;
import static javafx.application.Platform.runLater;

public class StartGameController {
    @FXML
    private AnchorPane mainRoot;
    @FXML
    private Button newGame;
    @FXML
    private Button joinGame;
    @FXML
    private Button exitGame;

    private Client client;

    /**
     * 
     * This method is called when the user clicks on the "New Game" button in the
     * main menu. It establishes a connection to the server, sets up a client
     * object,and loads the CreateRoom.fxml file using JavaFX's FXMLLoader.
     * 
     * @param ae The action event triggered by clicking on the "New Game" button.
     * @throws Exception   if an exception occurs during the socket creation,
     *                     loading the FXML file, or setting up the client object.
     * @throws IOException if an I/O error occurs during the creation of input and
     *                     output streams for the socket.
     */
    @FXML
    public void newGame(ActionEvent ae) throws Exception, IOException {
        URL xmlResource = getClass().getResource("/ui/JoinGame.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
        AnchorPane pane = fxmlLoader.load(); // Load the FXML file
        JoinRoomController joinRoomController = fxmlLoader.getController();
        joinRoomController.setClient(client);
        mainRoot.getChildren().setAll(pane);
    }

    /**
     * 
     * This method is called when the user clicks on the "Exit Game" button in the
     * main menu. It exits the game by calling the System.exit() method
     * 
     * @param ae The action event triggered by clicking on the "Exit Game" button.
     */
    @FXML
    public void exitGame(ActionEvent ae) {
        System.exit(0);
    }

    /**
     * Set the client for the controller.
     * 
     * @param c
     */
    public void setClient(Client c) {
        this.client = c;
    }
}
