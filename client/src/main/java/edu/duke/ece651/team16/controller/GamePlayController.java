package edu.duke.ece651.team16.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.input.ScrollEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ToolBar;
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

public class GamePlayController {
    @FXML
    private AnchorPane territoryRoot;
    @FXML
    private ToolBar toolBar;
    @FXML
    private Button rule;
    @FXML
    private Button units;
    @FXML
    private Button move;
    @FXML
    private Button attack;
    @FXML
    private Button research;
    @FXML
    private Button upgrade;

    @FXML
    private TextArea textArea;

    @FXML
    private TextArea history;

    @FXML
    private GridPane assignUnits_GridPane;

    @FXML
    private Button assignUnits;

    @FXML
    private ImageView mapImage;

    @FXML
    private ImageView blackBackground;

    @FXML
    private GridPane territroyGrid;

    @FXML
    private Button finish;

    @FXML
    private ImageView battleTime;

    private Client client;
    private MapParser mapParser;
    private double zoomFactor = 1.0;
    private Status playerStatus = Status.DEFAULT;
    private ArrayList<String> myOrder;
    private String oneOrderContent = "";
    private AlertBox alert = new AlertBox();

    private LinkedHashMap<String, String> myTerritory;
    private int maxUnits = 50;

    // private int myUnits = 0;
    private ArrayList<Integer> currTerritoryUnits = new ArrayList<>();

