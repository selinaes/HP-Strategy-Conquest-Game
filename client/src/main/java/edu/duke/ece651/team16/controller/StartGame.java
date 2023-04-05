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

public class StartGame {
    @FXML
    private AnchorPane mainRoot;
    @FXML
    private Button newGame;
    @FXML
    private Button joinGame;
    @FXML
    private Button exitGame;

    /**
     * 
     * This method is called when the user clicks on the "New Game" button in the
     * main menu. It establishes a connection to the server, sets up a client
     * object,and loads the CreateRoom.fxml file using JavaFX's FXMLLoader. It then
     * sets the loaded AnchorPane as the root node of the main scene and sets the
     * client object to the corresponding controller instance.
     * 
     * @param ae The action event triggered by clicking on the "New Game" button.
     * @throws Exception   if an exception occurs during the socket creation,
     *                     loading the FXML file, or setting up the client object.
     * @throws IOException if an I/O error occurs during the creation of input and
     *                     output streams for the socket.
     */
    @FXML
    public void newGame(ActionEvent ae) throws Exception, IOException {
        try {
            int port = 1651;
            String ip = "127.0.0.1";
            Socket clientSocket = null;
            clientSocket = new Socket(ip, port);
            PrintStream out = System.out;
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader socketReceive = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter socketSend = new PrintWriter(clientSocket.getOutputStream(),
                    true);
            Client client = new Client(inputReader, out, socketReceive, socketSend);
            client.setClientSocket(clientSocket);
            if (client.getClientSocket() == null) {
                System.out.println("clientSocket is null");
            } else {
                System.out.println("clientSocket is not null");
            }
            // URL xmlResource = getClass().getResource("/ui/CreateRoom.fxml");
            // FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new
            // FXMLLoader
            // AnchorPane pane = fxmlLoader.load(); // Load the FXML file

            // CreateRoomController createRoomController = fxmlLoader.getController();
            // createRoomController.setClient(client);

            // mainRoot.getChildren().setAll(pane);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called when the user clicks on the "Join Game" button in the
     * main menu.It loads the JoinGame.fxml file using JavaFX's FXMLLoader and sets
     * the loaded AnchorPane as the root node of the main scene.
     * 
     * @param ae The action event triggered by clicking on the "Join Game" button.
     */
    @FXML
    public void joinGame(ActionEvent ae) {
        try {
            URL xmlResource = getClass().getResource("/ui/JoinGame.fxml");
            AnchorPane pane = FXMLLoader.load(xmlResource);
            mainRoot.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
