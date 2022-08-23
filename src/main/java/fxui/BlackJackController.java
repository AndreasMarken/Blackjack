package fxui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import model.Game;

public class BlackJackController implements Initializable {

    private Stage stage;

    private Game game;

    @FXML
    private Label user, userScore, dealerScore, winnerOutput;

    @FXML
    private Button backButton, dealButton, hitButton, standButton, betButton;

    @FXML
    private TextField balance, Bet;

    @FXML 
    private ImageView userCard1, userCard2;

    @FXML
    private FlowPane dealerFlowPane, userFlowPane;

    private ImageView[] dealerImageView, userImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Hente spillinformasjon fra gameholder sendt fra UserAndBankController.
        receiveData();
    }

    @FXML
    private void receiveData() {
        GameHolder holder = GameHolder.getInstance();
        this.game = holder.getGame();
        updateBlackJackGUI();
    }

    @FXML 
    private void backButton() {
        if (game.getPlayerHand() != null) {
            handleStand();
        }
        stage = (Stage) backButton.getScene().getWindow();
        try {
            GameHolder holder = GameHolder.getInstance();
            holder.setGame(game);
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("UserAndBank.fxml"));
            Scene scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            alertMessage("En feil har skjedd i opplastingen av siden.", e.getMessage());
        }
    }

    @FXML
    private void handleDeal() {
        if (game.getBet() > 0) {
            //Hvis bruker har betta så deales kortene ut.
            winnerOutput.setText("");
            boolean blackjack = game.deal();
            updateBlackJackGUI();
            dealButton.setDisable(true);

            //Man skal kun kunne se et av dealer sine kort. Mens brukeren sine kort skal man kunne se.
            showDealerCard(false);
            showUserCards();

            if (!blackjack) {
                //Hvis brukeren ikke fikk blackjack, skal man kunne hitte eller stande.
                hitButton.setDisable(false);
                standButton.setDisable(false);
            } else if (blackjack) {
                winnerOutput.setText("Du vant fordi du fikk blackjack!");
                dealButton.setDisable(true);
                disableBetButton(false);
                updateBlackJackGUI();
            }
        }
    }

    @FXML
    private void handleHit() {
        try {
            game.hit();
        } catch (IllegalStateException e) {
            //Hvis man prøver å hitte, men er bust, så skal stand-metoden automatisk kjøre. Dette er et tilfelle som oppstår hvis det er en feil i tilstanden, og er der får å sikre at spillet ikke vil havne i denne feile tilstanden.
            handleStand();
        }
        updateBlackJackGUI();
        //Oppdaterer kortene til brukeren
        showUserCards();
        if (game.getPlayerHandScore() >= 21) {
            //Hvis bruker har fått over 21, skal stand-metoden automatisk kjøre
            handleStand();
        }
    }

    @FXML
    private void handleStand() {
        //Stand-metoden returnerer en verdi, 1,0,-1 avhengig av hvem som vinner.
        int winner = game.stand();
        hitButton.setDisable(true);
        standButton.setDisable(true);
        showDealerCard(true);

        //Avhengig av hvem som vinner skal switch-statementet avgjøre teksten som kan leses på skjermen for bruker.
        switch (winner) {
            case 1:
                winnerOutput.setText("Du vant!");
                break;
            case 0:
                winnerOutput.setText("Uavgjort!");
                break;
            case -1:
                winnerOutput.setText("Dealeren vinner...");
                break;
            default:
                alertMessage("Feil i spill", "Noe feil har skjedd. Runden nullstilles.");
                game.getUser().updateChipCount(Integer.parseInt(Bet.getText()));
        }
        updateBlackJackGUI();
        disableBetButton(false);
    }

    @FXML
    private void handleBet() {
        try {
            if (Integer.parseInt(Bet.getText()) > 0) {
                game.setBet(Integer.parseInt(Bet.getText()));
                disableBetButton(true);
                dealButton.setDisable(false);
                winnerOutput.setText("");
            }
        } catch (Exception e) {
            alertMessage("Feil bet.", "Sjekk at du har skrevet inn et gyldig heltall. Kunne ikke gjøre det om til et gyldig heltall.");
        }
        updateBlackJackGUI();
    }

    @FXML 
    private void updateBlackJackGUI() {
        if (game != null) {
            user.setText(game.getUser().getUserName());
            balance.setText("Balance: $"+Integer.toString(game.getUser().getChipCount()));
        } 
    }

    @FXML
    private void showDealerCard(boolean showFirstCard) {
        dealerFlowPane.getChildren().clear();
        //Tar utgangspunkt i at kun de 12 første kortene som blir trukket, er synlige. Ved ekstremt sjeldne tilfeller vil ikke dette være nok, men i de fleste tilfeller er det nok.
        dealerImageView = new ImageView[12];
        if (!showFirstCard) {
            try  {
                //Hvis en dealer ikke skal vise det første kortet, vises baksiden av kortstokken.
                dealerImageView[0] = new ImageView(getClass().getResource("img" + System.getProperty("file.separator") + "gamedeck.png").toString());
                dealerImageView[0].setPreserveRatio(true);
                dealerImageView[0].setFitHeight(160);
                dealerFlowPane.getChildren().add(dealerImageView[0]);

                //Viser det andre kortet til dealeren.
                dealerImageView[1] = new ImageView(getClass().getResource("img" + System.getProperty("file.separator") + game.getDealer().toString(1) + ".png").toString());
                dealerImageView[1].setPreserveRatio(true);
                dealerImageView[1].setFitHeight(160);
                dealerFlowPane.getChildren().add(dealerImageView[1]);
                
                dealerScore.setText(Integer.toString(game.getDealer().getDealerFirstCardValue()));
            } catch (Exception e) {
                //Kan oppstå feil i henting av bilde, da vil en alert dukke opp.
                alertMessage("Feil i opplastning av bilde: ", e.getMessage());
            }
        } else if (showFirstCard) {
            for (int i = 0; i < game.getDealer().getDealerHand().size(); i++) {
                try {
                    //Hvis alle kort skal vises, kjøres en for-løkke som viser alle kortene til dealer.
                    dealerImageView[i] = new ImageView(getClass().getResource("img" + System.getProperty("file.separator") + game.getDealer().toString(i) + ".png").toString());
                    dealerImageView[i].setPreserveRatio(true);
                    dealerImageView[i].setFitHeight(160);
                    dealerFlowPane.getChildren().add(dealerImageView[i]);
                } catch (Exception e) {
                    alertMessage("Feil i opplastning av bilde", "Fikk ikke lastet inn bildet som skal vise frem kortet");
                }
                
            }
            dealerScore.setText(Integer.toString(game.getDealer().getDealerHandValue()));
        }
    }

    @FXML
    private void showUserCards() {
        //Tar utgangspunkt i at kun de 12 første kortene som blir trukket, er synlige. Ved ekstremt sjeldne tilfeller vil ikke dette være nok, men i de fleste tilfeller er det nok.
        userImageView = new ImageView[12];
        userFlowPane.getChildren().clear();
        for (int i = 0; i < game.getPlayerHand().getPlayerHandSize(); i++) {
            //Itererer gjennom alle kortene til brukeren og viser de i imageviewet.
            try {
                userImageView[i] = new ImageView(getClass().getResource("img" + System.getProperty("file.separator") + game.getPlayerHand().toString(i).toString() + ".png").toString());
                userImageView[i].setPreserveRatio(true);
                userImageView[i].setFitHeight(160);
                userFlowPane.getChildren().add(userImageView[i]);
            } catch (Exception e) {
                alertMessage("Feil i opplastning av bilde", "Fikk ikke lastet inn bildet som skal vise frem kortet: " + e.getMessage());
            }
        }
        userScore.setText(Integer.toString(game.getPlayerHandScore()));
    }

    @FXML
    private void disableBetButton(boolean setDisable) {
        Bet.setDisable(setDisable);
        betButton.setDisable(setDisable);
    }

    @FXML
    private void alertMessage(String tittel, String innhold) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(tittel);
        alert.setHeaderText("En feil har oppstått");
        alert.setContentText(innhold);
        alert.showAndWait();
    }
}
