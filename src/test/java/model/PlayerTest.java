package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlayerTest {

    @Test
	@DisplayName("Tester at feltene i klassen er satt til private")
	public void testPrivateFields() {
		PrivateFieldTester.checkPrivateFields(Player.class);
	}

    @Test
    @DisplayName("Sjekker at konstruktøren følger ønsket oppførsel")
    public void testConstructor() {
        Player player1 = new Player("Anders", 124);
        Assertions.assertEquals("Anders", player1.getUserName());
        Assertions.assertEquals(124, player1.getChipCount());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Player("", 100);
        }, "Kan ikke opprette en spiller uten navn.");
        Assertions.assertThrows(NullPointerException.class, () -> {
            new Player(null, 100);
        }, "Kan ikke opprette en spiller uten navn.");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Player("Jens", -10);
        }, "Det er ikke mulig å ha negativ verdi på kontoen.");
    }

    @Test
    @DisplayName("Sjekker at updatePlayerHand fungerer slik den skal.")
    public void testUpdatePlayerHand() {
        Player player = new Player("Anders", 100);
        Assertions.assertThrows(IllegalStateException.class, () -> {
            player.updatePlayerHand(null);
        }, "Det er ikke mulig å sette null som en hånd");

        PlayerHand playerHand = new PlayerHand(new GameDeck());
        player.updatePlayerHand(playerHand);
        Assertions.assertEquals(playerHand, player.getPlayerHand());
    }

    @Test
    @DisplayName("Sjekker at updateChipCount lar deg legge til og ta ut penger.") 
    public void testUpdateChipCount() {
        Player player = new Player("Anders", 100);
        Assertions.assertEquals(100, player.getChipCount());
        player.updateChipCount(100);
        Assertions.assertEquals(200, player.getChipCount());
        player.updateChipCount(-100);
        Assertions.assertEquals(100, player.getChipCount());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            player.updateChipCount(-101);;
        }, "Det er ikke mulig å ta ut mer enn det man har på kontoen");
    }
}
