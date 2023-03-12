package edu.duke.ece651.team16.client;

import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.SocketException;
import java.io.IOException;
import java.net.Socket;

public class ClientIO {
    private BufferedReader out;
    private PrintWriter in;
    private Socket clientSocket;

    public ClientIO(String IP, int PORT) {
        try {
          this.clientSocket = new Socket(IP, PORT);
          this.out = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
          this.in = new PrintWriter(clientSocket.getOutputStream(), true);
        } catch (IOException e) {
          System.out.println("Failed to initialize Connection.");
        }
    }

    // write to client's input stream
    // public void send(String msg) {
    //     // write msg to input stream
        
    //     if (out != null) {
    //         out.println(msg);
    //     } else {
    //         System.err.println("Failed to send message: out is null.");
    //     }
    // }

    // read from client's output stream
    public String recv() throws IOException {
        String ans = "";
        try {
            ans = out.readLine();
        } catch (IOException e) {
            System.out.println("Failed to receive message.");
        }
        return ans;
    }

    public void close() throws IOException {
        try{
            clientSocket.close();
        }catch (IOException e){
            System.out.println("Failed to close socket..");
        }
    }
}
