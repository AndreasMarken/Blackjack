package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CardTest {

	private void testCard(Card card, char suit, int face) {
		Assertions.assertEquals(card.getSuit(), suit);
		Assertions.assertEquals(card.getFace(), face);
	}

	@Test
	@DisplayName("Tester at feltene i klassen er satt til private")
	public void testPrivateFields() {
		PrivateFieldTester.checkPrivateFields(Card.class);
	}

	@Test
	@DisplayName("Sjekk at det går å lage gyldige verdier av kort.")
	public void testConstructor() {
		testCard(new Card('S', 4), 'S', 4);
		testCard(new Card('H', 1), 'H', 1);
		testCard(new Card('H', 8), 'H', 8);
		testCard(new Card('S', 13), 'S', 13);
		testCard(new Card('D', 3), 'D', 3);
		testCard(new Card('D', 12), 'D', 12);
		testCard(new Card('C', 1), 'C', 1);
		testCard(new Card('C', 13), 'C', 13);
	}

    @Test
    @DisplayName("Sjekker at det ikke går å lage ugyldige kort.")
    public void testCardInput() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Card('Y', 4);
		}, "Kan ikke lage kort med suit Y.");

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Card('D', 0);
		}, "Skal ikke kunne lage et kort med verdi 0");

		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new Card('H', 14);
		}, "Skal ikke kunne lage et kort med verdi 14");
    }
}