    @FXML
    public void initialize() {
        // at Assign Units Phase
        blackBackground.setVisible(true);
        battleTime.setVisible(false); // image for acting battle is not visible
        toolBar.setDisable(true); // ToolBar for orders is not visible
        finish.setDisable(true); // Finish Order button is not visible
        assignUnits.setOnAction(this::onInitAssignUnits);

        rule.setOnAction(event -> {
            try {
                showRule(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        // enable user to zoom or drag map
        mapImage.setOnScroll(this::onZoom);
        mapImage.setOnMouseDragged(this::onDrag);
        myOrder = new ArrayList<>();
    }

    /**
     * This is a private method used to format the units information received from
     * the server
     * 
     * @param unitsInfo The units information received from the server
     * @return The formatted units information
     */
    private String parseUnitsInfo(String unitsInfo) {
        String[] unitName = { "Freshman", "Sophomore", "Junior", "Senior", "Graduate", "PhD", "Professor" };
        String[] unitsInfoArray = unitsInfo.split(",");
        StringBuilder unitsInfoString = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            unitsInfoString.append(unitName[i] + ": " + unitsInfoArray[i] + ", ");
        }
        unitsInfoString.append(unitName[6] + ": " + unitsInfoArray[6]);
        return unitsInfoString.toString();
    }

    private int getUnitNum(String unitsInfo) {
        int sum = 0;
        String[] unitsInfoArray = unitsInfo.split(",");
        for (int i = 0; i < 7; i++) {
            sum += Integer.parseInt(unitsInfoArray[i]);
        }
        return sum;
    }

    private ArrayList<Integer> getUnitNumArray(String unitsInfo) {
        ArrayList<Integer> unitNumArray = new ArrayList<>();
        String[] unitsInfoArray = unitsInfo.split(",");
        for (int i = 0; i < 7; i++) {
            unitNumArray.add(Integer.parseInt(unitsInfoArray[i]));
        }
        return unitNumArray;
    }

    private String formatUnitsInfo(ArrayList<Integer> unitNumArray) {
        StringBuilder unitsInfoString = new StringBuilder();
        for (int i = 0; i < 7; i++) {
            unitsInfoString.append(unitNumArray.get(i) + ",");
        }
        return unitsInfoString.toString();
    }

    /**
     * This method is called when a territory button is clicked.
     * It retrieves information about the territory and displays it in the text
     * area. Depending on the player's status, it may also initiate an attack or a
     * move.
     * 
     * @param ae The action event generated by clicking on the button.
     * @throws Exception If there is an error getting territory information from the
     *                   map parser.
     */
    @FXML
    public void onTerritoryButton(ActionEvent ae) throws Exception {
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            showTerritoryInfo(btn.getId());
            HashMap<String, String> territoryInfo = mapParser.getTerritoryInfo(btn.getId());
            if (playerStatus == Status.ATTACK_FROM) {
                int unitnum = getUnitNum(territoryInfo.get("Unit"));
                if (unitnum > 0) {// initiate attack
                    oneOrderContent = btn.getId();
                    setEnemyTerritoryDisable(false);
                    setMyTerritoryDisable(true);
                    playerStatus = Status.ATTACK_TO;
                    currTerritoryUnits = getUnitNumArray(territoryInfo.get("Unit"));
                } else {
                    alert.showAlert("Alert", "This territory has 0 avaliable unit.");
                }
            } else if (playerStatus == Status.ATTACK_TO) {// finish initiating attack
                oneOrderContent += ", " + btn.getId() + ", ";
                setMyTerritoryDisable(false);
                playerStatus = Status.ATTACK_Units;
                onAttackMoveUnits();
                finish.setDisable(false);
                research.setDisable(false);
                move.setDisable(false);
                upgrade.setDisable(false);
                playerStatus = Status.DEFAULT;
            } else if (playerStatus == Status.MOVE_FROM) {
                int unitnum = getUnitNum(territoryInfo.get("Unit"));
                if (unitnum > 0) {// initiate move
                    oneOrderContent = btn.getId(); // oneOrderContent=[T1]
                    playerStatus = Status.MOVE_TO;
                    currTerritoryUnits = getUnitNumArray(territoryInfo.get("Unit"));
                } else {
                    alert.showAlert("Alert", "This territory has 0 avaliable unit.");
                }
            } else if (playerStatus == Status.MOVE_TO) {// finish initiating move
                oneOrderContent += ", " + btn.getId() + ", "; // oneOrderContent=[T1, T2, ]
                setEnemyTerritoryDisable(false);
                playerStatus = Status.MOVE_Units;
                onAttackMoveUnits();// oneOrderContent=[T1, T2, level, units]
                finish.setDisable(false);
                research.setDisable(false);
                attack.setDisable(false);
                upgrade.setDisable(false);
                playerStatus = Status.DEFAULT;
            }
        } else {
            throw new IllegalArgumentException("Invalid source " + source +
                    " for ActionEvent");
        }
    }

    /**
     * This method is called when a Finish button is clicked.
     * It sends the player's order list to the server.
     * Once all players have finished their turns, updates the game map and checks
     * if the game is still continuing or if there is a winner.
     * If the game is still continuing, checks if the player has lost and needs to
     * watch the game or if they can continue playing.
     * If the game is over, displays a message indicating the winner and closes the
     * client's socket connection.
     * 
     * @param ae The action event generated by clicking on the button.
     * @throws Exception
     */
    @FXML
    public void onFinishButton(ActionEvent ae) throws Exception {
        // Clear the order list and set player status to FINISH
        myOrder.clear();
        playerStatus = Status.FINISH;
        myOrder.add("d");
        client.playerOneAction(myOrder);
        battleTime.setVisible(true);
        alert.displayImageAlert("Finished", "/img/texts/wait.png");
        client.waitEveryoneDone();
        battleTime.setVisible(false);
        alert.displayImageAlert("New Round", "/img/texts/newround.png");
        try {
            history.appendText("====================New Round=======================\n");
            String msg = client.recvMsg();// receive war log
            Views view = new Views(System.out);
            history.appendText(view.displayLog(msg));
            msg = client.recvMsg();// receive map
            mapParser.setMap(msg);
            setMapParser();
            msg = client.recvMsg(); // receive "Game continous or Winner"
            if (msg.equals("Game continues")) {
                // receive "watching" "Choose watch" "do nothing"
                // "watching": player has already lost, he is watching game
                // "Choose watch": player lost, he need to choose watch or exit
                // "do nothing": player is alive, just continue game
                msg = client.recvMsg();
                if (msg.equals("Choose watch")) {
                    ChooseWatch();
                }
            } else {
                alert.displayImageAlert("Game Finish", "/img/texts/youwin.png");
                Socket clientSocket = client.getClientSocket();
                clientSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method is called when a Attack button is clicked. It set
     * the user's status to be ATTACK_FROM, and make enemy territory button be
     * disabled
     * And it will set attack button to be "Cancel" to let user cancel
     * attack order
     * 
     * @param ae The action event generated by clicking on the button.
     * @throws Exception
     */
    @FXML
    public void onAttackButton(ActionEvent ae) throws Exception {
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            if (btn.getText().equals("Attack")) {
                finish.setDisable(true);
                research.setDisable(true);
                move.setDisable(true);
                upgrade.setDisable(true);
                btn.setText("Cancel");
                myOrder.clear();
                playerStatus = Status.ATTACK_FROM;
                myOrder.add("a");// add attack order
                setEnemyTerritoryDisable(true);
            } else {
                finish.setDisable(false);
                research.setDisable(false);
                move.setDisable(false);
                upgrade.setDisable(false);
                btn.setText("Attack");
                myOrder.clear();
                playerStatus = Status.DEFAULT;
                setEnemyTerritoryDisable(false);
                setMyTerritoryDisable(false);
            }
        }
    }

    /**
     * This method is called when a Move button is clicked. It set
     * the user's status to be MOVE_FROM, and make enemy territory button be
     * disabled
     * And it will set Move button to be "Cancel" to let user cancel
     * move order
     * 
     * @param ae The action event generated by clicking on the button.
     * @throws Exception
     */
    @FXML
    public void onMoveButton(ActionEvent ae) throws Exception {
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            if (btn.getText().equals("Move")) {
                finish.setDisable(true);
                research.setDisable(true);
                attack.setDisable(true);
                upgrade.setDisable(true);
                btn.setText("Cancel");
                myOrder.clear();
                playerStatus = Status.MOVE_FROM;
                myOrder.add("m");// add attack order
                setEnemyTerritoryDisable(true);
            } else { // cancel
                finish.setDisable(false);
                research.setDisable(false);
                attack.setDisable(false);
                upgrade.setDisable(false);
                btn.setText("Move");
                myOrder.clear();
                playerStatus = Status.DEFAULT;
                setEnemyTerritoryDisable(false);
            }
        }
    }

    /**
     * This method is called when a Research button is clicked. It set
     * the user's status to be RESEARCH, and make enemy territory button be
     * disabled
     * And it will set Research button to be "Cancel" to let user cancel
     * research order
     * 
     * @param ae The action event generated by clicking on the button.
     * @throws Exception
     */
    @FXML
    public void onResearchButton(ActionEvent ae) throws Exception {
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            if (btn.getText().equals("Research")) {
                myOrder.clear();
                myOrder.add("r"); // add research order
                String problem = client.playerOneAction(myOrder);
                if (!problem.equals("Valid")) {
                    alert.showAlert("Invalid Action", problem);
                    history.appendText("Successful research\n");
                }
                // remove tech resource
                myOrder.clear();

            }
        }
    }

    /**
     * This method is called when the player is selecting the number of units when
     * attack or move
     * 
     * @throws Exception Throws an exception if there is an error in the input
     *                   format.
     */
    @FXML
    private void onAttackMoveUnits() throws Exception {
        int level = 0;
        int numUnits = 0;

        // Loop until the player enters a valid number of units to assign
        while (true) {
            ArrayList<String> res = setLevelsOfUnits();
            try {// If the player enters a value, try to parse it as an integer
                level = Integer.parseInt(res.get(0));
                numUnits = Integer.parseInt(res.get(1));
                if (numUnits > 0 && numUnits <= currTerritoryUnits.get(level)) {
                    currTerritoryUnits.set(level, currTerritoryUnits.get(level) - numUnits);
                    break;
                }
            } catch (NumberFormatException e) {
                // handle invalid input
            }
        }
        // Add the number of units assigned to the current order content and the overall
        // order
        oneOrderContent += String.valueOf(level) + ", " + String.valueOf(numUnits);// oneOrderContent=[T1, T2, level,
                                                                                   // numUnits]
        myOrder.add(oneOrderContent);

        // If the order is an attack, display the attack details in the history
        if (myOrder.get(0).equals("a")) {
            String[] parts = oneOrderContent.split(",");
            history.appendText("Attack from " + parts[0].trim() + " to " + parts[1].trim() + " with " + parts[3]
                    .trim() + " units(" + parts[2].trim() + " level).\n");
        } // If the order is a move, display the move details in the history
        else if (myOrder.get(0).equals("m")) {
            String[] parts = oneOrderContent.split(",");
            history.appendText("Move from " + parts[0].trim() + " to " + parts[1].trim() + " with " + parts[3]
                    .trim() + " units(" + parts[2].trim() + " level).\n");
        }
        String problem = client.playerOneAction(myOrder);
        if (!problem.equals("Valid")) {// display an alert if it's not valid
            alert.showAlert("Invalid Action", problem);
            // String promt = "1. Attack: you can only attack the territory next to your
            // territory.\n 2. Move: you can only move to the territory you can reach to.";
            // showAlert("Unvalid Territory", "The territories you choose are unvalid \n
            // please check:\n" + promt);
        } else {// update the number of units in the selected territory and clear the order
            String[] parts = oneOrderContent.split(",");
            String terr = parts[0].trim();
            mapParser.updateUnitsInTerritory(formatUnitsInfo(currTerritoryUnits), terr);
        }
        // Reset the attack and move button text and clear the current order
        attack.setText("Attack");
        move.setText("Move");
        myOrder.clear();
    }

    private ArrayList<String> setLevelsOfUnits() {
        // create the text input fields
        TextField textField1 = new TextField();
        TextField textField2 = new TextField();

        // create the dialog and set the content
        TextInputDialog dialog = new TextInputDialog();
        dialog.setHeaderText(null);
        dialog.setTitle("Enter Units Level and Number");
        dialog.getDialogPane()
                .setContent(new VBox(10, new Label("level : "), textField1, new Label("Number: "), textField2));

        // show the dialog and wait for the user response
        Optional<String> result = dialog.showAndWait();

        // check if the user clicked OK and retrieve the input values
        ArrayList<String> res = new ArrayList<>();
        if (result.isPresent()) {
            res.add(textField1.getText());
            res.add(textField2.getText());
            // process the input values here
        }
        return res;
    }

    private void showTerritoryInfo(String terrirtoryName) {
        HashMap<String, String> territoryInfo = mapParser.getTerritoryInfo(terrirtoryName);
        System.out.println("TerritoryInfo" + territoryInfo);
        textArea.appendText("==================== " + terrirtoryName + " =======================\n");
        textArea.appendText("Player " + territoryInfo.get("Player") + "'s \n");
        textArea.appendText("Units: " + parseUnitsInfo(territoryInfo.get("Unit")) + "\n");
        textArea.appendText("Neighbors: " + territoryInfo.get("Neighbors") + "\n");
        textArea.appendText("Rate: " + territoryInfo.get("Rate") + "\n");
        textArea.appendText("Resource: " + territoryInfo.get("Resource") + "\n");
    }

    /**
     * set enemy territory button disable or not
     * 
     * @param isDisabled
     */
    private void setEnemyTerritoryDisable(boolean isDisabled) {
        for (Node node : territroyGrid.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (!myTerritory.containsKey(btn.getId())) {
                    btn.setDisable(isDisabled);
                    String styleToRemove = isDisabled ? "button-able" : "button-unable";
                    String styleToAdd = isDisabled ? "button-unable" : "button-able";
                    btn.getStyleClass().remove(styleToRemove);
                    btn.getStyleClass().add(styleToAdd);
                }
            }
        }
    }

    /**
     * set my territory button disable or not
     * 
     * @param isDisabled
     */
    private void setMyTerritoryDisable(boolean isDisabled) {
        for (Node node : territroyGrid.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (myTerritory.containsKey(btn.getId())) {
                    btn.setDisable(isDisabled);
                    String styleToRemove = isDisabled ? "button-able" : "button-unable";
                    String styleToAdd = isDisabled ? "button-unable" : "button-able";
                    btn.getStyleClass().remove(styleToRemove);
                    btn.getStyleClass().add(styleToAdd);
                }
            }
        }
    }

