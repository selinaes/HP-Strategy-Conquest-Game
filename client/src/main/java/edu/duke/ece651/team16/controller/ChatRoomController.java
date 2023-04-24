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

import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PrintStream;

public class ChatRoomController {
    private Conn connector;
    private ChatClient chatClient;
    private String name;
    @FXML
    private Button send;
    @FXML
    private TextField input;
    @FXML
    private ListView content;
    private ChatHelper chatHelper;
    @FXML
    private Label chatRoomName;

    private class ChatHelper extends Thread {
        public void run() {
            while (true) {
                // receive content
                final StringBuilder str = new StringBuilder(connector.recv());
                System.out.println("recieve: " + str);
                // add content
                String[] arr = str.toString().split(":");
                if (arr[0].equals("server")) {
                    Platform.runLater(() -> DisplayContent(str.toString(), 0));
                } else if (!arr[0].equals(name)) {
                    Platform.runLater(() -> DisplayContent(str.toString(), 1));
                } else {
                    Platform.runLater(() -> DisplayContent(str.toString(), 2));
                }
            }
        }
    }

    private void DisplayContent(String text, int comesFrom) {
        HBox Other = new HBox();
        Label msg = new Label(text);
        msg.setPrefHeight(40);
        if (comesFrom == 2) {
            msg.setStyle("-fx-background-color: lightskyblue;" + "-fx-background-radius: 5, 4;");
            Other.getChildren().addAll(msg);
            Other.setAlignment(Pos.CENTER_RIGHT);
        } else if (comesFrom == 1) {
            msg.setStyle("-fx-background-color: #A9A9A9;" + "-fx-background-radius: 5, 4;");
            Other.getChildren().addAll(msg);
            Other.setAlignment(Pos.CENTER_LEFT);
        } else {
            msg.setStyle("-fx-background-color: red;" + "-fx-background-radius: 5, 4;");
            Other.getChildren().addAll(msg);
            Other.setAlignment(Pos.CENTER_LEFT);
        }
        content.getItems().add(Other);
    }

    public ChatRoomController(String color) {
        name = color;
        chatHelper = new ChatHelper();
        try {
            int port = 4321;
            String ip = "127.0.0.1";
            System.out.println("Connecting to " + ip + " on port " + port);
            Socket chatSocket = new Socket(ip, port);
            connector = new Conn(chatSocket);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        chatHelper.start();
    }

    @FXML
    public void sendClick() throws IOException {
        String text = name + ": " + input.getText();
        System.out.println("send: " + text);
        connector.send(text);
        input.clear();
    }

    public void sendMsg(String msg) throws IOException {
        System.out.println("send: " + msg);
        connector.send(msg);
    }
}