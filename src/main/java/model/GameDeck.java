package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;

public class GameDeck {
    private List<Card> gameDeck = new ArrayList<>();

    public GameDeck() {
        newGameDeck();
    }

    //En gamedeck i blackjack består av 6 hele kortstokker.
    public void newGameDeck() {
        for (int i = 0; i < 6; i++) {
            for (int j = 1; j < 14; j++) {
                gameDeck.add(new Card('H', j));
                gameDeck.add(new Card('C', j));
                gameDeck.add(new Card('D', j));
                gameDeck.add(new Card('S', j));
            }
        }
    }

    public List<Card> getGameDeck() {
        return gameDeck;
    }

    public void shuffleDeck() {
        //Kjøres 10 ganger for å sørge for at kortstokken er godt stokka.
        for (int i = 0; i < 10; i++) {
            Collections.shuffle(gameDeck);
        }
    }

    public Card getCard(int n) {
        if (n > getSize()-1) {
            throw new IllegalArgumentException("For høy index");
        }
        return gameDeck.get(n);
    }

    public int getSize() {
        return gameDeck.size();
    }

    public Card getFirstCardAndRemoveCardFromDeck() {
        if (getSize() == 0) {
            throw new EmptyGameDeckException("Tom kortstokk");
        }
        Card first = getCard(0);
        gameDeck.remove(0);
        return first;
    }

    public void clearDeck() {
        this.gameDeck.clear();
    }
    
    public void addCard(Card card) {
        this.gameDeck.add(card);
    }
}
