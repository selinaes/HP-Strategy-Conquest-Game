package edu.duke.ece651.team16.shared;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class ConnectionTest {

  @Test
  public void test_connection() throws IOException {
    Connection c1 = new Connection("127.0.0.1", 12345);

    String msg = "Hello";

    c1.send(msg);

    String response = c1.recv();
    assertEquals(msg, response);

    c1.close();
  }

}
