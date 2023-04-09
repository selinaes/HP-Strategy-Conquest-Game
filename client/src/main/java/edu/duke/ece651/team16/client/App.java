/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.team16.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.net.URL;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
// import javafx.util.Duration;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Slider;
import javafx.geometry.Insets;
import javafx.stage.Modality;

public class App extends Application {
  public static MediaPlayer mediaPlayer;

  @Override
  public void start(Stage stage) {
    try {
      addMusic();
      URL xmlResource = getClass().getResource("/ui/login.fxml");
      AnchorPane gp = FXMLLoader.load(xmlResource);

      CustomTab presetTab = new CustomTab(1, gp);
      TabPane tabPane = new TabPane();
      tabPane.getTabs().add(presetTab);

      Scene scene = new Scene(tabPane);
      stage.setScene(scene);
      stage.show();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Add music to the game.
   */
  public void addMusic() {
    Media sound = new Media(getClass().getResource("/audio/Adventure.mp3").toString());
    mediaPlayer = new MediaPlayer(sound);
    mediaPlayer.setAutoPlay(true);
    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
    mediaPlayer.setStartTime(javafx.util.Duration.seconds(0));
    mediaPlayer.setStopTime(javafx.util.Duration.seconds(84));
    mediaPlayer.play();
  }

  /**
   * Launch the application.
   * 
   * @param args
   */
  public static void main(String[] args) {
    launch(args);
  }

  /**
   * Custom tab class.
   */
  private static class CustomTab extends Tab {
    private int tabNumber;
    private static int count = 1;

    /**
     * Constructor for the custom tab.
     * 
     * @param tabNumber
     * @param content
     */
    public CustomTab(int tabNumber, AnchorPane content) {
      super();
      this.tabNumber = tabNumber;
      this.setContent(content);
      Label label = new Label("Risk Game " + tabNumber);
      Button showSettings = new Button("o");
      showSettings.setOnAction(e -> {
        createDialog();
      });
      if (tabNumber == 1) {
        HBox hbox = new HBox(showSettings, label);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        this.setGraphic(hbox);
      } else {
        HBox hbox = new HBox(label);
        hbox.setAlignment(Pos.CENTER_RIGHT);
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10, 10, 10, 10));
        this.setGraphic(hbox);
      }
    }

    /**
     * Create the dialog for the settings.
     */
    private void createDialog() {
      Stage dialog = new Stage();
      dialog.initModality(Modality.APPLICATION_MODAL);
      // dialog.initOwner(this.getScene().getWindow());
      dialog.setTitle("Settings");

      Button addPage = new Button("Add Tab");
      Button turnOff = new Button("Turn off music");
      Button turnOn = new Button("Turn on music");
      Slider volumeSlider = new Slider();
      volumeSlider.setValue(50);

      addPage.setOnAction(e -> {
        try {
          URL xmlResource = getClass().getResource("/ui/login.fxml");
          AnchorPane gp = FXMLLoader.load(xmlResource);
          CustomTab newTab = new CustomTab(++count, gp);
          ((TabPane) this.getTabPane()).getTabs().add(newTab);
          ((TabPane) this.getTabPane()).getSelectionModel().select(newTab);
        } catch (IOException ex) {
          ex.printStackTrace();
        }
      });

      turnOff.setOnAction(e -> {
        mediaPlayer.pause();
        turnOff.setDisable(true);
        turnOn.setDisable(false);
      });

      turnOn.setOnAction(e -> {
        mediaPlayer.play();
        turnOn.setDisable(true);
        turnOff.setDisable(false);
      });

      volumeSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
        if (mediaPlayer != null) {
          mediaPlayer.setVolume(volumeSlider.getValue() / 100);
        }
      });

      VBox dialogLayout = new VBox(addPage, turnOff, turnOn, volumeSlider);
      dialogLayout.setSpacing(10);
      dialogLayout.setAlignment(Pos.BOTTOM_RIGHT);
      dialogLayout.setPadding(new Insets(10, 10, 10, 10));
      Scene dialogScene = new Scene(dialogLayout, 150, 150);
      dialog.setScene(dialogScene);
      dialog.show();
    }
  }

}