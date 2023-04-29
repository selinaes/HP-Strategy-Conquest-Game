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
import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.application.Platform;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import edu.duke.ece651.team16.controller.MapParser;

import java.net.URL;
import java.net.Socket;
import java.util.HashMap;
import java.util.Arrays;

public class InitGamePlayController {
    @FXML
    private AnchorPane territoryRoot;
    @FXML
    private AnchorPane HPmap;
    @FXML
    private Button assignUnits;
    @FXML
    private ImageView mapImage;
    @FXML
    private ImageView blackBackground;
    @FXML
    public Label color;
    @FXML
    public Label room;

    private Client client;
    private MapParser mapParser;
    private AlertBox alert = new AlertBox();

    private LinkedHashMap<String, String> myTerritory;
    private int maxUnits = 24;

    private ArrayList<Integer> currTerritoryUnits = new ArrayList<>();
    private GamePlayDisplay gamePlayDisplay = new GamePlayDisplay();

    @FXML
    public void initialize() {
        assignUnits.setOnAction(event -> {
            try {
                onInitAssignUnits(event);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        Platform.runLater(() -> {
            for (Node node : HPmap.getChildren()) {
                if (node instanceof HBox) {
                    HBox hbox = (HBox) node;
                    for (Node node2 : hbox.getChildren()) {
                        if (node2 instanceof Button) {
                            Button btn = (Button) node2;
                            String currentTer = btn.getText();
                            String currentColor = mapParser.getTerritoryInfo(currentTer).get("Player");
                            String style = "button-" + currentColor;
                            btn.getStyleClass().remove("button-territory");
                            btn.getStyleClass().add(style);
                        }
                    }
                }
            }
        });
    }

    public void setColorText(String which) {
        color.setText("Color: " + which);
    }

    public void setRoomText(String number) {
        room.setText("Room: " + number);
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
        // updateAssignUnitsGridRow(territory);
        myTerritory = new LinkedHashMap<String, String>();
        for (String t : territory) {
            System.out.println("My territory: " + t);
            myTerritory.put(t, "0");
        }
    }

    public void setMapParser() {
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
            PopupBox popup = new PopupBox(territoryRoot);
            popup.display("/img/texts/wait.png");
            assignUnits();
            // alert.displayImageAlert("Done!", "/img/texts/wait.png");
            try {
                client.waitEveryoneDone();
                String msg = client.recvMsg();
                System.out.println("msg is " + msg);
                mapParser.setMap(msg);
                client.recvMsg(); // receive "Game continous or Winner"
                client.recvMsg(); // receive "watching" "Choose watch" "do nothing"
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            FXMLLoader loaderStart = new FXMLLoader(getClass().getResource("/ui/ChatRoom.fxml"));
            loaderStart.setControllerFactory(c -> {
                return new ChatRoomController(client.getColor());
            });
            AnchorPane chatRoomPane = loaderStart.load();
            ChatRoomController chatRoomController = loaderStart.getController();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/GamePlay.fxml"));
            AnchorPane gamePlayPane = fxmlLoader.load();
            GamePlayController gamePlayController = fxmlLoader.getController();
            gamePlayController.setClient(client);
            gamePlayController.setColorText(client.getColor());
            gamePlayController.setRoomText(client.getRoom());
            gamePlayController.setMapParser(mapParser);
            gamePlayController.setMyTerritory(myTerritory);
            gamePlayController.setChatRoomController(loaderStart.getController());
            chatRoomController.setGamePlayController(gamePlayController);
            SplitPane splitPane = new SplitPane(chatRoomPane, gamePlayPane);
            splitPane.setDividerPositions(0.2); // set the initial position of the divider

            territoryRoot.getChildren().setAll(splitPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * according myTerritory, make all spinner of HBox in the enemy territory
     * invisible
     */
    public void initSpinner() {
        for (Node node : HPmap.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                for (Node node2 : hbox.getChildren()) {
                    Node btn = hbox.getChildren().get(0);
                    Node spinner = hbox.getChildren().get(1);
                    String currentTer = ((Button) btn).getText();
                    if (!myTerritory.containsKey(currentTer)) {
                        spinner.setVisible(false);
                    } else {
                        // set null to be 0
                        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(
                                0, maxUnits, 0);

                        ((Spinner<Integer>) spinner).setValueFactory(valueFactory);
                    }
                }
            }
        }
    }

    // /**
    // * update AssignUnits GridRow for different number of players with
    // * different number of territories
    // *
    // * @param territory
    // */
    // private void updateAssignUnitsGridRow(List<String> territory) {
    // int numTerritories = territory.size();
    // // Check if number of rows in gridpane is less than number of territories
    // if (assignUnits_GridPane.getRowConstraints().size() < numTerritories) {
    // // Add required number of rows to gridpane
    // for (int i = 0; i < numTerritories; i++) {
    // assignUnits_GridPane.getRowConstraints().add(i, new RowConstraints(10.0,
    // 30.0, 10.0));
    // }
    // }
    // for (int i = 0; i < numTerritories; i++) {
    // // Set the row index for each node
    // Spinner spinner = new Spinner();
    // spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0,
    // maxUnits, 0));
    // assignUnits_GridPane.add(spinner, 1, i);
    // TextField textField = new TextField();
    // textField.setText(territory.get(i)); // Set text of textfield
    // assignUnits_GridPane.add(textField, 0, i);
    // }
    // // Update the row index of the commit button
    // Button commitButton = (Button) assignUnits_GridPane.lookup("#assignUnits");
    // GridPane.setRowIndex(commitButton, numTerritories);

    // // assignUnits_GridPane.autosize();
    // assignUnits_GridPane.setHgap(10);
    // assignUnits_GridPane.setVgap(18);
    // }

    private int getSpinnerSum() {
        int sum = 0;
        for (Node node : HPmap.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                for (Node node2 : hbox.getChildren()) {
                    if (node2 instanceof Spinner) {
                        Spinner spinner = (Spinner) node2;
                        if (spinner.getValue() != null) {
                            int value = (int) spinner.getValue();
                            sum += value;
                        }
                    }
                }
            }
        }
        return sum;
    }

    private void assignUnits() {
        for (Node node : HPmap.getChildren()) {
            if (node instanceof HBox) {
                HBox hbox = (HBox) node;
                Button btn = (Button) hbox.getChildren().get(0);
                Spinner spinner = (Spinner) hbox.getChildren().get(1);
                // add the unit number to myTerritory
                if (spinner.getValue() != null) {
                    myTerritory.put(btn.getText(), String.valueOf(spinner.getValue()));
                    System.out.println("put " + String.valueOf(spinner.getValue()) + " in myterritory");
                }
            }
        }

        try {
            for (String key : myTerritory.keySet()) {
                // server set that we can not input 0 as unit number
                if (!myTerritory.get(key).equals("0")) {
                    client.playerAssignUnit(key);
                    client.playerAssignUnit(myTerritory.get(key));
                    System.out.println(
                            "Assign " + myTerritory.get(key) + " in " + key);
                }
            }
            client.playerAssignUnit("done");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
