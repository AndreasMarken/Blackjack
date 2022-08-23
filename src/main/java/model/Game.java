package model;

public class Game {
    private Dealer dealer;
    private Player user;
    private GameDeck gameDeck;
    private int bet;
    private int payoutRate;

    public Game(Player user) {
        if (user == null) {
            throw new IllegalArgumentException("Kan ikke spille uten en spiller!");
        }
        this.user = user;
        this.gameDeck = new GameDeck();
        gameDeck.shuffleDeck();
        payoutRate = 2;
    }

    public void setUser(String userName, int chips) {
        if (userName == null) {
            throw new IllegalArgumentException("Må ha et brukernavn!");
        } 
        if (chips < 0) {
            throw new IllegalArgumentException("Kan ikke ha negativ sum på konto.");
        }
        this.user = new Player(userName, chips);
    }

    public Dealer getDealer() {
        return dealer;
    }

    public void setDealer(Dealer dealer) {
        if (dealer == null) {
            throw new IllegalArgumentException("Kan ikke sette dealer til null");
        }
        this.dealer = dealer;
    }

    public Player getUser() {
        return user;
    }

    public GameDeck getGameDeck() {
        return gameDeck;
    }

    public void setGameDeck(GameDeck gameDeck) {
        this.gameDeck = gameDeck;
    }

    public void createDealer() {
        checkEmptyDeck(2);
        this.dealer = new Dealer(gameDeck);
    }

    public void generatePlayerHand() {
        checkEmptyDeck(2);
        PlayerHand hand = new PlayerHand(gameDeck);
        getUser().updatePlayerHand(hand);
    }

    public void updateDealerPlayerCount(int n) {
        this.dealer.updateUserHandScore(getPlayerHand().getPlayerHandScore());
        this.dealer.setUserHandSize(getPlayerHand().getPlayerHandSize());
    }

    public PlayerHand getPlayerHand() {
        return getUser().getPlayerHand();
    }

    public int getBet() {
        return bet;
    }

    public void setBet(int bet) {
        if (bet > getUser().getChipCount()) {
            throw new IllegalArgumentException("Du kan ikke bette så mye!");
        } else {
            this.bet = bet;
            getUser().updateChipCount(-bet);
        }   
    }

    public void hit() {
        if (this.bet == 0) {
            throw new IllegalArgumentException("Du må bette før du kan hitte!");
        } else {
            if (getPlayerHand() == null || getDealer() == null) {
                throw new IllegalArgumentException("Kan ikke hitte uten å ha deala først");
            }
            if (getPlayerHand().getPlayerHandScore() == 21 && getPlayerHand().getPlayerHandSize() == 2) {
                throw new IllegalArgumentException("Kan ikke hitte hvis du fikk blackjack.");
            }
            checkEmptyDeck(1);
            user.getPlayerHand().hit(gameDeck);
            getDealer().updateUserHandScore(getPlayerHand().getPlayerHandScore());
        }
    }


    private void payout() {
        getUser().updateChipCount(this.bet*this.payoutRate);
    }

    public boolean checkWin() {
        if (getDealer().getDealerHandValue() > 21 && getPlayerHand().getPlayerHandScore() < 22) {
            return true;
        } else if (getPlayerHand().getPlayerHandScore() > 21) {
            return false;
        } else if (getPlayerHand().getPlayerHandScore() > getDealer().getDealerHandValue()) {
            return true;
        }
        return false;
    } 

    public boolean checkDraw() {
        if (getDealer().getDealerHandValue() == getPlayerHand().getPlayerHandScore()) {
            return true;
        } else if (getDealer().getDealerHandValue() > 21 && getPlayerHand().getPlayerHandScore() > 21) {
            return true;
        }
        return false;
    }
    
    public int getPlayerHandScore() {
        return getPlayerHand().getPlayerHandScore();
    }

    private boolean checkEmptyDeck(int cardsNeeded) {
        //Brukes til å sjekke at det nødvendige antall kort for å hitte eller deale er tilgjengelig.
        if (this.gameDeck.getSize() < cardsNeeded) {
            this.gameDeck.newGameDeck();
            this.gameDeck.shuffleDeck();
            return true;
        }
        return false;
    }

    public boolean deal() {
        if (getBet() <= 0) {
            throw new IllegalArgumentException("Du må bette før du kan prøve å deale ut kort.");
        }
        
        generatePlayerHand();
        createDealer();

        //Hvis dealer får blackjack returneres true, og runden avsluttes
        if (getPlayerHandScore() == 21) {
            payout();
            setBet(0);
            return true;
        }
        return false;
    }

    public int stand() {
        if (getPlayerHand() == null || getDealer() == null) {
            throw new IllegalArgumentException("Du må deale før du kan stande.");
        }
        try {
            getDealer().dealerHit(gameDeck);
        } catch (EmptyGameDeckException e) {
            //Hvis man prøver å få dealer til å hitte, men det ikke er nok kort, vil man bare opprette ny gamedeck og få dealer til å hitte igjen. Dette gjøres fordi det er uvisst antall kort som er nødvendig i kortstokken for at dealer skal hitte.
            System.out.println("Lager ny gamedeck og fortsetter spillet");
            getGameDeck().newGameDeck();
            getGameDeck().shuffleDeck();
            getDealer().dealerHit(gameDeck);
        }
        //-1 vil si at dealer vinnet
        int returnValue = -1;
        if (checkWin()) {
            //Dersom bruker vinner skal return value være 1
            payout();
            returnValue = 1;
        } else if (checkDraw()) {
            //Dersom det ender uavgjort skal returverdien være 0
            getUser().updateChipCount(getBet());
            returnValue = 0;
        }
        setBet(0);
        return returnValue;
    }
}

