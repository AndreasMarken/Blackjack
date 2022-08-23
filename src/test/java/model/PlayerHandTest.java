package model;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PlayerHandTest {
    private PlayerHand playerHand;
    private GameDeck gameDeck;

    @BeforeEach
    public void start() {
        gameDeck = new GameDeck();
        gameDeck.clearDeck();
        //Endrer kortstokken slik at man vet hvilke verdier som kommer når man utfører de ulike metode-kallene, og kan dermed forutse de ulike tilstandene som skal dukke opp.
        List<Card> newGameDeck = List.of(new Card('D', 3),new Card('H', 4),new Card('S', 7),new Card('C', 13),new Card('D', 5),new Card('H', 6),new Card('S', 3),new Card('C', 1));
        newGameDeck.forEach(c -> gameDeck.addCard(c));
        playerHand = new PlayerHand(gameDeck);
    }

    @Test
	@DisplayName("Tester at feltene i klassen er satt til private")
	public void testPrivateFields() {
		PrivateFieldTester.checkPrivateFields(PlayerHand.class);
	}

    @Test
    @DisplayName("Tester at konstruktøren oppretter et objekt med riktige verdier.")
    public void testConstructor() {
        Assertions.assertEquals(2, playerHand.getPlayerHandSize());
        Assertions.assertEquals(7, playerHand.getPlayerHandScore());
        Assertions.assertEquals(6, gameDeck.getSize());
    }

    @Test
    @DisplayName("Tester at bruker kan hite to ganger, før den vil gå bust.")
    public void testHit() {
        playerHand.hit(gameDeck);
        Assertions.assertEquals(14, playerHand.getPlayerHandScore());
        Assertions.assertEquals(5, gameDeck.getSize());

        playerHand.hit(gameDeck);
        Assertions.assertEquals(24, playerHand.getPlayerHandScore());
        Assertions.assertEquals(4, gameDeck.getSize());

        Assertions.assertThrows(IllegalStateException.class, () -> {
            playerHand.hit(gameDeck);
        }, "Når du er over 21 kan du ikke hitte.");
    }
}
