package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import java.lang.reflect.Field;
import java.util.ArrayList;

import org.junit.jupiter.api.Test;

public class BattleTest {

  @Test
  public void testAddGroup() {
    Battle battle = new Battle();
    ArrayList<Unit> units = new ArrayList<Unit>();
    Player p1 = mock(Player.class);
    when(p1.getColor()).thenReturn("p1");
    BasicUnit unit = new BasicUnit(p1, mock(Territory.class), true, 0);
    units.add(unit);
    battle.addGroup(units);
    assertEquals(1, battle.getParties().size());
  }

  @Test
  public void testResolveBattle() {
    Battle battle = new Battle();
    ArrayList<Unit> partyA = new ArrayList<Unit>();
    ArrayList<Unit> partyB = new ArrayList<Unit>();
    ArrayList<Unit> partyC = new ArrayList<Unit>();
    ArrayList<Unit> partyD = new ArrayList<Unit>();
    ArrayList<Unit> partyE = new ArrayList<Unit>();
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
    BasicUnit unit1 = new BasicUnit(p1, mock(Territory.class), true, 0);
    BasicUnit unit2 = new BasicUnit(p1, mock(Territory.class), true, 0);
    partyA.add(unit1);
    partyA.add(unit2);

    BasicUnit unit3 = new BasicUnit(p1, mock(Territory.class), true, 0);
    BasicUnit unit4 = new BasicUnit(p1, mock(Territory.class), true, 0);
    partyB.add(unit3);
    partyB.add(unit4);

    BasicUnit unit5 = new BasicUnit(p1, mock(Territory.class), true, 0);
    BasicUnit unit6 = new BasicUnit(p1, mock(Territory.class), true, 0);
    partyC.add(unit5);
    partyC.add(unit6);

    BasicUnit unit7 = new BasicUnit(p1, mock(Territory.class), true, 0);
    BasicUnit unit8 = new BasicUnit(p1, mock(Territory.class), true, 0);
    partyD.add(unit7);
    partyD.add(unit8);

    BasicUnit unit9 = new BasicUnit(p5, mock(Territory.class), false, 0);
    partyE.add(unit9);

    battle.addGroup(partyA);
    battle.addGroup(partyB);
    battle.addGroup(partyC);
    battle.addGroup(partyD);
    battle.addGroup(partyE);
    battle.addGroup(partyA);
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

    ArrayList<Unit> partyA = new ArrayList<Unit>();
    ArrayList<Unit> partyB = new ArrayList<Unit>();
    Player p1 = mock(Player.class);
    when(p1.getColor()).thenReturn("p1");
    Player p2 = mock(Player.class);
    when(p2.getColor()).thenReturn("p2");
    BasicUnit unit1 = new BasicUnit(p1, mock(Territory.class), true, 0);
    partyA.add(unit1);
    battle.addGroup(partyA);

    BasicUnit unit3 = new BasicUnit(p2, mock(Territory.class), true, 0);
    partyB.add(unit3);

    battle.addGroup(partyB);
    int i = 50;
    while (i > 0) {
      Player winner = battle.resolveBattle();
      if (winner.equals(p2)) {
        unit1 = new BasicUnit(p1, mock(Territory.class), true, 0);
        partyA.add(unit1);
        battle.addGroup(partyA);
      } else {
        unit3 = new BasicUnit(p2, mock(Territory.class), true, 0);
        partyB.add(unit3);
        battle.addGroup(partyB);
      }
      --i;
    }

    // assertEquals(p1, battle.resolveBattle());
  }

}
