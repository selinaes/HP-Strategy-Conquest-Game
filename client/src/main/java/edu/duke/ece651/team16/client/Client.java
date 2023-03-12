package edu.duke.ece651.team16.client;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.InputStreamReader;
import java.net.SocketTimeoutException;
import java.net.SocketException;
import java.net.Socket;
import java.io.PrintStream;

public class Client {

    private BufferedReader socketReceive;
    private PrintWriter socketSend;
    private Socket clientSocket;

    //system input and output
    final PrintStream out;
    final BufferedReader inputReader;

    /* 
    * Constructor
    * @param IP: server IP
    * @param port: server port
    * @param inputSource: system input
    * @param out: system output
    * @throws IOException
    */  
    public Client(String IP, int port,  BufferedReader inputSource, PrintStream out) throws IOException {
        // views = new Views();
        // this.clientIO = new ClientIO(IP, port);
        // clientIO.send("Client are trying to connect to server...");
        this.out = out;
        this.inputReader = inputSource;
        try {
            this.clientSocket = new Socket(IP, port);
            this.socketReceive = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            this.socketSend = new PrintWriter(clientSocket.getOutputStream(), true);

            //system input and output
             //new BufferedReader(new InputStreamReader(System.in));
          } catch (IOException e) {
            System.out.println("Failed to initialize Connection.");
          }
    }

    /* 
    * Run client
    * @throws IOException
    */
    public void run() throws IOException {
        while (true) {
            // step1: Init Game setting: Color
            playerChooseColor();

            // step2: Init Game setting: Territory, Units

            // step3: Do placement

            // step4: Play game

        }

        // this.close();
    }

    /* 
    * Receive prompt from server
    * @return prompt message
    * @throws IOException
    */
    public String recvPrompt() throws IOException {
        boolean received = false;
        String prompt = null;
        while (!received) {
          try {
            prompt = socketReceive.readLine();
            if (prompt != null) {
              received = true;
            }
          } catch (IOException e) {
            System.out.println("Failed to receive message.");
          }
        }
        return prompt;
    }

    /*
    * Read client input from system input
    * @param prompt: prompt message
    * @return client input
    * @throws IOException
    */
    public String readClientInput(String prompt) throws IOException {
        out.println(prompt);
        String s = null;
        s = this.inputReader.readLine();
        if (s == null) {
            throw new EOFException();
        }
        return s;
    }

    /*
    * Send response to server
    * @param response: response message
    * @throws IOException
    */
    public void sendResponse(String response) throws IOException {
        this.socketSend.println(response);
    }

    /*
    * Close client
    * @throws IOException
    */
    public void close() throws IOException {
        try{
            clientSocket.close();
        }catch (IOException e){
            System.out.println("Failed to close socket..");
        }
    }

    /*
    * Player choose color and send to server
    * @throws IOException
    */ 
    public void playerChooseColor() throws IOException {
        // receive color choosing prompt from server
        String prompt = recvPrompt();
        String clientInput = "";
        boolean validInput = false;
        while(!validInput){
            try{
                clientInput = readClientInput(prompt);
                sendResponse(clientInput);
                // successful choose color
                String color = clientInput;
                out.println("Successfully set color: " + color);
            }
            catch(IllegalArgumentException e){
                out.println(e.getMessage());
            }
        }
    }
}
