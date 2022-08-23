package model;

import java.io.FileNotFoundException;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class SaveBlackJackGameTest {
    SaveBlackJackGame saver = new SaveBlackJackGame();

    @Test
    @DisplayName("Tester at saveFile ikke utløser et unntak")
    public void testSave() throws Exception {
        Game game = new Game(new Player("Test", 12345));
        game.getGameDeck().clearDeck();
        List<Card> newGameDeck = List.of(new Card('D', 1),new Card('H', 12),new Card('S', 7),new Card('C', 13),new Card('D', 5),new Card('H', 6),new Card('S', 3),new Card('C', 1));
        
        newGameDeck.forEach(c -> game.getGameDeck().addCard(c));

        Assertions.assertDoesNotThrow( () -> {
            saver.saveToFile("TestFile", game);
        });
    }

    @Test
    @DisplayName("Sjekker at innholdet som lagres til filen er det samme som blir hentet.")
    public void testLoad() throws FileNotFoundException, Exception  {

        //Må kjøre metoden testSave for å sørge for at filen som skal leses senere i denne test-metoden eksisterer
        testSave();
        
        Game game = saver.loadGame("TestFile");

        Assertions.assertEquals("Test", game.getUser().getUserName());
        Assertions.assertEquals(12345, game.getUser().getChipCount());

        List<Card> expCards = List.of(new Card('D', 1),new Card('H', 12),new Card('S', 7),new Card('C', 13),new Card('D', 5),new Card('H', 6),new Card('S', 3),new Card('C', 1));
        List<Card> actCards = game.getGameDeck().getGameDeck();

        for (int i = 0; i < expCards.size(); i++) {
            testCard(expCards.get(i), actCards.get(i));
        }
    }

    @Test
    @DisplayName("Tester at du ikke kan laste en fil som ikke finnes.")
    public void testFileDoesentExist() {
        Assertions.assertThrows(FileNotFoundException.class, () -> {
            saver.loadGame("FinnesIkke");
        }, "Når filen ikke finnes vil FileNotFoundException bli utløst");
    }

    private void testCard(Card card1, Card card2) {
		Assertions.assertEquals(card1.getFace(), card2.getFace());
        Assertions.assertEquals(card1.getSuit(), card2.getSuit());
	}
}
