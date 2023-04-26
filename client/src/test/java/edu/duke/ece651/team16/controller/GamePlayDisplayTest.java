package edu.duke.ece651.team16.controller;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;

public class GamePlayDisplayTest {
  @Test
  public void testParseUnitsInfo() {
    GamePlayDisplay display = new GamePlayDisplay();
    String unitsInfo = "1,2,3,4,5,6,7";
    String expected = "First-year: 1, Second-year: 2, Third-Year: 3, Fourth-year: 4, Fifth-year: 5, Sixth-year: 6, Seventh-year: 7";
    String actual = display.parseUnitsInfo(unitsInfo);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetUnitNum() {
    GamePlayDisplay display = new GamePlayDisplay();
    String unitsInfo = "1,2,3,4,5,6,7";
    int expected = 28;
    int actual = display.getUnitNum(unitsInfo);
    assertEquals(expected, actual);
  }

  @Test
  public void testGetUnitNumArray() {
    GamePlayDisplay display = new GamePlayDisplay();
    String unitsInfo = "1,2,3,4,5,6,7";
    ArrayList<Integer> expected = new ArrayList<Integer>();
    expected.add(1);
    expected.add(2);
    expected.add(3);
    expected.add(4);
    expected.add(5);
    expected.add(6);
    expected.add(7);
    ArrayList<Integer> actual = display.getUnitNumArray(unitsInfo);
    assertEquals(expected, actual);
  }

}
