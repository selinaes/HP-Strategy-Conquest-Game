/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.duke.ece651.team16.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

public class App {
  public static Client client;

  public static void main(String[] args) throws IOException {
    int port = 1234;
    String ip = "vcm-32174.vm.duke.edu";
    
    Socket clientSocket = null;
    try {
      clientSocket = new Socket(ip, port);
      PrintStream out = System.out;
      BufferedReader inputReader = new BufferedReader(new InputStreamReader(System.in));
      BufferedReader socketReceive = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
      PrintWriter socketSend = new PrintWriter(clientSocket.getOutputStream(), true);
      Client client = new Client(inputReader, out, socketReceive, socketSend);
      client.run();
    } catch (IOException e) {
      System.out.println("Failed to initialize Connection.");
      System.exit(1);
    } finally {
      if (clientSocket != null) {
        clientSocket.close();
      }
    }
  }
}