    /**
     * Click "Rule", show rule
     * 
     * @param event
     */
    public void showRule(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ui/GameRule.fxml"));
            AnchorPane rulePane = fxmlLoader.load();
            Scene ruleScene = new Scene(rulePane, 800, 600);
            Stage ruleStage = new Stage();
            ruleStage.setScene(ruleScene);
            ruleStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    private void onInitAssignUnits(ActionEvent event) {
        int sum = getSpinnerSum();
        if (sum > maxUnits) {
            alert.showAlert("Invalid input", "Total units assigned exceeds the maximum allowed");
        } else {
            assignUnits();
            alert.displayImageAlert("Done!", "/img/texts/wait.png");
            try {
                client.waitEveryoneDone();
                mapParser.setMap(client.recvMsg());
                // receive "Game continous or Winner"
                System.out.println("Game continous or Winner: " + client.recvMsg());
                // receive "watching" "Choose watch" "do nothing"
                System.out.println("watching Choose watch do nothing: " + client.recvMsg());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assignUnits_GridPane.setVisible(false);
        }
        toolBar.setDisable(false);
        finish.setDisable(false);
        blackBackground.setDisable(true);
        blackBackground.setVisible(false);
    }

    private void ChooseWatch() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Choose an option");
        alert.setHeaderText(null);
        alert.setContentText("You lose. Please select one of the following options:");

        ButtonType watch = new ButtonType("Watch");
        ButtonType exit = new ButtonType("Exit");

        alert.getButtonTypes().setAll(watch, exit);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent()) {
            if (result.get() == watch) {
                finish.setDisable(true);
                toolBar.setDisable(true);
                try {
                    client.playerChooseWatch("w");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            } else {
                try {
                    client.playerChooseWatch("e");
                    Socket clientSocket = client.getClientSocket();
                    clientSocket.close();
                    URL xmlResource = getClass().getResource("/ui/StartGame.fxml");
                    FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
                    AnchorPane pane = fxmlLoader.load(); // Load the FXML file
                    territoryRoot.getChildren().setAll(pane);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
        assignUnits_GridPane.setVgap(15);
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
                System.out.println("i is: " + i);
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
