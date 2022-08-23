package model;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameDeckTest {
    private GameDeck gameDeck;

    private void testCard(Card card, char suit, int face) {
		Assertions.assertEquals(card.getSuit(), suit);
		Assertions.assertEquals(card.getFace(), face);
	}
    
    @BeforeEach
    public void start() {
        gameDeck = new GameDeck();
    }

    @Test
	@DisplayName("Tester at feltene i klassen er satt til private")
	public void testPrivateFields() {
		PrivateFieldTester.checkPrivateFields(GameDeck.class);
	}

    @Test
    @DisplayName("Test at konstruktøren oppretter en riktig størrelse på kortstokken.")
    public void testConstructor() {
        Assertions.assertTrue(gameDeck.getSize() == 312);
    }

    @Test
    @DisplayName("Sjekk at det går å slette og lage legge til nye kortstokker og kort i gamedecken")
    public void testNewGameDeck() {
        Assertions.assertTrue(gameDeck.getSize() == 312);
        gameDeck.newGameDeck();
        Assertions.assertTrue(gameDeck.getSize() == 624);
        gameDeck.clearDeck();
        Assertions.assertTrue(gameDeck.getSize() == 0);

        List<Card> cards = List.of(new Card('H', 4),new Card('H', 7),new Card('C', 2),new Card('D', 1),new Card('C', 7),new Card('S', 12),new Card('S', 9));
        cards.forEach(c -> gameDeck.addCard(c));
        Assertions.assertTrue(gameDeck.getSize() == 7);
    }

    @Test
    @DisplayName("Sjekker at man kan fjerne et kort, få det returnert og at det dermed ikke er i kortstokken lenger")
    public void testGetFirstCardAndRemoveFromDeck() {
        testCard(gameDeck.getCard(0), 'H', 1);
        Assertions.assertTrue(gameDeck.getSize()== 312);
        testCard(gameDeck.getFirstCardAndRemoveCardFromDeck(), 'H', 1);
        Assertions.assertTrue(gameDeck.getSize()== 311);

        gameDeck.clearDeck();
        Assertions.assertThrows(EmptyGameDeckException.class, () -> {
			gameDeck.getFirstCardAndRemoveCardFromDeck();
		}, "Kan ikke hente et kort hvis kortstokken er tom.");
    }
}
