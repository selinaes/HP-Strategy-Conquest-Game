package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

public class CombatTest {

  // @Test
  // public void testDetermineWin() {
  // AdvancedUnit attacker = mock(AdvancedUnit.class);
  // AdvancedUnit defender = mock(AdvancedUnit.class);
  // when(attacker.getisAttacker()).thenReturn(true);
  // when(defender.getisAttacker()).thenReturn(false);
  // Combat c1 = new Combat();
  // int i = 15;
  // while (i > 0) {
  // c1.determineWin(attacker, defender);
  // --i;
  // }
  // }

  @Test
  public void testDetermineWin() {
    Player playerA = mock(Player.class);
    Player playerB = mock(Player.class);
    when(playerA.getDiceAdvantageSwitch()).thenReturn(false);
    when(playerB.getDiceAdvantageSwitch()).thenReturn(false);

    AdvancedUnit attacker = mock(AdvancedUnit.class);
    AdvancedUnit defender = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    when(attacker.getOwner()).thenReturn(playerA);
    when(defender.getOwner()).thenReturn(playerB);

    Combat c1 = new Combat();
    int i = 15;
    while (i > 0) {
      c1.determineWin(attacker, defender);
      --i;
    }
  }

  @Test
  public void testDetermineWin_DefenderWins() {
    Player playerA = mock(Player.class);
    Player playerB = mock(Player.class);
    when(playerA.getDiceAdvantageSwitch()).thenReturn(false);
    when(playerB.getDiceAdvantageSwitch()).thenReturn(false);

    AdvancedUnit attacker = mock(AdvancedUnit.class);
    AdvancedUnit defender = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    when(attacker.getOwner()).thenReturn(playerA);
    when(defender.getOwner()).thenReturn(playerB);
    Combat c = new Combat();
    c.setSeed(16807);
    c.setDiceNum(10);
    assertEquals(defender, c.determineWin(attacker, defender));
  }

  @Test
  public void testDetermineWin_TieAndBothAttackers() {
    Player playerA = mock(Player.class);
    Player playerB = mock(Player.class);
    when(playerA.getDiceAdvantageSwitch()).thenReturn(false);
    when(playerB.getDiceAdvantageSwitch()).thenReturn(false);
    AdvancedUnit attacker1 = mock(AdvancedUnit.class);
    AdvancedUnit attacker2 = mock(AdvancedUnit.class);
    when(attacker1.getisAttacker()).thenReturn(true);
    when(attacker2.getisAttacker()).thenReturn(true);
    when(attacker1.getOwner()).thenReturn(playerA);
    when(attacker2.getOwner()).thenReturn(playerB);
    Combat c = new Combat();
    c.setSeed(16807);
    c.setDiceNum(10);
    assertEquals(null, c.determineWin(attacker1, attacker2));
  }

  @Test
  public void testDetermineWin_TieAndAIsNotAttacker() {

    AdvancedUnit defender = mock(AdvancedUnit.class);
    AdvancedUnit attacker = mock(AdvancedUnit.class);
    Player playerA = mock(Player.class);
    Player playerB = mock(Player.class);
    when(playerA.getDiceAdvantageSwitch()).thenReturn(false);
    when(playerB.getDiceAdvantageSwitch()).thenReturn(false);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    when(attacker.getOwner()).thenReturn(playerA);
    when(defender.getOwner()).thenReturn(playerB);
    Combat c = new Combat();
    c.setSeed(16807);
    c.setDiceNum(10);
    assertEquals(defender, c.determineWin(defender, attacker));
  }

  @Test
  public void testDetermineWin2() {
    Player playerA = mock(Player.class);
    Player playerB = mock(Player.class);
    when(playerA.getDiceAdvantageSwitch()).thenReturn(false);
    when(playerB.getDiceAdvantageSwitch()).thenReturn(false);

    AdvancedUnit defender = mock(AdvancedUnit.class);
    AdvancedUnit attacker = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    when(attacker.getOwner()).thenReturn(playerA);
    when(defender.getOwner()).thenReturn(playerB);
    Combat c = new Combat();
    c.setSeed(123);
    c.setDiceNum(100);
    c.determineWin(attacker, defender);
  }

  @Test
  public void testDetermineWin3() {
    Player playerA = mock(Player.class);
    Player playerB = mock(Player.class);
    when(playerA.getDiceAdvantageSwitch()).thenReturn(false);
    when(playerB.getDiceAdvantageSwitch()).thenReturn(false);
    AdvancedUnit defender = mock(AdvancedUnit.class);
    AdvancedUnit attacker = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    when(attacker.getOwner()).thenReturn(playerA);
    when(defender.getOwner()).thenReturn(playerB);
    Combat c = new Combat();
    c.setSeed(1234);
    c.setDiceNum(10);
    assertEquals(defender, c.determineWin(attacker, defender));
  }

  @Test
  public void testDetermineWin_DiceAdvantageSwitch() {
    Player playerA = mock(Player.class);
    Player playerB = mock(Player.class);
    when(playerA.getDiceAdvantageSwitch()).thenReturn(true);
    when(playerB.getDiceAdvantageSwitch()).thenReturn(false);

    AdvancedUnit attacker = mock(AdvancedUnit.class);
    AdvancedUnit defender = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    when(attacker.getOwner()).thenReturn(playerA);
    when(defender.getOwner()).thenReturn(playerB);

    Combat c1 = new Combat();
    c1.setSeed(1234); // Set a fixed seed value for reproducible results
    c1.setDiceNum(10);

    c1.determineWin(attacker, defender);
    // Reset the standard output stream
    System.setOut(System.out);
  }

  @Test
  public void testDetermineWin_DiceAdvantageSwitch1() {
    Player playerA = mock(Player.class);
    Player playerB = mock(Player.class);
    when(playerA.getDiceAdvantageSwitch()).thenReturn(false);
    when(playerB.getDiceAdvantageSwitch()).thenReturn(true);

    AdvancedUnit attacker = mock(AdvancedUnit.class);
    AdvancedUnit defender = mock(AdvancedUnit.class);
    when(attacker.getisAttacker()).thenReturn(true);
    when(defender.getisAttacker()).thenReturn(false);
    when(attacker.getOwner()).thenReturn(playerA);
    when(defender.getOwner()).thenReturn(playerB);

    Combat c1 = new Combat();
    c1.setSeed(1234); // Set a fixed seed value for reproducible results
    c1.setDiceNum(10);

    c1.determineWin(attacker, defender);
    // Reset the standard output stream
    System.setOut(System.out);
  }
}