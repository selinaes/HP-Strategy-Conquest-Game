package edu.duke.ece651.team16.server;

import java.util.Arrays;

public class ChatHandler extends Thread {
    private Conn connector;
    private ChatServer chatServer;

    public ChatHandler(Conn connector, ChatServer chatServer) {
        this.connector = connector;
        this.chatServer = chatServer;
    }

    public void run() {
        while (true) {
            String str = connector.recv();
            String[] arr = str.toString().split(":");
            String[] result = new String[3];
            result[0] = arr[0];
            result[1] = String.join(":", Arrays.copyOfRange(arr, 1, arr.length - 1));
            result[2] = arr[arr.length - 1];
            if (result[2].equals("All")) {
                System.out.println("send to all:" + result[0] + ": " + result[1]);
                chatServer.sendToAll(result[0] + ": " + result[1]);
            } else {
                System.out.println("send to one:" + result[0] + ": " + result[1] + ": " + result[2]);
                chatServer.sendToOne(result[1], result[2], result[0]);
            }
        }
    }
}