package model;

public class Player {
    private String userName;
    private int chipCount;
    private PlayerHand playerHand;

    public Player(String userName, int chipCount) {
        if (userName.equals("")) {
            throw new IllegalArgumentException("Må ha et gyldig brukernavn!");
        } 
        if (userName.equals(null)) {
            throw new NullPointerException("Kan ikke ikke ha noe brukernavn");
        }
        if (chipCount < 0) {
            throw new IllegalArgumentException("Kan ikke ha negativ balance på kontoen");
        }
        this.userName = userName;
        this.chipCount = chipCount;
    }

    public void updatePlayerHand(PlayerHand playerHand) {
        if (playerHand == null) {
            throw new IllegalStateException("Det er ikke mulig å sette null som en hånd.");
        }
        this.playerHand = playerHand;
    }

    public PlayerHand getPlayerHand() {
        return this.playerHand;
    }

    public String getUserName() {
        return userName;
    }

    public int getChipCount() {
        return chipCount;
    }

    public void updateChipCount(int chipCount) {
        if (chipCount < 0) {
            if (this.chipCount+chipCount < 0) {
                throw new IllegalArgumentException("Kan ikke ta ut så mye fra kontoen.");
            }
        }
        this.chipCount += chipCount;
    }
}
