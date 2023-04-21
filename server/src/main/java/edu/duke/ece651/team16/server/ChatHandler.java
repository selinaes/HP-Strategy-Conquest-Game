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
            chatServer.sendToAll(str);
        }
    }
}