package fxui;

import model.Game;

public class GameHolder {
    private Game game;
    private final static GameHolder INSTANCE = new GameHolder();

    //Gjør det umulig å opprette nye instanser av et GameHolder objekt. Dvs. at den instansen som hentes ved hjelp av stativ GameHolde getInstance() er den eneste som noen gang kan bli hentet og opprettet. Denne vil hele tiden være lik, og dermed vil også tilstanden til game være lagret. Det gjør at vi kan sende over informasjonen om game til de ulike kontrollerne.
    private GameHolder() {}

    public static GameHolder getInstance() {
        return INSTANCE;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }
}
