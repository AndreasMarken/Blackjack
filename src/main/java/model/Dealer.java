package model;

import java.util.ArrayList;
import java.util.List;

public class Dealer {
    private int userHandScore;
    private List<Card> dealerHand = new ArrayList<>();
    private int acesInHand;
    private int dealerHandValue;
    private int userHandSize;

    public Dealer(GameDeck gameDeck) {
        if (gameDeck == null) {
            throw new IllegalArgumentException("Kan ikke opprette en dealer uten en kortstokk");
        }
        dealerHand.add(gameDeck.getFirstCardAndRemoveCardFromDeck());
        dealerHand.add(gameDeck.getFirstCardAndRemoveCardFromDeck());
        this.updateDealerHandValue();
    }

    public List<Card> getDealerHand() {
        return new ArrayList<>(this.dealerHand);
    }

    //Kortene 2-10 er verdt sin face-value. Knekt, dronning og konge er verdt 10. Et ess har verdien 1 eller 11 avhengig av hva som er best for hånden. Hvis man går bust, men har ess i hånda, vil verdien kunne endres fra 11 til 1, og brukeren er dermed ikke bust lenger.
    private void updateDealerHandValue() {
        int tmp = 0;
        int aces = 0;
        for (Card card : dealerHand) {
            if (card.getFace() >= 2 && card.getFace() < 10) {
                tmp += card.getFace();
            } else if (card.getFace() > 9) {
                tmp += 10;
            } else if (card.getFace() == 1) {
                tmp += 11; 
                aces += 1;
            }
        }
        acesInHand = aces;
        for (int i = 0; i < acesInHand; i++) {
            if (tmp > 21) {
                tmp -= 10;
                aces--;
            }
        }
        acesInHand = aces;
        dealerHandValue = tmp;
    }

    public int getDealerHandValue() {
        return dealerHandValue;
    }

    //Gjør det sånn at man kun kan se det ene kortet til dealeren. Man kan kun se det ene når man hitter.
    public int getDealerFirstCardValue() {
        int dealerFirstCard = this.getDealerHand().get(1).getFace();
        if (dealerFirstCard < 11 && dealerFirstCard > 1) {
            return dealerFirstCard;
        } else if (dealerFirstCard == 1) {
            return 11;
        } else {
            return 10;
        }
    }

    private int getUserHandScore() {
        return userHandScore;
    }

    public void dealerHit(GameDeck gameDeck) {
        //En dealer skal alltid hitte så lenge kravene satt ut ifra reglene er oppfyllt. Så lenge disse kravene er sanne, hitter dealer, såfremt det ikke er en tom kortstokk. Da utløses EmptyGameDeckException og hovedprogrammet håndterer hva som da skal skje.
        while (checkIfDealerHit() == true) {
            if (gameDeck.getSize()==0) {
                throw new EmptyGameDeckException("Kan ikke hitte med tom kortstokk.");
            }
            Card card = gameDeck.getFirstCardAndRemoveCardFromDeck();
            dealerHand.add(card);
            updateDealerHandValue();
        }
    }

    private boolean checkIfDealerHit() {
        //Hvis brukeren fikk blackjack har den automatisk vunnet
        if (getUserHandScore() == 21 && userHandSize == 2) {
            return false;
        //Hvis brukeren har over 21 er den bust, og dealer trenger ikke hitte.
        } else if (getUserHandScore() > 21) {
            return false;
        //Hvis dealer er bust, eller har fått 21, skal den ikke hitte.
        } else if (getDealerHandValue() >= 21) {
            return false;
        //Hvis ingen av reglene ovenfor har intruffet, må en dealer hitte såfremt den er under 17. Med en gang en dealer har en score over 17 kan den ikke hitte mer, uansett hvordan brukerhånden ser ut.
        } else if (getDealerHandValue() < 17) {
            return true;
        }
        //Ved resten av tilfellene nå: dealer-score = bruker-score, eller dealerScore er større enn brukerscore; skal ikke dealer hitte.
        return false;
    }

    public void updateUserHandScore(int n) {
        this.userHandScore = n;
    }

    public int getAcesInHand() {
        return this.acesInHand;
    }

    //toString-metoden som brukes til å vise kortene i brukergrensesnittet. Henter kort nummer n, og 
    public String toString(int n) {
        if (n>dealerHand.size()) {
            throw new IllegalArgumentException("Kan ikke hente et kort med høyere index enn det er kort i kortstokken.");
        }
        return getDealerHand().get(n).toString();
    }
    
    public void setUserHandSize(int userHandSize) {
        this.userHandSize = userHandSize;
    }

}
