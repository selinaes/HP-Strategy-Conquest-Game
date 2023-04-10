package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class BattleTest {

  @Test
  public void testAddGroup() {
    Battle battle = new Battle();
    ArrayList<Unit> units = new ArrayList<Unit>();
    Player p1 = mock(Player.class);
    when(p1.getColor()).thenReturn("p1");
    AdvancedUnit unit = new AdvancedUnit(p1, mock(Territory.class), true, 0);
    units.add(unit);
    battle.addGroup(units);
    assertEquals(1, battle.getParties().size());
  }

  private ArrayList<Unit> makeParty(int num, Player p) {
    ArrayList<Unit> party = new ArrayList<Unit>();
    for (int i = 0; i < num; i++) {
      AdvancedUnit unit = new AdvancedUnit(p, mock(Territory.class), true, 0);
      party.add(unit);
    }
    return party;
  }

  @Test
  public void testResolveBattle() {
    Battle battle = new Battle();
    Player p1 = mock(Player.class);
    when(p1.getColor()).thenReturn("p1");
    Player p2 = mock(Player.class);
    when(p2.getColor()).thenReturn("p2");
    Player p3 = mock(Player.class);
    when(p3.getColor()).thenReturn("p3");
    Player p4 = mock(Player.class);
    when(p4.getColor()).thenReturn("p4");
    Player p5 = mock(Player.class);
    when(p5.getColor()).thenReturn("p5");

    battle.addGroup(makeParty(17, p1));
    battle.addGroup(makeParty(2, p2));
    battle.addGroup(makeParty(5, p3));
    battle.addGroup(makeParty(32, p4));
    battle.addGroup(makeParty(13, p5));
    battle.resolveBattle();
    // assertEquals(p1, battle.resolveBattle());
  }

  @Test
  public void testResolveBattle2() {
    Battle battle = new Battle();
    Player p1 = mock(Player.class);
    when(p1.getColor()).thenReturn("p1");
    Player p2 = mock(Player.class);
    when(p2.getColor()).thenReturn("p2");

    battle.addGroup(makeParty(6, p1));
    battle.addGroup(makeParty(2, p2));
    battle.resolveBattle();
    // assertEquals(p1, battle.resolveBattle());
  }

  @Test
  public void testResolveBattle_Equal() {
    Battle battle = new Battle();
    Combat c = new Combat();
    // c.setSeed(42);
    c.setDiceNum(2);
    battle.setCombat(c);

    Player p1 = mock(Player.class);
    when(p1.getColor()).thenReturn("p1");
    Player p2 = mock(Player.class);
    when(p2.getColor()).thenReturn("p2");

    battle.addGroup(makeParty(3, p1));
    battle.addGroup(makeParty(1, p2));
    int i = 50;
    while (i > 0) {
      Player winner = battle.resolveBattle();
      if (winner.equals(p2)) {
        battle.addGroup(makeParty(1, p1));
      } else {
        battle.addGroup(makeParty(1, p2));
      }
      --i;
    }
  }

  @Test
  public void testCheckGroupExisted() {
    Territory territory = new Territory("Gondor");
    assertFalse(territory.existsBattle());
    Conn c2 = mock(Conn.class);
    List<Territory> list = new ArrayList<Territory>();
    list.add(territory);
    Player player1 = new Player("red", c2, list, 2);

    Territory territory1 = new Territory("Duke");
    Conn c = mock(Conn.class);
    List<Territory> list0 = new ArrayList<Territory>();
    list0.add(territory1);
    Player player2 = new Player("blue", c2, list, 2);

    Unit unit1 = new AdvancedUnit(player1, null, true, 1);
    Unit unit2 = new AdvancedUnit(player2, null, true, 1);

    ArrayList<Unit> units1 = new ArrayList<>();
    units1.add(unit1);
    ArrayList<Unit> units2 = new ArrayList<>();
    units2.add(unit2);
    Battle battlefield = new Battle();
    battlefield.addGroup(units1);
    assertTrue(battlefield.checkGroupExisted(units1));
    assertFalse(battlefield.checkGroupExisted(units2));
  }

  @Test
  public void testFindSmallest() {
    // create an ArrayList of Unit objects to test the method
    // create an ArrayList of Unit objects to test the method
    ArrayList<Unit> party = new ArrayList<Unit>();
    Player p1 = mock(Player.class);
    when(p1.getColor()).thenReturn("p1");
    AdvancedUnit unit = new AdvancedUnit(p1, mock(Territory.class), true, 0);
    AdvancedUnit unit1 = new AdvancedUnit(p1, mock(Territory.class), true, 0);
    AdvancedUnit unit2 = new AdvancedUnit(p1, mock(Territory.class), true, 0);
    unit.upgrade(3);
    unit1.upgrade(2);
    unit2.upgrade(1);

    party.add(unit);
    party.add(unit1);
    party.add(unit2);
    ArrayList<Unit> units1 = new ArrayList<>();
    Battle battlefield = new Battle();
    battlefield.addGroup(units1);

    // create an instance of the Party class and call the findLargest() method
    // Party testParty = new Party();
    Unit smallerUnit = battlefield.findSmallest(party);
  }

  @Test
  public void testFindLargest() {
    // create an ArrayList of Unit objects to test the method
    ArrayList<Unit> party = new ArrayList<Unit>();
    Player p1 = mock(Player.class);
    when(p1.getColor()).thenReturn("p1");
    AdvancedUnit unit = new AdvancedUnit(p1, mock(Territory.class), true, 0);
    AdvancedUnit unit1 = new AdvancedUnit(p1, mock(Territory.class), true, 0);
    AdvancedUnit unit2 = new AdvancedUnit(p1, mock(Territory.class), true, 0);
    unit.upgrade(1);
    unit1.upgrade(2);
    unit2.upgrade(3);

    party.add(unit);
    party.add(unit1);
    party.add(unit2);
    ArrayList<Unit> units1 = new ArrayList<>();
    Battle battlefield = new Battle();
    battlefield.addGroup(units1);

    // create an instance of the Party class and call the findLargest() method
    // Party testParty = new Party();
    Unit largestUnit = battlefield.findLargest(party);
    Unit smallerUnit = battlefield.findSmallest(party);

    // assertEquals(7, largestUnit.getLevel());
  }

}
