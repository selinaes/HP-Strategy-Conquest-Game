package edu.duke.ece651.team16.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class ViewsTest {
  @Test
  public void test_displayInitialMap() throws IOException, Exception {
    View view = new View(out);
    assertThrows(IOException.class, () -> view.displayInitialMap("Not Valid"));
    String jsonString = "{\"name\":[{\"TerritoryName\":\"Baldwin Auditorium\",\"Neighbors\":\"(next to: Brodie Recreational Center, Smith Warehouse, Duke Chapel)\"}]}";
    view.displayInitialMap(jsonString);
  }

}
