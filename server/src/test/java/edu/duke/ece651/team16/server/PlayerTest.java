package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.ArrayList;
import java.net.Socket;
import java.util.List;
import org.junit.jupiter.api.Test;
import edu.duke.ece651.team16.shared.*;

public class PlayerTest {
  @Test
  public void test_addTerritory() throws IOException {
    Socket s1 = new Socket("127.0.0.1", 1234);
    Connection c1 = new Connection(s1);
    Player p = new Player("testPlayer", "blue", c1);
    Territory t = new Territory("Gondor");
    p.addTerritories(t);
    // create a list and add the territory to it
    List<Territory> list = new ArrayList<Territory>();
    list.add(t);
    assertEquals(p.getTerritories(), list);
  }

  @Test
  public void test_getName_getColor() throws IOException {
    Socket s1 = new Socket("127.0.0.1", 1234);
    Connection c1 = new Connection(s1);
    Player p = new Player("testPlayer", "blue", c1);
    assertEquals(p.getName(), "testPlayer");
    assertEquals(p.getColor(), "blue");
  }
  
  // @Test
  // public void test_getConnection(){
  //   Connection c = new Connection(new Socket());
  //   Player p = new Player("testPlayer", "blue", c);
  //   assertEquals(p.getConnection(), c);
  // }

}
