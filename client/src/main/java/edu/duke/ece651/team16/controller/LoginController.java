package edu.duke.ece651.team16.controller;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import java.net.Socket;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class LoginController {
    @FXML
    private AnchorPane mainRoot;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private Button login;

    private Client client;

    @FXML
    public void initialize() throws Exception {
        createClient();
    }

    @FXML
    public void onLoginButton(ActionEvent ae) throws Exception {
        String user = username.getText();
        String pass = password.getText();
        if (user.isEmpty() || pass.isEmpty()) {
            AlertBox alert = new AlertBox();
            alert.showAlert("Error", "Please enter both username and password.");
        } else {
            // Perform login with username and password

            // login
            String msg = client.recvMsg(); // show alert
            System.out.println(msg);
            URL xmlResource = getClass().getResource("/ui/StartGame.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
            AnchorPane pane = fxmlLoader.load(); // Load the FXML file
            StartGameController startGameController = fxmlLoader.getController();
            startGameController.setClient(client);

            mainRoot.getChildren().setAll(pane);

            Platform.runLater(() -> {
                AlertBox alert = new AlertBox();
                alert.displayImageAlert("Welcome!", "/img/texts/welcome.png");// welcome
            });
        }
    }

    /**
     * make a client
     * 
     * @return client
     */
    private void createClient() throws Exception {
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
            client = new Client(inputReader, out, socketReceive, socketSend);
            client.setClientSocket(clientSocket);// add socket to client
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
