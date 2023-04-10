package edu.duke.ece651.team16.server;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import static org.mockito.Mockito.mock;
import org.junit.jupiter.api.Test;

public class MovePathRuleCheckerTest {

  @Test
  public void test_MovePathRuleChecker() {
    /*
     * t1:player1
     * / \
     * t2:player2 t3: player1
     * \ /
     * t4: player2
     * |
     * t5: player1
     */
    HashMap<String, List<Territory>> map = new HashMap<String, List<Territory>>();
    Territory t1 = new Territory("t1");
    Territory t2 = new Territory("t2");
    Territory t3 = new Territory("t3");
    Territory t4 = new Territory("t4");
    Territory t5 = new Territory("t5");
    List<Territory> t1Neighbor = new ArrayList<Territory>();
    t1Neighbor.add(t2);
    t1Neighbor.add(t3);
    t1.setNeighbors(t1Neighbor);

    List<Territory> t2Neighbor = new ArrayList<Territory>();
    t2Neighbor.add(t1);
    t2Neighbor.add(t4);
    t2.setNeighbors(t2Neighbor);

    List<Territory> t3Neighbor = new ArrayList<Territory>();
    t3Neighbor.add(t1);
    t3Neighbor.add(t4);
    t3.setNeighbors(t3Neighbor);

    List<Territory> t4Neighbor = new ArrayList<Territory>();
    t4Neighbor.add(t2);
    t4Neighbor.add(t3);
    t4Neighbor.add(t5);
    t4.setNeighbors(t4Neighbor);

    List<Territory> tPlayer1 = new ArrayList<Territory>();
    tPlayer1.add(t1);
    tPlayer1.add(t3);
    tPlayer1.add(t5);
    List<Territory> tPlayer2 = new ArrayList<Territory>();
    tPlayer2.add(t2);
    tPlayer2.add(t4);

    map.put("player1", tPlayer1);
    map.put("player2", tPlayer2);

    GameMap gameMap = new GameMap(0);
    gameMap.setMap(map);

    Player player1 = new Player("player1", mock(Conn.class), tPlayer1, 0);
    Player player2 = new Player("player2", mock(Conn.class), tPlayer2, 0);

    t1.setOwner(player1);
    t2.setOwner(player2);
    t3.setOwner(player1);
    t4.setOwner(player2);
    t5.setOwner(player1);

    MovePathRuleChecker movePathRuleChecker = new MovePathRuleChecker(mock(OrderRuleChecker.class));
    assertEquals(null, movePathRuleChecker.checkMyRule(t1, t3, player1, 0, gameMap, 0));
    assertEquals("No valid path", movePathRuleChecker.checkMyRule(t2, t5, player2, 0, gameMap, 0));
  }

}
