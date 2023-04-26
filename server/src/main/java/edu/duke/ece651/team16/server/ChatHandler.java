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
            if(arr[0].equals("quit")) {
                chatServer.remove(connector);
                break;
            }
            chatServer.sendToAll(str);
        }
    }
}