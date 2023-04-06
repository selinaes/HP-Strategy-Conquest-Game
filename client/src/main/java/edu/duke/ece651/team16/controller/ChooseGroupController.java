package edu.duke.ece651.team16.controller;

import java.io.IOException;
import java.net.URL;

import edu.duke.ece651.team16.controller.Client;
import edu.duke.ece651.team16.controller.MapParser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

import javafx.application.Platform;
import static javafx.application.Platform.runLater;

public class ChooseGroupController {
    private Client client;
    private MapParser mapParser;

    @FXML
    private AnchorPane chooseGroupRoot;

    @FXML
    private Button startGame;

    @FXML
    private ImageView green;

    @FXML
    private ImageView red;

    @FXML
    private ImageView blue;

    @FXML
    private ImageView yellow;

    public void setClient(Client client) {
        this.client = client;
    }

    public void setMapParser(MapParser mapParser) {
        this.mapParser = mapParser;
    }

    /**
     * Handles the event of clicking on an image of a color during the color
     * choosing process. The method receives the color choice from the user,
     * validates it, and either proceeds to the game play screen or shows an alert
     * with an error message.
     * 
     * @param event The MouseEvent object associated with the image click event.
     * @throws Exception Throws an exception if there is an error loading the FXML
     *                   file for the game play screen.
     */
    @FXML
    private void onImageClicked(MouseEvent event) throws Exception, IOException {
        // Get the ImageView object that was clicked
        ImageView imageView = (ImageView) event.getSource();
        // get the color choice associated with it
        String text = (String) imageView.getUserData();
        System.out.println(text);
        // receive color choosing prompt from server
        String prompt = client.recvMsg();
        boolean valid = client.playerChooseColor(text);
        // Proceed to game play screen if the color choice is valid, or show an alert
        // with an error message if it is not
        if (valid) {
            try {
                URL xmlResource = getClass().getResource("/ui/GamePlay.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
                AnchorPane pane = fxmlLoader.load(); // Load the FXML file

                GamePlayController gamePlayController = fxmlLoader.getController();
                gamePlayController.setClient(client);
                mapParser.setPlayer(text);
                gamePlayController.setinitMapParser(mapParser);
                // Display the game play screen
                chooseGroupRoot.getChildren().setAll(pane);

                // Show an alert to the user after the new page is shown
                Platform.runLater(() -> {
                    AlertBox alert = new AlertBox();
                    alert.showAlert("Please assign your initial units.",
                            "Please assign your initial units. The sum of units assigned can not exceed 24.");
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {// Show an alert with an error message if the color choice is not valid
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Invalid Color.");
            alert.setHeaderText(null);
            alert.setContentText(prompt);
            alert.showAndWait();
        }
    }
}
