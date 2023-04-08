package edu.duke.ece651.team16.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.event.ActionEvent;

public class SettingsPaneController {
  @FXML
  private Button settingsButton;
    @FXML
    private Button exitButton;

    @FXML
    private Button toggleMusicButton;

    @FXML
    private Slider volumeSlider;

    @FXML
    private VBox settingsBox;

    private boolean vBoxVisible = true;

    @FXML
    private void onHideButton(ActionEvent event) {
      settingsBox.setVisible(vBoxVisible);
      vBoxVisible = !vBoxVisible;
        // Implement settings button functionality
    }

    @FXML
    private void handleExit() {
        // Implement exit button functionality
    }

    @FXML
    private void handleMusicToggle() {
        // Implement music toggle button functionality
    }

    @FXML
    private void handleVolumeChange() {
        // Implement volume slider functionality
    }
}
