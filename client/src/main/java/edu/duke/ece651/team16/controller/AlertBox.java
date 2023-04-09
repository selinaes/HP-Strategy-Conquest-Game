package edu.duke.ece651.team16.controller;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class AlertBox {
    /*
     * displays image alert
     * 
     * @param title title of the alert
     * 
     * @param imageUrl url of the image
     */
    public void displayImageAlert(String title, String imageUrl) {
        Stage imageWindow = new Stage();
        imageWindow.initModality(Modality.APPLICATION_MODAL);
        imageWindow.setTitle(title);
        Image image = new Image(imageUrl);
        ImageView imageView = new ImageView(image);
        Button closeButton = new Button("Close");
        closeButton.setOnAction(e -> imageWindow.close());
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(imageView, closeButton);
        Scene scene = new Scene(layout);
        imageWindow.setScene(scene);
        imageWindow.showAndWait();
    }

    /*
     * displays text alert
     * 
0     * @param title title of the alert
     * 
     * @param message message of the alert
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
