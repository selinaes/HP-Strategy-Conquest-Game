package edu.duke.ece651.team16.server;

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
            System.out.println("recieve: " + str);
            String[] arr = str.toString().split(":");
            if (arr[2].equals("All")) {
                System.out.println("send to all:" + arr[0] + ": " + arr[1]);
                chatServer.sendToAll(arr[0] + ": " + arr[1]);
            } else {
                chatServer.sendToOne(arr[1], arr[2], arr[0]);
            }
        }
    }
}