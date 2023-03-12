package edu.duke.ece651.team16.client;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.SocketException;
import java.io.IOException;
import java.net.Socket;

public class ClientIO {
    private BufferedReader in;
    private PrintWriter out;
    private Socket serverSocket;


    public ClientIO(String IP, int PORT) {
        try {
          this.serverSocket = new Socket(IP, PORT);
          this.in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
          this.out = new PrintWriter(serverSocket.getOutputStream(), true);
          
        } catch (IOException e) {
          System.out.println("Failed to initialize Connection.");
        }
    }

    public void send(String msg) {
        if (out != null) {
            out.println(msg);
        } else {
            System.err.println("Failed to send message: out is null.");
        }
    }

    public String recv() throws IOException {
        String ans = "";
        try {
            ans = in.readLine();
        } catch (IOException e) {
            System.out.println("Failed to receive message.");
        }
        return ans;
    }

    public void close() throws IOException {
        try{
            serverSocket.close();
        }catch (IOException e){
            System.out.println("Failed to close socket..");
        }
    }
}
