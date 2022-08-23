package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class GameTest {
    Player player;
    Game game;
    
    @BeforeEach
    public void start() {
        player = new Player("John", 1000);
        game = new Game(player);
    }
    
    @Test
	@DisplayName("Tester at feltene i klassen er satt til private")
	public void testPrivateFields() {
		PrivateFieldTester.checkPrivateFields(Game.class);
	}

    @Test
    @DisplayName("Tester konstruktøren")
    public void testConstructor() {

        //Tester først at konstruktøren fungerer slik den skal
        Assertions.assertEquals(player, game.getUser());
        Assertions.assertEquals(0, game.getBet());
        Assertions.assertEquals(null, game.getDealer());
        Assertions.assertEquals(312, game.getGameDeck().getSize());

        //Prøve å sette spillet i feil tilstand ved ugyldig argument.
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new Game(null);
        }, "Kan ikke starte et spill uten en spiller.");
    }

    @Test
    @DisplayName("Tester at man ikke kan sette en ny spiller som ikke eksisterer. Setter spillet i feil tilstand, ved ugyldige argumenter.")
    public void testSetUser() {
        game.setUser("Test", 500000);
        Assertions.assertEquals("Test", game.getUser().getUserName());
        Assertions.assertEquals(500000, game.getUser().getChipCount());

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.setUser(null, 20);
        },"Man må ha et brukernavn når man oppretter en spiller.");
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.setUser("Test", -10);
        }, "Kan ikke ha negativ sum på konto.");
    }

    @Test
    @DisplayName("Tester at tilstanden i spillet blir riktig når man oppretter en dealer.")
    public void testCreateDealer() {
        game.createDealer();

        Assertions.assertEquals(2, game.getDealer().getDealerHand().size());
        Assertions.assertTrue(game.getDealer().getDealerHandValue() != 0);

        //Tester at det skal være mulig å lage en ny dealer selv med tom kortstokk. Da skal spillet lage ny kortstokk.
        Assertions.assertDoesNotThrow(() -> {
            game.getGameDeck().clearDeck();
            game.createDealer();
        });
        }

    @Test
    @DisplayName("Tester at man kan generere en player hand og at dette blir gjort på riktig måte.")
    public void testGeneratePlayerHand() {
        game.generatePlayerHand();

        Assertions.assertEquals(2, game.getPlayerHand().getPlayerHandSize());
        Assertions.assertTrue(game.getPlayerHand().getPlayerHandScore() != 0);

        //Tester at det er mulig å lage ny playerhand selv om kortstokken er tom.
        Assertions.assertDoesNotThrow(() -> {
            game.getGameDeck().clearDeck();
            game.generatePlayerHand();
        });
    }

    @Test
    @DisplayName("Tester at metoden deal starter spillet og setter det i riktig tilstand")
    public void testDeal() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.deal();
        }, "Kan ikke deale ut kortene uten å ha betta.");

        game.setBet(10);
        game.deal();
        
        Assertions.assertEquals(2, game.getPlayerHand().getPlayerHandSize());
        Assertions.assertEquals(2, game.getDealer().getDealerHand().size());

        
    }

    @Test
    @DisplayName("Test at blackjack er mulig å få når man kjører deal metoden")
    public void testBlackJack() {
        game.setBet(10);
        for (int i = 0; i < 1000; i++) {
            //Hvis spilleren får blackjack
            if (game.deal()) {
                System.out.println("GameTest: Fikk blackjack på forsøk nummer "+ i);
                Assertions.assertEquals(0, game.getBet());
                Assertions.assertEquals(1010, game.getUser().getChipCount());
                break;
            }
        } 
    }

    @Test
    @DisplayName("Tester at metoden som skal la brukeren hitte fungerer slik den skal")
    public void testHit() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.hit();
        }, "Kan ikke hitte uten å ha betta.");

        game.setBet(10);

        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.hit();
        }, "Kan ikke hitte uten å ha deala ut kort først.");

        game.deal();
        //Hvis bruker har fått blackjack så kan man ikke kjøre metoden hit. Dette må derfor gjøres en sjekk på.
        if (game.getPlayerHandScore() != 21) {
            System.out.println("Bruker fikk ikke blackjack.");
            game.hit();

            //Dersom bruker ikke fikk blackjack, kan man hitte, og størrelsen på hånda skal være 3.
            Assertions.assertEquals(3, game.getPlayerHand().getPlayerHandSize());
        }
        
        for (int i = 0; i < 100; i++) {
            //Kjører denne for-løkka for å sørge for å finne en hånd der bruker ikke får blackjack. Det gjøres for å sikre at neste test kjøres riktig.
            game.setBet(10);
            game.deal();
            if (game.getPlayerHandScore() != 21) break;
        }
        
        Assertions.assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i < 30; i++) {
                game.hit();
            }
        },"Kan ikke fortsette å hitte i evig tid, etterhvert vil man gå bust");
    }

    @Test
    @DisplayName("Tester at metoden stand får dealer til å begynne å hitte, før den finner ut av hvem som er vinner, og så returnerer denne vinnerverdien.")
    public void testStand() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            game.stand();
        }, "Kan ikke kjøre metoden stand før både dealer og spiller har fått en hånd.");

        game.setBet(10);
        game.deal();
        int n = game.stand();

        if (n == 1) {
            System.out.println("Bruker vant runden.");

            Assertions.assertTrue(game.getPlayerHandScore() > game.getDealer().getDealerHandValue() || game.getDealer().getDealerHandValue() > 21);
            Assertions.assertEquals(1010, game.getUser().getChipCount());
        } else if (n == 0) {
            System.out.println("Uavgjort.");

            Assertions.assertTrue(game.getPlayerHandScore()==game.getDealer().getDealerHandValue());
            Assertions.assertEquals(1000, game.getUser().getChipCount());
        } else {
            System.out.println("Dealer vinner.");
            Assertions.assertTrue(game.getPlayerHandScore()<game.getDealer().getDealerHandValue() && game.getDealer().getDealerHandValue() < 22);
            Assertions.assertEquals(990, game.getUser().getChipCount());
        }

        Assertions.assertEquals(0, game.getBet());
    }
}
