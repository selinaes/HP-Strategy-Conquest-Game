package edu.duke.ece651.team16.shared;

import java.net.*;
import java.io.*;

public class Connection {
  private Socket socket;
  private BufferedReader in;
  private PrintWriter out;
  private ServerSocket serverSocket;

  public Connection(String IP, int PORT, boolean isServer) {
    try {
      if(isServer){
        this.serverSocket = new ServerSocket(PORT);
        this.socket = serverSocket.accept();
      }
      else{
        this.socket = new Socket(IP, PORT);
      }
      this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
      this.out = new PrintWriter(socket.getOutputStream(), true);
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
      socket.close();
      if(serverSocket != null){
        serverSocket.close();
      }
    }catch (IOException e){
      System.out.println("Failed to close socket..");
    }
  }
}
