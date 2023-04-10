package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.util.*;
import static org.mockito.Mockito.mock;

public class AttackAdjacentRuleCheckerTest {
  @Test
  public void test_AttackAdjacentRuleChecker() {
    AttackAdjacentRuleChecker attackAdjacentRuleChecker = new AttackAdjacentRuleChecker(mock(OrderRuleChecker.class));
    Player p1 = mock(Player.class);
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    Territory t3 = new Territory("t3");
    List<Territory> t1Neighbor = new ArrayList<Territory>();
    t1Neighbor.add(t2);
    t1.setNeighbors(t1Neighbor);
    assertEquals(null, attackAdjacentRuleChecker.checkMyRule(t1, t2, p1, 1, mock(GameMap.class), 0));
    assertEquals("Target is not adjcent.",
        attackAdjacentRuleChecker.checkMyRule(t1, t3, p1, 1, mock(GameMap.class), 0));
  }

}
