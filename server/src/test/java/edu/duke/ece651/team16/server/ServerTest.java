package edu.duke.ece651.team16.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import org.junit.jupiter.api.Test;

import edu.duke.ece651.team16.shared.Connection;

public class ServerTest {
  // @Test
  // public void testRun() throws Exception {
  //   ServerSocket serverSocketMock = mock(ServerSocket.class);
  //   Socket socketMock = mock(Socket.class);

  //   Connection connectionMock = mock(Connection.class);
  //   Player p1 = mock(Player.class);

  //   when(serverSocketMock.accept()).thenReturn(socketMock);
  //   when(socketMock.getInputStream()).thenReturn(mock(InputStream.class));
  //   when(socketMock.getOutputStream()).thenReturn(mock(OutputStream.class));

  //   when(connectionMock.recv()).thenReturn("Hello World!");

  //   when(p1.getName()).thenReturn("Player1");

  //   Server server = new Server(serverSocketMock);

  //   server.addPlayer(p1);
  //   server.run();
  //   verify(connectionMock).recv();
  //   verify(p1).getName();
  // }

  @Test
  public void test_formMap() {
    // int port = 1234;
    // Server s = new Server(port);
    // Player p1 = new Player("p1", "blue", null);
    // Territory t = new Territory("Gondor");
    // ArrayList<Territory> neighbors = new ArrayList<Territory>();
    // neighbors.add(new Territory("Mordor"));
    // t.setNeighbors(neighbors);
    // s.addPlayer(p1);
    // p1.addTerritories(t);
    // HashMap<String, ArrayList<String>> map = new HashMap<String,  ArrayList<String>>();
    // ArrayList<String> list = new ArrayList<String>();
    // list.add("Gondor");
    // list.add("Mordor");
    // map.put("p1", list);
    // assertEquals(map,  s.formMap());
  }

}
