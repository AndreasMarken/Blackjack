package model;

import java.util.Arrays;
import java.util.List;

public class Card {
    private char suit;
    private int face;
    private final List<Character> suits = Arrays.asList('S', 'H', 'D', 'C');

    public Card(char suit, int face) {
        checkSuit(suit);
        checkFace(face);
        this.suit = suit;
        this.face = face;
    }

    public int getFace() {
        return face;
    }

    public char getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return "" + suit + face;
    }

    private void checkSuit(char suit) {
        if (!suits.contains(suit)) {
            throw new IllegalArgumentException("Må velge en av de fire lovlige suitsa.");
        }
    }

    private void checkFace(int face) {
        if (face > 13 || face < 1) {
            throw new IllegalArgumentException("Et kort kan kun være mellom 1 og 13.");
        }
    }

    public static void main(String[] args) {
        Card card = new Card('H', 12);
        System.out.println(card);
    }
}
