package edu.duke.ece651.team16.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.text.Font;

import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PrintStream;
import javafx.scene.control.ChoiceBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

public class ChatRoomController {
    private ConnClient connector;
    private ChatClient chatClient;
    private String name;
    @FXML
    private Button send;
    @FXML
    private TextField input;
    @FXML
    private ListView content;
    @FXML
    private ChoiceBox toWho;
    private ChatHelper chatHelper;
    @FXML
    private Label chatRoomName;
    private List<String> playerList;
    private GamePlayController gamePlayController;

    public void setGamePlayController(GamePlayController gamePlayController) {
        this.gamePlayController = gamePlayController;
    }

    private class ChatHelper extends Thread {
        public void run() {
            while (true) {
                // receive content
                final StringBuilder str = new StringBuilder(connector.recv());
                System.out.println("recieve: " + str);
                String[] arr = str.toString().split(":");
                String[] result = new String[3];
                result[0] = arr[0];
                result[1] = String.join(":", Arrays.copyOfRange(arr, 1, arr.length - 1));
                result[2] = arr[arr.length - 1];
                if (result[0].equals("server")) {
                    Platform.runLater(() -> DisplayContent(str.toString(), 0));
                } else if (result[0].equals("playerlist")) {
                    Platform.runLater(() -> DisplayContent(result[1], 2));
                } else if (result[0].equals("map")) {
                    gamePlayController.setMapFromChatRoom(result[1]);
                } else if (result[2].equals(name)) { // other player's msg
                    Platform.runLater(() -> DisplayContent(result[0] + ": " + result[1], 3));
                } else if (result[2].equals("All") && !result[0].equals(name)) {
                    Platform.runLater(() -> DisplayContent(result[0] + ": " + result[1], 1));
                }
            }
        }
    }

    private void DisplayContent(String text, int comesFrom) {
        HBox Other = new HBox();
        Label msg = new Label(text);
        msg.setPadding(new Insets(10, 15, 10, 15));
        msg.setWrapText(true);
        msg.setFont(Font.font("Microsoft YaHei", 14));

        if (comesFrom == 1) {
            msg.setStyle("-fx-background-color: #d7b580; -fx-background-radius: 15; -fx-text-fill: #000;");
            Other.getChildren().addAll(msg);
            Other.setAlignment(Pos.CENTER_RIGHT);
            Other.setMargin(msg, new Insets(5, 10, 5, 50));
        } else if (comesFrom == 3) {
            msg.setStyle("-fx-background-color: #A9A9A9; -fx-background-radius: 15; -fx-text-fill: #000;");
            Other.getChildren().addAll(msg);
            Other.setAlignment(Pos.CENTER_LEFT);
            Other.setMargin(msg, new Insets(5, 50, 5, 10));
        } else if (comesFrom == 0) {
            msg.setStyle("-fx-background-color: #fc8e44; -fx-background-radius: 15; -fx-text-fill: #FFF;");
            Other.getChildren().addAll(msg);
            Other.setAlignment(Pos.CENTER_LEFT);
            Other.setMargin(msg, new Insets(5, 50, 5, 10));
        } else if (comesFrom == 2) {
            if (playerList == null) {
                playerList = new ArrayList<String>();
                String[] arr = text.toString().split(" ");
                for (int i = 0; i < arr.length; i++) {
                    playerList.add(arr[i]);
                }
                System.out.println(playerList);
                playerList.remove(name);
                playerList.add("All");
                toWho.getItems().addAll(playerList);
                return;
            }
        }
        content.getItems().add(Other);
    }

    public ChatRoomController(String color) {
        name = color;
        chatHelper = new ChatHelper();
        try {
            int port = 4321;
            String ip = "127.0.0.1";
            // String ip = "vcm-33174.vm.duke.edu";
            System.out.println("Connecting to " + ip + " on port " + port);
            Socket chatSocket = new Socket(ip, port);
            connector = new ConnClient(chatSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        chatHelper.start();
        // toWho.getItems().addAll("All", "red", "blue", "green", "yellow");
    }

    @FXML
    public void sendClick() throws IOException {
        String mytext = "";
        if (toWho.getValue().equals("All")) {
            mytext = input.getText();
        } else {
            mytext = input.getText() + ":" + "(private message to " + toWho.getValue() + ")";
        }
        HBox Other = new HBox();
        Label msg = new Label(mytext);
        msg.setPadding(new Insets(10, 15, 10, 15));
        msg.setWrapText(true);
        msg.setFont(Font.font("Microsoft YaHei", 14));
        msg.setStyle("-fx-background-color: #d7b580; -fx-background-radius: 15; -fx-text-fill: #000;");
        Other.getChildren().addAll(msg);
        Other.setAlignment(Pos.CENTER_RIGHT);
        Other.setMargin(msg, new Insets(5, 10, 5, 50));
        content.getItems().add(Other);

        String text = name + ": " + input.getText() + ":" + toWho.getValue();
        System.out.println("send: " + text);
        connector.send(text);
        input.clear();
    }

    public void sendMsg(String msg) throws IOException {
        System.out.println("send: " + msg);
        connector.send(msg);
    }
}