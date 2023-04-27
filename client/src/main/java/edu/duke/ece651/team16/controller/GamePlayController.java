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
import javafx.scene.control.Label;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import java.net.URL;
import java.net.Socket;
import java.util.HashMap;
import java.util.Arrays;

public class GamePlayController {
    @FXML
    private AnchorPane territoryRoot;
    @FXML
    private AnchorPane HPmap;
    @FXML
    private ToolBar toolBar;
    @FXML
    private Button special;
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
    private Button alliance;
    @FXML
    public Button watchUpdate;
    @FXML
    public Button exitGame;
    @FXML
    private TextArea textArea;
    @FXML
    private TextArea history;
    @FXML
    public ImageView mapImage;
    @FXML
    private Button finish;
    @FXML
    public ImageView battleTime;
    @FXML
    public Label color;

    private Client client;
    private MapParser mapParser;
    private Status playerStatus = Status.DEFAULT;
    private ArrayList<String> myOrder;
    private String oneOrderContent = "";
    private AlertBox alert = new AlertBox();
    private boolean usedSpecialOneRound = false;
    private LinkedHashMap<String, String> myTerritory;
    private int maxUnits = 24;

    private ArrayList<Integer> currTerritoryUnits = new ArrayList<>();
    private GamePlayDisplay gamePlayDisplay = new GamePlayDisplay();

    private ChatRoomController chatRoomController;
    private HashMap<String, ArrayList<String>> neighborLines = new HashMap<>();;

    @FXML
    private VBox chatRoomContainer;

