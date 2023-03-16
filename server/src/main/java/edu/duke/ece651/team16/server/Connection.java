package edu.duke.ece651.team16.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class Connection {
  private Socket clientsocket;
  private BufferedReader in;
  private PrintWriter out;

  /**
   * Constructor for Server side Connection => will send/recv messages to/from
   * client
   * 
   * @param socket: the "accepted" client socket representing the client side
   */
  public Connection(Socket clientsocket) {
    try {
      this.clientsocket = clientsocket;
      this.in = new BufferedReader(new InputStreamReader(clientsocket.getInputStream()));
      this.out = new PrintWriter(clientsocket.getOutputStream(), true);

    } catch (IOException e) {
      System.out.println("Failed to initialize Connection.");
    }
  }

  /**
   * send a message to out
   *
   * @param msg: the message to be sent
   */
  public void send(String msg) {
    if (out != null) {
      out.println(msg);
    } else {
      System.err.println("Failed to send message: out is null.");
    }
  }

  /**
   * Receive a message from the other side
   * 
   * @return: the message received
   */
  public String recv() throws IOException {
    boolean received = false;
    String ans = null;
    while (!received) {
      try {
        ans = in.readLine();
        if (ans != null) {
          received = true;
        }
      } 
      // catch (SocketTimeoutException e) {
      //   System.out.println("Timeout occurred while waiting for message.");
      // }
      catch (IOException e) {
        System.out.println("Failed to receive message.");
        // System.exit(1);
      }
    }
    return ans;
  }

  /**
   * Close the connection
   */
  public void close() throws IOException {
    try {
      clientsocket.close();
    } catch (IOException e) {
      System.out.println("Failed to close socket..");
    }
  }
}
