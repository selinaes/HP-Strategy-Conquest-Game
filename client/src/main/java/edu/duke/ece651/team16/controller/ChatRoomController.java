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

import java.io.IOException;
import java.net.Socket;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.PrintStream;

public class ChatRoomController {
    private ChatClient chatClient;
    private String name;
    @FXML
    private Button send;
    @FXML
    private TextField input;
    @FXML
    private ListView content;
    private Stage Window;
    private ChatHelper chatHelper;

    private class ChatHelper extends Thread {
        public void run() {
            while (true) {
                // receive content
                String str = "";
                try {
                    str = chatClient.recvMsg();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                // add content
                String[] arr = str.split(":");
                if (!arr[0].equals(name)) {
                    DisplayContent(arr[1], false);
                } else {
                    DisplayContent(arr[1], true);
                }
            }
        }
    }

    private void DisplayContent(String text, boolean Mine) {
        HBox Other = new HBox();
        Label msg = new Label(text);
        msg.setPrefHeight(40);
        if (Mine) {
            msg.setStyle("-fx-background-color: lightskyblue;" + "-fx-background-radius: 5, 4;");
            Other.getChildren().addAll(msg);
            Other.setAlignment(Pos.CENTER_RIGHT);
        } else {
            msg.setStyle("-fx-background-color: darkseagreen;" + "-fx-background-radius: 5, 4;");
            Other.getChildren().addAll(msg);
            Other.setAlignment(Pos.CENTER_LEFT);
        }
        content.getItems().add(Other);
    }

    public ChatRoomController(String color, Stage W) {
        Window = W;
        name = color;
        chatHelper = new ChatHelper();
        try {
            int port = 4321;
            String ip = "127.0.0.1";

            Socket chatSocket = new Socket(ip, port);
            PrintStream out = System.out;
            BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader socketReceive = new BufferedReader(new InputStreamReader(chatSocket.getInputStream()));
            PrintWriter socketSend = new PrintWriter(chatSocket.getOutputStream(),
                    true);
            chatClient = new ChatClient(inputReader, out, socketReceive, socketSend);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initialize() {
        chatHelper.start();
    }

    @FXML
    public void sendClick() throws IOException {
        chatClient.sendResponse(input.getText());
        input.clear();
    }
}