package edu.duke.ece651.team16.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class ViewsTest {
  @Test
  public void test_displaymap() {
    // make a sample hashmap of territories
    HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    ArrayList<String> list = new ArrayList<String>();
    list.add("Oz");
    list.add("Narnia");
    map.put("Green xx", list);


    Views v = new Views();
    String mapdisplay = v.displayMap(map);
    String expected = "Green xx player: \n" +
    "-----------------\n" +
    "Oz\n" +
    "Narnia\n\n";
    assertEquals(mapdisplay, expected);
  }

}
