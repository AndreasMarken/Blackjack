package model;

public class EmptyGameDeckException extends IllegalStateException {
    //Oppretter en ny exception som kan utløses når man prøver å hitte, men kortstokken er tom. Denne arver fra IllegalStateException fordi det viser til at en metode er forsøkt kjørt i en upassende eller ulovlig situasjon.
    public EmptyGameDeckException() {
        super();
    }
    public EmptyGameDeckException(String s) {
        super(s);
    }
}
