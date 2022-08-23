package model;

import java.util.ArrayList;
import java.util.List;


public class PlayerHand {
    private List<Card> playerHand = new ArrayList<>();
    private int acesInHand;
    private int userHandScore;

    public PlayerHand(GameDeck gameDeck) {
        //Man starter alltid med to kort i en hånd.
        playerHand.add(gameDeck.getFirstCardAndRemoveCardFromDeck());
        playerHand.add(gameDeck.getFirstCardAndRemoveCardFromDeck());
        updateUserHandScore(playerHand.get(0).getFace());
        updateUserHandScore(playerHand.get(1).getFace());
    }

    public List<Card> getPlayerHand() {
        return this.playerHand;
    }

    public void hit(GameDeck gameDeck) {
        //Hvis man allerede er bust og prøver å hitte, vil dette være å prøve å fortsette med et spill som vil være i feil tilstand.
        checkIfBust();

        //Dersom man ikke allerede er bust, legger man til et kort i hånda, og oppdaterer "scoren" sin med face-valuen til dette kortet.
        Card cardToAdd = gameDeck.getFirstCardAndRemoveCardFromDeck();
        playerHand.add(cardToAdd);
        updateUserHandScore(cardToAdd.getFace());
    }

    //Kortene 2-10 er verdt sin face-value. Knekt, dronning og konge er verdt 10. Et ess har verdien 1 eller 11 avhengig av hva som er best for hånden. Hvis man går bust, men har ess i hånda, vil verdien kunne endres fra 11 til 1, og brukeren er dermed ikke bust lenger.
    private void updateUserHandScore(int n) {
        if (n >= 2 && n < 10) {
            this.userHandScore += n;
        } else if (n > 9) {
            this.userHandScore += 10;
        } else if (n == 1) {
            this.userHandScore += 11;
            acesInHand += 1;
        }
        for (int i = 0; i < acesInHand; i++) {
            if (userHandScore > 21) {
                userHandScore -= 10;
                acesInHand--;
            }
        }
    }

    //En spiller er bust når man får en hånd høyere enn 21.
    private void checkIfBust() {
        if (userHandScore > 21) {
            throw new IllegalStateException("Bust!");
        } 
    }

    public int getPlayerHandScore() {
        return this.userHandScore;
    }

    public int getPlayerHandSize() {
        return this.playerHand.size();
    }

    public Card toString(int n) {
        return this.playerHand.get(n);
    }  
}
