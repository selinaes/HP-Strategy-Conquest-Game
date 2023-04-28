package edu.duke.ece651.team16.controller;

import java.io.IOException;
import java.net.URL;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.control.*;
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

    @FXML
    private ImageView green_C;

    @FXML
    private ImageView red_C;

    @FXML
    private ImageView blue_C;

    @FXML
    private ImageView yellow_C;

    @FXML
    public void initialize() {
        setImageViewProperties(green);
        setImageViewProperties(red);
        setImageViewProperties(blue);
        setImageViewProperties(yellow);
        green_C.setVisible(false);
        red_C.setVisible(false);
        blue_C.setVisible(false);
        yellow_C.setVisible(false);
        setImageViewProperties(green_C);
        setImageViewProperties(red_C);
        setImageViewProperties(blue_C);
        setImageViewProperties(yellow_C);
    }

    /**
     * Set the client for the controller.
     * 
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * Set the map parser for the controller.
     * 
     * @param mapParser
     */
    public void setMapParser(MapParser mapParser) {
        this.mapParser = mapParser;
    }

    /**
     * Set the image view properties for the color images.
     * 
     * @param imageView
     */
    private void setImageViewProperties(ImageView imageView) {

        imageView.setOnMouseEntered(event -> {
            if (imageView.getId().equals("green")) {
                green_C.setVisible(true);
            } else if (imageView.getId().equals("red")) {
                red_C.setVisible(true);
            } else if (imageView.getId().equals("blue")) {
                blue_C.setVisible(true);
            } else if (imageView.getId().equals("yellow")) {
                yellow_C.setVisible(true);
            }
        });
        imageView.setOnMouseExited(event -> {
            green_C.setVisible(false);
            red_C.setVisible(false);
            blue_C.setVisible(false);
            yellow_C.setVisible(false);
        });
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
                URL xmlResource = getClass().getResource("/ui/InitGamePlay.fxml");
                FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
                AnchorPane pane = fxmlLoader.load(); // Load the FXML file

                InitGamePlayController initgamePlayController = fxmlLoader.getController();
                initgamePlayController.setClient(client);
                initgamePlayController.setColorText(client.getColor());
                initgamePlayController.setRoomText(client.getRoom());
                mapParser.setPlayer(text);
                client.setColor(text);
                initgamePlayController.setinitMapParser(mapParser);
                initgamePlayController.initSpinner();
                // Display the game play screen
                chooseGroupRoot.getChildren().setAll(pane);

                // Show an alert to the user after the new page is shown
                Platform.runLater(() -> {
                    // AlertBox alert = new AlertBox();
                    // alert.showAlert("Please assign your initial units.",
                    // "Please assign your initial units. The sum of units assigned can not exceed
                    // 24.");

                    PopupBox popup = new PopupBox(chooseGroupRoot);
                    popup.displayText("Assign initial students.",
                            "Please assign your initial First-year students. The sum of students can not exceed 24.");
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
