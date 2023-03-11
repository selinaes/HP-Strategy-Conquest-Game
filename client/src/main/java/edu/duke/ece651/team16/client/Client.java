package edu.duke.ece651.team16.client;
import edu.duke.ece651.team16.shared.*;
import java.io.IOException;

public class Client {
    private Views views;
    private Connection client_connection;

    public Client(String ip, int port) {
        views = new Views();
        this.client_connection = new Connection(ip, port, false);
    }

    // receive the map from server
    public void recvMap() throws IOException {
        String received = client_connection.recv();
        // printout the string
        System.out.println(received);
    }

    /* 
    * Close the connection
    */
    public void close() throws IOException {
        client_connection.close();
    }
}