    @FXML
    public void initialize() {
        // at Assign Units Phase
        exitGame.setVisible(false);
        watchUpdate.setVisible(false);
        battleTime.setVisible(false); // image for acting battle is not visible
        myOrder = new ArrayList<>();
        initNeighborLines();
        Platform.runLater(() -> {
            for (Node node : HPmap.getChildren()) {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    String currentTer = btn.getText();
                    String currentColor = mapParser.getTerritoryInfo(currentTer).get("Player");
                    String style = "button-" + currentColor;
                    btn.getStyleClass().remove("button-territory");
                    btn.getStyleClass().add(style);
                    showNeighborLine(btn);
                } else if (node instanceof Pane) {
                    Pane p = (Pane) node;
                    p.setVisible(false);
                }
            }
            if (!mapParser.checkAllowedNumAlly()) {
                alliance.setDisable(true);
            }
        });

    }

    private void initNeighborLines() {
        neighborLines.put("lake", new ArrayList<>(Arrays.asList("lakeToOffice", "willowToLake")));
        neighborLines.put("willow", new ArrayList<>(Arrays.asList("willowToLake", "willowToHall", "willowToLibrary")));
        neighborLines.put("library", new ArrayList<>(Arrays.asList("willowToLibrary", "libraryToSecret")));
        neighborLines.put("office", new ArrayList<>(Arrays.asList("lakeToOffice", "officeToRequirement")));
        neighborLines.put("hall", new ArrayList<>(Arrays.asList("willowToHall", "hallToSecret")));
        neighborLines.put("secret", new ArrayList<>(
                Arrays.asList("libraryToSecret", "hallToSecret", "secretToRequirement", "secretToPotions")));
        neighborLines.put("potion", new ArrayList<>(Arrays.asList("secretToPotions", "wingToPotions")));
        neighborLines.put("requirement",
                new ArrayList<>(Arrays.asList("officeToRequirement", "requirementToPitch", "secretToRequirement")));
        neighborLines.put("pitch", new ArrayList<>(Arrays.asList("pitchToTower", "pitchToWing", "requirementToPitch")));
        neighborLines.put("hospital",
                new ArrayList<>(Arrays.asList("pitchToWing", "wingToTower", "wingToForest", "wingToPotions")));
        neighborLines.put("tower", new ArrayList<>(Arrays.asList("pitchToTower", "wingToTower", "towerToForest")));
        neighborLines.put("forest", new ArrayList<>(Arrays.asList("towerToForest", "wingToForest")));
    }

    public void setColorText(String which) {
        color.setText("Color: " + which);
    }

    /**
     * set chatRoomController
     * 
     * @param chatRoomController
     */
    public void setChatRoomController(ChatRoomController chatRoomController) {
        this.chatRoomController = chatRoomController;
    }

    /**
     * Initialize the Map parser
     * 
     * @param mapParser
     */
    public void setMapParser(MapParser mapParser) {
        this.mapParser = mapParser;
    }

    /**
     * Initialize myterritory
     * 
     * @param t
     */
    public void setMyTerritory(LinkedHashMap<String, String> t) {
        this.myTerritory = t;
    }

    public void changeImage(String imageUrl) {
        Image newImage = new Image(imageUrl);
        mapImage.setImage(newImage);
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
        if (!(source instanceof Button)) {
            throw new IllegalArgumentException("Invalid source " + source +
                    " for ActionEvent");
        }
        Button btn = (Button) source;
        // showTerritoryInfo(btn.getText());
        HashMap<String, String> territoryInfo = mapParser.getTerritoryInfo(btn.getText());
        int unitnum;
        switch (playerStatus) {
            case ATTACK_FROM:
                unitnum = gamePlayDisplay.getUnitNum(territoryInfo.get("Unit"));
                if (unitnum > 0) {// initiate attack
                    oneOrderContent = btn.getText();
                    setEnemyTerritoryDisable(false);
                    setMyTerritoryDisable(true);
                    playerStatus = Status.ATTACK_TO;
                    currTerritoryUnits = gamePlayDisplay.getUnitNumArray(territoryInfo.get("Unit"));
                } else {
                    alert.showAlert("Alert", "This territory has 0 avaliable unit.");
                }
                break;
            case ATTACK_TO:
                oneOrderContent += ", " + btn.getText() + ", ";
                setMyTerritoryDisable(false);
                playerStatus = Status.ATTACK_Units;
                onAttackMoveUnits();
                setButtonsDisabled(false, finish, research, move, upgrade, alliance, special);
                playerStatus = Status.DEFAULT;
                break;
            case MOVE_FROM:
                unitnum = gamePlayDisplay.getUnitNum(territoryInfo.get("Unit"));
                if (unitnum > 0) {// initiate move
                    oneOrderContent = btn.getText(); // oneOrderContent=[T1]
                    playerStatus = Status.MOVE_TO;
                    currTerritoryUnits = gamePlayDisplay.getUnitNumArray(territoryInfo.get("Unit"));
                } else {
                    alert.showAlert("Alert", "This territory has 0 avaliable unit.");
                }
                break;
            case MOVE_TO:
                oneOrderContent += ", " + btn.getText() + ", "; // oneOrderContent=[T1, T2, ]
                setEnemyTerritoryDisable(false);
                playerStatus = Status.MOVE_Units;
                onAttackMoveUnits();// oneOrderContent=[T1, T2, level, units]
                setButtonsDisabled(false, finish, research, attack, upgrade, alliance, special);
                playerStatus = Status.DEFAULT;
                break;
            case UPGRADE_AT:
                unitnum = gamePlayDisplay.getUnitNum(territoryInfo.get("Unit"));
                if (unitnum > 0) { // can upgrade
                    oneOrderContent = btn.getText(); // oneOrderContent= [T1] source, unitNum, initialLevel,
                                                     // upgradeAmount
                    System.out.println("One Order Content: " + oneOrderContent);
                    setMyTerritoryDisable(false);
                    currTerritoryUnits = gamePlayDisplay.getUnitNumArray(territoryInfo.get("Unit"));
                    System.out.println("Before onUpgradeUnits");
                    onUpgradeUnits();
                    System.out.println("After onUpgradeUnits");
                    setButtonsDisabled(false, finish, research, attack, move, alliance, special);
                    playerStatus = Status.DEFAULT;
                    setEnemyTerritoryDisable(false);
                } else { // cannot upgrade, no unit
                    alert.showAlert("Alert", "This territory has 0 avaliable unit.");
                }
                break;
            case BOMB_AT:
                oneOrderContent += ", " + btn.getText(); // oneOrderContent= Nuclear Bomb, [T1] location
                System.out.println("One Order Content: " + oneOrderContent);
                myOrder.add(oneOrderContent);
                performAction(myOrder);
                oneOrderContent = "";
                setMyTerritoryDisable(false);
                playerStatus = Status.DEFAULT;
                break;
            default:
                break;
        }
    }

    /**
     * set up mapParser
     */
    private void setupMapParser() {
        List<String> territory = mapParser.getMyTerritory();
        myTerritory = new LinkedHashMap<String, String>();
        for (String t : territory) {
            myTerritory.put(t, "0");
        }
    }

    /**
     * button for watching game info
     * 
     * @param ae The action event generated by clicking on the button.
     * @throws Exception
     */
    @FXML
    public void onWatchGameInfo(ActionEvent event) throws IOException {
        try {
            client.waitEveryoneDone();
            history.appendText("====================New Round=======================\n");
            String msg = client.recvMsg();// receive war log
            Views view = new Views(System.out);
            history.appendText(view.displayLog(msg));
            mapParser.setMap(client.recvMsg());// receive map
            setupMapParser();

            msg = client.recvMsg(); // receive "Game continous or Winner"
            if (msg.equals("Game continues")) {
                msg = client.recvMsg(); // watching
            } else {
                history.appendText("Winner is " + msg + "!");
                // alert.displayImageAlert("Game Finish", "/img/texts/youlose.png");
                PopupBox popup = new PopupBox(territoryRoot);
                popup.display("/img/texts/youlose.png");

                // close socket! Game ends
                Socket clientSocket = client.getClientSocket();
                clientSocket.close();

                exitGame.setVisible(true);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method is called when a Finish button is clicked.
     * 
     * @param ae The action event generated by clicking on the button.
     * @throws Exception
     */
    @FXML
    public void onFinishButton(ActionEvent ae) throws Exception {
        battleTime.setVisible(true);
        // Clear the order list and set player status to FINISH
        myOrder.clear();
        playerStatus = Status.FINISH;
        myOrder.add("d");
        client.playerOneAction(myOrder);
        // PopupBox popup1 = new PopupBox(territoryRoot);
        // popup1.display("/img/texts/wait.png");
        // changeImage("/img/backgrounds/waiting4.png");
        alert.displayImageAlert("Finished", "/img/texts/wait.png");
        client.waitEveryoneDone();
        battleTime.setVisible(false);
        // alert.displayImageAlert("New Round", "/img/texts/newround.png");
        PopupBox popup = new PopupBox(territoryRoot);
        popup.display("/img/texts/newround.png");
        // set special buttons to enable
        special.setDisable(false);
        usedSpecialOneRound = false;
        try {
            history.appendText("====================New Round=======================\n");
            String msg = client.recvMsg();// receive war log
            Views view = new Views(System.out);
            history.appendText(view.displayLog(msg));
            msg = client.recvMsg();// receive map
            mapParser.setMap(msg);
            if (!mapParser.checkAllowedNumAlly()) {
                alliance.setDisable(true);
            }
            setupMapParser();
            msg = client.recvMsg(); // receive "Game continous or Winner"
            if (msg.equals("Game continues")) {
                msg = client.recvMsg();
                if (msg.equals("Choose watch")) {
                    ChooseWatch();
                }
            } else {
                if (msg.equals(client.getColor())) { // if winner is self
                    // alert.displayImageAlert("Game Finish", "/img/texts/youwin.png");
                    popup.display("/img/texts/youwin.png");
                    Socket clientSocket = client.getClientSocket();
                    clientSocket.close();

                    exitGame.setVisible(true);
                } else {
                    history.appendText("Winner is " + msg + "!");
                    // alert.displayImageAlert("Game Finish", "/img/texts/youlose.png");
                    popup.display("/img/texts/youlose.png");

                    // close socket! Game ends
                    Socket clientSocket = client.getClientSocket();
                    clientSocket.close();

                    exitGame.setVisible(true);
                }
            }
            for (Node node : HPmap.getChildren()) {
                if (node instanceof Button) {
                    Button btn = (Button) node;
                    String currentTer = btn.getText();
                    String currentColor = mapParser.getTerritoryInfo(currentTer).get("Player");
                    String style = "button-" + currentColor;
                    String style2 = btn.getStyleClass().toString();
                    String[] originStyle = style2.split(" ");
                    System.out.println(btn.getStyleClass().toString());// button button-blue
                    System.out.println(originStyle[1]);
                    btn.getStyleClass().remove(originStyle[1]);
                    btn.getStyleClass().add(style);
                    showNeighborLine(btn);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onSpecialButton(ActionEvent ae) throws Exception {
        // System.out.println("Special Button Clicked");
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            myOrder.clear();
            myOrder.add("s"); // add special order
            // roll dice, 1: double resource, 2: two unit 3: disregard adjacency 4: dice
            // advantage 5: nuclear bomb
            Random rand = new Random();
            int num = rand.nextInt(5) + 1; // 1, 2, 3, 4， 5
            String option = "";
            switch (num) {
                case 1:
                    option = "Double Resource Production";
                    break;
                case 2:
                    option = "Two Students Generation";
                    break;
                case 3:
                    option = "Disregard Adjacency";
                    break;
                case 4:
                    option = "Dice Advantage";
                    break;
                case 5:
                    option = "Nuclear Bomb";
                    break;
                default:
                    break;
            }
            // System.out.println("Special Option: " + option);
            PopupBox popup = new PopupBox(territoryRoot);
            if (num != 5) {
                popup.displayText("Special Ability", "You used special ability - " + option + " - for this turn!");
                // add option to order
                myOrder.add(option);
                performAction(myOrder);
            } else {
                popup.displayText("Special Ability", "You used special ability - " + option
                        + " - for this turn!\nPlease choose an enemy territory to bomb!");
                oneOrderContent = option; // "Nuclear Bomb, T1"
                setMyTerritoryDisable(true); // disable my territory, leave enemy territories
                playerStatus = Status.BOMB_AT;
            }

            // disable after one use, per turn
            btn.setDisable(true);
            usedSpecialOneRound = true;
        }
    }

    /**
     * This method is called when a Attack button is clicked.
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
                setButtonsDisabled(true, finish, research, move, upgrade, alliance, special);
                btn.setText("Cancel");
                myOrder.clear();
                playerStatus = Status.ATTACK_FROM;
                myOrder.add("a");// add attack order
                setEnemyTerritoryDisable(true);
                setAllyTerritoryDisable(false);
            } else {
                setButtonsDisabled(false, finish, research, move, upgrade, alliance, special);
                btn.setText("Attack");
                myOrder.clear();
                playerStatus = Status.DEFAULT;
                setMyTerritoryDisable(false);
                setEnemyTerritoryDisable(false);
            }
        }
    }

    /**
     * This method is called when a Move button is clicked.
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
                setButtonsDisabled(true, finish, research, attack, upgrade, alliance, special);
                btn.setText("Cancel");
                myOrder.clear();
                playerStatus = Status.MOVE_FROM;
                myOrder.add("m");// add attack order
                setEnemyTerritoryDisable(true);
                setAllyTerritoryDisable(false);
            } else { // cancel
                setButtonsDisabled(false, finish, research, attack, upgrade, alliance, special);
                btn.setText("Move");
                myOrder.clear();
                playerStatus = Status.DEFAULT;
                setEnemyTerritoryDisable(false);
            }
        }
    }

    @FXML
    public void onAllianceButton(ActionEvent ae) throws Exception {
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            myOrder.clear();
            myOrder.add("l"); // add alliance order
            onAlliancePlayer();
            performAction(myOrder);
        }

    }

    /*
     * This method is called when an Upgrade button is clicked.
     * 
     * @param ae The action event generated by clicking on the button
     * 
     * @throws Exception
     */
    @FXML
    public void onUpgradeButton(ActionEvent ae) throws Exception {
        Object source = ae.getSource();
        if (source instanceof Button) {
            Button btn = (Button) source;
            if (btn.getText().equals("Upgrade")) {
                setButtonsDisabled(true, finish, research, attack, move, alliance, special);
                btn.setText("Cancel");
                myOrder.clear();
                myOrder.add("u"); // add upgrade order
                playerStatus = Status.UPGRADE_AT;
                setEnemyTerritoryDisable(true);
            } else { // cancel
                setButtonsDisabled(false, finish, research, attack, move, alliance, special);
                btn.setText("Upgrade");
                myOrder.clear();
                playerStatus = Status.DEFAULT;
                setEnemyTerritoryDisable(false);
            }
        }
    }

    /**
     * This method is called when a Research button is clicked.
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
                performAction(myOrder);
            }
        }
    }

    /* set a list of buttons disables to true or false */
    private void setButtonsDisabled(boolean disabled, Button... buttons) {
        Arrays.asList(buttons).forEach(btn -> {
            if (!(btn.equals(alliance)) && !(btn.equals(special))) {
                btn.setDisable(disabled);
            } else if (btn.equals(alliance) && mapParser.checkAllowedNumAlly()) {
                btn.setDisable(disabled);
            } else if (btn.equals(special) && !usedSpecialOneRound) {
                btn.setDisable(disabled);
            }
        });

        // Arrays.asList(buttons).forEach(btn -> btn.setDisable(disabled));
    }

    /**
     * Display the box to let player enter the player name to alliance with
     *
     */
    @FXML
    private void onAlliancePlayer() {
        String alliancePlayer = "";
        while (true) {
            ArrayList<String> res = gamePlayDisplay.setAllianceInfo();
            alliancePlayer = res.get(0);
            if (alliancePlayer != null) {
                myOrder.add(alliancePlayer);
                break;
            }
        }
    }

    /*
     * This method is called when player is selecting the num of units to upgrade
     * and the initial level and amount to update
     */
    @FXML
    private void onUpgradeUnits() throws Exception {
        int numUnits = 0;
        int initialLevel = 0;
        int upgradeAmount = 0;
        // Loop until the player enters a valid number of units to assign
        while (true) {
            ArrayList<String> res = gamePlayDisplay.setUpgradeInfo();
            try {// If the player enters a value, try to parse it as an integer
                numUnits = Integer.parseInt(res.get(0));
                initialLevel = Integer.parseInt(res.get(1));
                upgradeAmount = Integer.parseInt(res.get(2));
                if (numUnits > 0 && numUnits <= currTerritoryUnits.get(initialLevel)) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException:" + e.getMessage());
            }
        }
        oneOrderContent += ", " + String.valueOf(numUnits) + ", " + String.valueOf(initialLevel) + ", "
                + String.valueOf(upgradeAmount);
        // Add the upgrade order to the list of orders
        myOrder.add(oneOrderContent);
        System.out.println("myOrder:" + myOrder);
        performAction(myOrder);
        upgrade.setText("Upgrade");
    }

    /**
     * This method is called when the player is assigning units when attack or move
     * 
     * @throws Exception Throws an exception if there is an error in input format.
     */
    @FXML
    private void onAttackMoveUnits() throws Exception {
        int level = 0;
        int numUnits = 0;
        // Loop until the player enters a valid number of units to assign
        while (true) {
            ArrayList<String> res = gamePlayDisplay.setLevelsOfUnits();
            try {// If the player enters a value, try to parse it as an integer
                level = Integer.parseInt(res.get(0));
                numUnits = Integer.parseInt(res.get(1));
                if (numUnits > 0 && numUnits <= currTerritoryUnits.get(level)) {
                    break;
                }
            } catch (NumberFormatException e) {
                System.out.println("NumberFormatException:" + e.getMessage());
            }
        }
        // Add the number of units assigned to the current order content and the order
        oneOrderContent += String.valueOf(level) + ", " + String.valueOf(numUnits);// oneOrderContent=[T1, T2, level,
                                                                                   // numUnits]
        myOrder.add(oneOrderContent);
        performAction(myOrder);
        attack.setText("Attack");
        move.setText("Move");
    }

    /**
     * This method will show the latest territory info from the mapParser
     * within the text area
     * 
     * @params territoryName the name of the territory
     */
    private void showTerritoryInfo(String terrirtoryName) {
        HashMap<String, String> territoryInfo = mapParser.getTerritoryInfo(terrirtoryName);
        System.out.println("TerritoryInfo" + territoryInfo);
        // textArea.appendText(gamePlayDisplay.getTerritoryInfo(terrirtoryName,
        // territoryInfo));
    }

    /**
     * set enemy territory button disable or not
     * 
     * @param isDisabled
     */
    private void setEnemyTerritoryDisable(boolean isDisabled) {
        for (Node node : HPmap.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (!myTerritory.containsKey(btn.getText())) {
                    btn.setDisable(isDisabled);
                    String currentTer = btn.getText();
                    String currentColor = mapParser.getTerritoryInfo(currentTer).get("Player");
                    String style = "button-" + currentColor;
                    String styleToRemove = isDisabled ? style : "button-territory";
                    String styleToAdd = isDisabled ? "button-territory" : style;
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
        for (Node node : HPmap.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (myTerritory.containsKey(btn.getText())) {
                    btn.setDisable(isDisabled);
                    String currentTer = btn.getText();
                    String currentColor = mapParser.getTerritoryInfo(currentTer).get("Player");
                    String style = "button-" + currentColor;
                    String styleToRemove = isDisabled ? style : "button-territory";
                    String styleToAdd = isDisabled ? "button-territory" : style;
                    btn.getStyleClass().remove(styleToRemove);
                    btn.getStyleClass().add(styleToAdd);
                }
            }
        }
    }

    /**
     * set ally territory button disable or not
     * 
     * @param isDisabled
     */
    private void setAllyTerritoryDisable(boolean isDisabled) {
        String ally = mapParser.findAlly();
        if (ally.equals("")) {
            return;
        }
        for (Node node : HPmap.getChildren()) {
            if (node instanceof Button) {
                Button btn = (Button) node;
                if (mapParser.getAllyTerritory(ally).contains(btn.getText())) {
                    btn.setDisable(isDisabled);
                    String currentTer = btn.getText();
                    String currentColor = mapParser.getTerritoryInfo(currentTer).get("Player");
                    String style = "button-" + currentColor;
                    String styleToRemove = isDisabled ? style : "button-territory";
                    String styleToAdd = isDisabled ? "button-territory" : style;
                    btn.getStyleClass().remove(styleToRemove);
                    btn.getStyleClass().add(styleToAdd);
                }
            }
        }
    }

    /**
     * set client
     * 
     * @param client
     */
    public void setClient(Client client) {
        this.client = client;
    }

    /**
     * When a player loses, choose whether to watch or to exit
     */
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
                toolBar.setVisible(false);
                watchUpdate.setVisible(true);
                watchUpdate.setDisable(false);
                exitGame.setVisible(true);
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

    /**
     * Perform the action from myOrder
     * 
     * @param myOrder the order to be performed
     */
    private void performAction(ArrayList<String> myOrder) throws IOException {
        String problem = client.playerOneAction(myOrder);
        if (!problem.equals("Valid") && !problem.equals("Waiting for Alliance")) {// display an alert if it's not valid
            alert.showAlert("Invalid Action", problem);
        } else if (problem.equals("Waiting for Alliance")) {
            String allianceProblem = "Sent an alliance request, and waiting for the other player's response";
            mapParser.setMap(client.recvMsg());
            alert.showAlert("Waiting for Alliance", allianceProblem);
        } else {// update the number of units in the selected territory and clear the order
            if (myOrder.get(0) != "l") {
                history.appendText(gamePlayDisplay.getActionInfo(myOrder));
            } else {
                String msg = gamePlayDisplay.getActionInfo(myOrder);
                String sendString = client.getColor() + ": " + msg + ":All";
                System.out.println("want to send: " + sendString);
                chatRoomController.sendMsg(sendString);
            }
            mapParser.setMap(client.recvMsg());
        }
        myOrder.clear();
    }

    /**
     * disconnnected from the game
     */
    @FXML
    public void onExitButton(ActionEvent ae) throws Exception {
        // Socket clientSocket = client.getClientSocket();
        // clientSocket.close();
        URL xmlResource = getClass().getResource("/ui/login.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(xmlResource); // Create a new FXMLLoader
        AnchorPane pane = fxmlLoader.load(); // Load the FXML file
        territoryRoot.getChildren().setAll(pane);
    }

    @FXML
    private void showNeighborLine(Button btn) {
        ArrayList<String> lines = neighborLines.get(btn.getId());
        String styleToRemove = "default-line";
        String styleToAdd = "highlight-line";
        btn.setOnMouseEntered(event -> {
            for (Node node : HPmap.getChildren()) {
                if (node instanceof Line) {
                    Line l = (Line) node;
                    if (lines.contains(l.getId())) {
                        l.getStyleClass().remove(styleToRemove);
                        l.getStyleClass().add(styleToAdd);
                    }
                } else if (node instanceof Pane) {
                    Pane p = (Pane) node;
                    String panename = btn.getId() + "Pane";
                    if (p.getId().equals(panename)) {
                        p.setVisible(true);
                        TextArea textArea = (TextArea) p.getChildren().get(0); // get the TextArea
                        HashMap<String, String> panemap = mapParser.getTerritoryInfo(btn.getText());
                        textArea.setText(gamePlayDisplay.getTerritoryInfo(btn.getText(), panemap));
                    }
                }
            }
        });
        btn.setOnMouseExited(event -> {
            for (Node node : HPmap.getChildren()) {
                if (node instanceof Line) {
                    Line l = (Line) node;
                    if (lines.contains(l.getId())) {
                        l.getStyleClass().remove(styleToAdd);
                        l.getStyleClass().add(styleToRemove);
                    }
                } else if (node instanceof Pane) {
                    Pane p = (Pane) node;
                    String panename = btn.getId() + "Pane";
                    if (p.getId().equals(panename)) {
                        p.setVisible(false);
                    }
                }
            }
        });
    }
}
