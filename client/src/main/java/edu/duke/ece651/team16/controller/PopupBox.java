package edu.duke.ece651.team16.controller;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Popup;
import javafx.stage.PopupWindow.AnchorLocation;
// import edu.duke.ece651.team16.javafx.JavafxApplication;

public class PopupBox {
    AnchorPane anchorPane;

    public PopupBox(AnchorPane root) {
        this.anchorPane = root;
    }

    public void display(String imageUrl) {
        Image image = new Image(imageUrl);
        ImageView imageView = new ImageView(image);

        // Label message = new Label(title);

        VBox popupContent = new VBox(imageView);
        popupContent.setAlignment(Pos.CENTER);

        Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.setAnchorLocation(AnchorLocation.WINDOW_BOTTOM_LEFT);
        popup.getContent().add(popupContent);

        popup.show(anchorPane.getScene().getWindow());
        // popup.show(JavafxApplication.getPrimaryStage());

    }

    public void displayText(String title, String message) {
        Label titleLabel = new Label(title);
        titleLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 18px; -fx-text-alignment: left");

        Label messageLabel = new Label(message);
        messageLabel.setWrapText(true);
        messageLabel.setMaxWidth(400);
        messageLabel.setStyle("-fx-font-size: 16px;");

        VBox popupContent = new VBox(titleLabel, messageLabel);
        popupContent.setStyle("-fx-background-color: #aba7a7; -fx-opacity: 0.9; -fx-padding: 20px;");

        Popup popup = new Popup();
        popup.setAutoHide(true);
        popup.setAnchorLocation(AnchorLocation.WINDOW_TOP_RIGHT);
        popup.getContent().add(popupContent);

        popup.show(anchorPane.getScene().getWindow());
    }

}