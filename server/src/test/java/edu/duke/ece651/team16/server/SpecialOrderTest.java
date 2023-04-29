package edu.duke.ece651.team16.server;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SpecialOrderTest {

  @Test
  public void testTryAction() {
    // Create mock Player and Territory objects
    Player player = mock(Player.class);
    Territory target = mock(Territory.class);

    // Test "Double Resource Production" option
    SpecialOrder doubleResourceOrder = new SpecialOrder(player, "Double Resource Production");
    assertNull(doubleResourceOrder.tryAction());
    verify(player).setDoubleResourceSwitch(true);

    // Test "Two Units Generation" option
    SpecialOrder twoUnitsOrder = new SpecialOrder(player, "Two Units Generation");
    assertNull(twoUnitsOrder.tryAction());
    verify(player).setMoreUnitSwitch(true);

    // Test "Disregard Adjacency" option
    SpecialOrder disregardAdjacencyOrder = new SpecialOrder(player, "Disregard Adjacency");
    assertNull(disregardAdjacencyOrder.tryAction());
    verify(player).setDisregardAdjacencySwitch(true);

    // Test "Dice Advantage" option
    SpecialOrder diceAdvantageOrder = new SpecialOrder(player, "Dice Advantage");
    assertNull(diceAdvantageOrder.tryAction());
    verify(player).setDiceAdvantageSwitch(true);

    // Test "Fiendfyre" option with enemy territory
    when(target.getOwner()).thenReturn(null);
    SpecialOrder fiendfyreOrder = new SpecialOrder(player, "Fiendfyre", target);
    assertNull(fiendfyreOrder.tryAction());
    verify(target).placeBomb(player);

    // Test "Fiendfyre" option with own territory
    when(target.getOwner()).thenReturn(player);
    fiendfyreOrder = new SpecialOrder(player, "Fiendfyre", target);
    assertEquals("You can not Fiendfyre your own territory", fiendfyreOrder.tryAction());

    SpecialOrder otherOrder = new SpecialOrder(player, "fsafa", target);
    otherOrder.tryAction();
  }
}
