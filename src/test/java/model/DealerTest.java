package model;

import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DealerTest {
    private Dealer dealer;
    private GameDeck gameDeck;

    @BeforeEach
    public void start() {
        gameDeck = new GameDeck();
        gameDeck.clearDeck();
        //Endrer kortstokken slik at man vet hvilke verdier som kommer når man utfører de ulike metode-kallene, og kan dermed forutse de ulike tilstandene som skal dukke opp.
        List<Card> newGameDeck = List.of(new Card('D', 3),new Card('H', 4),new Card('S', 7),new Card('C', 13),new Card('D', 5),new Card('H', 6),new Card('S', 3),new Card('C', 1));
        newGameDeck.forEach(c -> gameDeck.addCard(c));
        dealer = new Dealer(gameDeck);
    }

    @Test
	@DisplayName("Tester at feltene i klassen er satt til private")
	public void testPrivateFields() {
		PrivateFieldTester.checkPrivateFields(Dealer.class);
	}

    @Test
    @DisplayName("Tester at konstruktøren fungerer som den skal.")
    public void testConstructor() {
        Assertions.assertEquals(2, dealer.getDealerHand().size());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Dealer(null);
        });
    }

    @Test
    @DisplayName("Tester at dealer ikke hitter når bruker får blackjack")
    public void testDealerHitWhenUserBlackJack() {
        dealer.updateUserHandScore(21);
        dealer.setUserHandSize(2);
        dealer.dealerHit(gameDeck);

        Assertions.assertEquals(2, dealer.getDealerHand().size());
    }

    @Test
    @DisplayName("Sjekker at dealer hiter, når dealer-score er under 17. Samt at dem stopper siden den går over 21")
    public void testHitWhenUnder17() {
        dealer.updateUserHandScore(dealer.getDealerHandValue());
        dealer.dealerHit(gameDeck);
        
        Assertions.assertEquals(4, dealer.getDealerHand().size());
    }

    @Test
    @DisplayName("Test at dealer ikke hitter hvis den allerede har vunnet")
    public void testHitWhenWin() {
        dealer.updateUserHandScore(23);
        dealer.dealerHit(gameDeck);

        Assertions.assertEquals(2, dealer.getDealerHand().size());
    }

    @Test
    @DisplayName("Test at man får riktig verdi når man skal hente førstekortverdien til å vise til brukeren")
    public void testGetDealerFirstCard() {
        Assertions.assertEquals(4, dealer.getDealerFirstCardValue());

        //Gjør at dealer hitter to ganger. Sjekk testen testHitWhenUnder17.
        dealer.updateUserHandScore(dealer.getDealerHandValue());
        dealer.dealerHit(gameDeck);

        //Skal fortsatt få det samme kortet.
        Assertions.assertEquals(4, dealer.getDealerFirstCardValue());
    }
}
