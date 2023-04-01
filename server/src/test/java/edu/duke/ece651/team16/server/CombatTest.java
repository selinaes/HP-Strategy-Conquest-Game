package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class CombatTest {

  @Test
  public void testDetermineWin() {
    AdvancedUnit attacker = mock(AdvancedUnit.class);
    AdvancedUnit defender = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    Combat c1 = new Combat();
    int i = 15;
    while (i > 0) {
      c1.determineWin(attacker, defender);
      --i;
    }
  }

  @Test
  public void testDetermineWin_DefenderWins() {
    AdvancedUnit attacker = mock(AdvancedUnit.class);
    AdvancedUnit defender = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    Combat c = new Combat();
    c.setSeed(16807);
    c.setDiceNum(10);
    assertEquals(defender, c.determineWin(attacker, defender));
  }

  @Test
  public void testDetermineWin_TieAndBothAttackers() {
    AdvancedUnit attacker1 = mock(AdvancedUnit.class);
    AdvancedUnit attacker2 = mock(AdvancedUnit.class);
    when(attacker1.getisAttacker()).thenReturn(true);
    when(attacker2.getisAttacker()).thenReturn(true);
    Combat c = new Combat();
    c.setSeed(16807);
    c.setDiceNum(10);
    assertEquals(null, c.determineWin(attacker1, attacker2));
  }

  @Test
  public void testDetermineWin_TieAndAIsNotAttacker() {
    AdvancedUnit defender = mock(AdvancedUnit.class);
    AdvancedUnit attacker = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    Combat c = new Combat();
    c.setSeed(16807);
    c.setDiceNum(10);
    assertEquals(defender, c.determineWin(defender, attacker));
  }

  @Test
  public void testDetermineWin2() {
    AdvancedUnit defender = mock(AdvancedUnit.class);
    AdvancedUnit attacker = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    Combat c = new Combat();
    c.setSeed(123);
    c.setDiceNum(100);
    c.determineWin(attacker, defender);
  }

  @Test
  public void testDetermineWin3() {
    AdvancedUnit defender = mock(AdvancedUnit.class);
    AdvancedUnit attacker = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    Combat c = new Combat();
    c.setSeed(1234);
    c.setDiceNum(10);
    assertEquals(defender, c.determineWin(attacker, defender));
  }

}
