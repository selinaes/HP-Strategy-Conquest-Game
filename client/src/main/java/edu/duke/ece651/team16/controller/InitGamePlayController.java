package edu.duke.ece651.team16.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import java.net.URL;
import java.net.Socket;
import java.util.HashMap;
import java.util.Arrays;

public class InitGamePlayController {
    @FXML
    private AnchorPane territoryRoot;
    @FXML
    private GridPane assignUnits_GridPane;
    @FXML
    private Button assignUnits;
    @FXML
    private ImageView mapImage;
    @FXML
    private ImageView blackBackground;

    private Client client;
    private MapParser mapParser;
    private double zoomFactor = 1.0;
    private AlertBox alert = new AlertBox();

    private LinkedHashMap<String, String> myTerritory;
    private int maxUnits = 24;

    private ArrayList<Integer> currTerritoryUnits = new ArrayList<>();
    private GamePlayDisplay gamePlayDisplay = new GamePlayDisplay();

    @FXML
    public void initialize() {
        blackBackground.setVisible(true);
        assignUnits.setOnAction(event -> {
            try {
                onInitAssignUnits(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        // enable user to zoom or drag map
        mapImage.setOnScroll(this::onZoom);
        mapImage.setOnMouseDragged(this::onDrag);
    }

    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * set initial map, add my terrirtory to "myTerritory"
     * 
     * @param mapParser
     */
    public void setinitMapParser(MapParser mapParser) throws IOException {
        this.mapParser = mapParser;
        List<String> territory = mapParser.getMyInitTerritory();
        updateAssignUnitsGridRow(territory);
        myTerritory = new LinkedHashMap<String, String>();
        for (String t : territory) {
            System.out.println("My territory: " + t);
            myTerritory.put(t, "0");
        }
    }

    private void setMapParser() {
        List<String> territory = mapParser.getMyTerritory();
        myTerritory = new LinkedHashMap<String, String>();
        for (String t : territory) {
            myTerritory.put(t, "0");
        }
    }

    /**
     * assign units at first
     * 
     * @param event
     */
    private void onInitAssignUnits(ActionEvent event) throws Exception {
        int sum = getSpinnerSum();
        System.out.println("onInitAssignUnits, sum is " + sum + ", maxunits is " + maxUnits);
        if (sum > maxUnits) {
            alert.showAlert("Invalid input", "Total units assigned exceeds the maximum allowed");
        } else {
            assignUnits();
            PopupBox popup = new PopupBox(territoryRoot);
            popup.display("/img/texts/wait.png");
            // alert.displayImageAlert("Done!", "/img/texts/wait.png");
            try {
                client.waitEveryoneDone();
                mapParser.setMap(client.recvMsg());
                client.recvMsg(); // receive "Game continous or Winner"
                client.recvMsg(); // receive "watching" "Choose watch" "do nothing"
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            URL xmlResource = getClass().getResource("/ui/GamePlay.fxml");
            FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
            AnchorPane pane = fxmlLoader.load(); // Load the FXML file

            GamePlayController gamePlayController = fxmlLoader.getController();
            gamePlayController.setClient(client);
            gamePlayController.setMapParser(mapParser);
            gamePlayController.setMyTerritory(myTerritory);
            // Display the game play screen
            territoryRoot.getChildren().setAll(pane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onZoom(ScrollEvent event) {
        zoomFactor *= event.getDeltaY() > 0 ? 1.1 : 1 / 1.1;
        zoomFactor = Math.min(Math.max(zoomFactor, 1.0), 2.0);
        mapImage.setScaleX(zoomFactor);
        mapImage.setScaleY(zoomFactor);
        event.consume();
    }

    private void onDrag(MouseEvent event) {
        if (zoomFactor >= 1.0) {
            double deltaX = event.getX() - mapImage.getFitWidth() / 2.0;
            double deltaY = event.getY() - mapImage.getFitHeight() / 2.0;
            mapImage.setTranslateX(mapImage.getTranslateX() + deltaX);
            mapImage.setTranslateY(mapImage.getTranslateY() + deltaY);
        }
    }

    /**
     * update AssignUnits GridRow for different number of players with
     * different number of territories
     * 
     * @param territory
     */
    private void updateAssignUnitsGridRow(List<String> territory) {
        int numTerritories = territory.size();
        // Check if number of rows in gridpane is less than number of territories
        if (assignUnits_GridPane.getRowConstraints().size() < numTerritories) {
            // Add required number of rows to gridpane
            for (int i = 0; i < numTerritories; i++) {
                assignUnits_GridPane.getRowConstraints().add(i, new RowConstraints(10.0, 30.0, 10.0));
            }
        }
        for (int i = 0; i < numTerritories; i++) {
            // Set the row index for each node
            Spinner spinner = new Spinner();
            spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, maxUnits, 0));
            assignUnits_GridPane.add(spinner, 1, i);
            TextField textField = new TextField();
            textField.setText(territory.get(i)); // Set text of textfield
            assignUnits_GridPane.add(textField, 0, i);
        }
        // Update the row index of the commit button
        Button commitButton = (Button) assignUnits_GridPane.lookup("#assignUnits");
        GridPane.setRowIndex(commitButton, numTerritories);

        // assignUnits_GridPane.autosize();
        assignUnits_GridPane.setHgap(10);
        assignUnits_GridPane.setVgap(18);
    }

    private int getSpinnerSum() {
        int sum = 0;
        for (Node node : assignUnits_GridPane.getChildren()) {
            // Check if the child is a Spinner
            if (node instanceof Spinner) {
                Spinner spinner = (Spinner) node;
                // Get the current value of the spinner
                if (spinner.getValue() != null) {
                    int value = (int) spinner.getValue();
                    sum += value;
                }
            }
        }
        return sum;
    }

    private void assignUnits() {
        ArrayList<String> unit = new ArrayList<>();
        for (Node node : assignUnits_GridPane.getChildren()) {
            // Check if the child is a Spinner
            if (node instanceof Spinner) {
                Spinner spinner = (Spinner) node;
                // Get the current value of the spinner
                if (spinner.getValue() != null) {
                    int value = (int) spinner.getValue();
                    unit.add(String.valueOf(value));
                }
            }
        }
        int i = 0;
        try {
            for (String key : myTerritory.keySet()) {
                myTerritory.put(key, unit.get(i));
                // server set that we can not input 0 as unit number
                if (!unit.get(i).equals("0")) {
                    client.playerAssignUnit(key);
                    client.playerAssignUnit(unit.get(i));
                    System.out.println(
                            "Assign " + unit.get(i) + " in " + key);
                }
                i++;
            }
            client.playerAssignUnit("done");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
