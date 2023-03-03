package edu.duke.ece651.team32374.client;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AppTest {
    @Test
    void test_GetMessage() {
      App a = new App();
      assertEquals("Hello from the client for team32374", a.getMessage());
    }
}
