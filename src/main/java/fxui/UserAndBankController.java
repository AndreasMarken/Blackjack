package fxui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Player;
import model.SaveBlackJackGame;
import model.Game;

public class UserAndBankController implements Initializable {
    private Game game;

    private Stage stage;

    @FXML
    private Button backButton, generatePlayer, plus1, plus5, plus25, plus50, plus100, pluss500, startGameButton, saveGame, depositButton;

    @FXML
    private ButtonBar addBalanceButtonBar;

    @FXML
    private TextField userName, depositField, saveFile;

    @FXML
    private Label currentPlayer, playerBalance;


    @FXML 
    private void backButton() {
        stage = (Stage) backButton.getScene().getWindow();
        try {
            AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("MainMenu.fxml"));
            Scene scene = new Scene(root, 1280, 800);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            alertMessage("Fant ikke siden du lette etter.", e.getMessage());
        }
    }

    @FXML 
    private void generatePlayer() {
        String name = userName.getText();
        try {
            game = new Game(new Player(name, 0));
            updateUserAndBankGUI();
        } catch (Exception e) {
            alertMessage("Feil i start av spill", e.getMessage());
        }
    }

    @FXML
    private void addBalance1() {
        if (game != null) {
            game.getUser().updateChipCount(1);
            updateUserAndBankGUI();
        }
    }

    @FXML
    private void addBalance5() {
        if (game != null) {
            game.getUser().updateChipCount(5);
            updateUserAndBankGUI();
        }
    }

    @FXML
    private void addBalance25() {
        if (game != null) {
            game.getUser().updateChipCount(25);
            updateUserAndBankGUI();
        }
    }

    @FXML
    private void addBalance50() {
        if (game != null) {
            game.getUser().updateChipCount(50);
            updateUserAndBankGUI();
        }
    }

    @FXML
    private void addBalance100() {
        if (game != null) {
            game.getUser().updateChipCount(100);
            updateUserAndBankGUI();
        }
    }

    @FXML
    private void addBalance500() {
        if (game != null) {
            game.getUser().updateChipCount(500);
            updateUserAndBankGUI();
        }
    }

    @FXML
    private void startGame() {
        if (game != null) {
            stage = (Stage) startGameButton.getScene().getWindow();
            try {
                GameHolder holder = GameHolder.getInstance();
                holder.setGame(game);
                Parent root = FXMLLoader.load(getClass().getResource("BlackJack.fxml"));
                Scene scene = new Scene(root, 1280, 800);
                stage.setScene(scene);
                stage.show();
            } catch (IOException e) {
                alertMessage("Feil i start spill.", e.getMessage() +". Prøv å starte applikasjonen på nytt");
            }
        }
        
    }

    @FXML
    private void updateUserAndBankGUI() {
        if (game != null) {
            currentPlayer.setText(game.getUser().getUserName());
            playerBalance.setText(Integer.toString(game.getUser().getChipCount()) + "$");
            depositField.setDisable(false);
            saveFile.setDisable(false);
            saveGame.setDisable(false);
            addBalanceButtonBar.setDisable(false);
            startGameButton.setDisable(false);
            depositButton.setDisable(false);
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //Kjøres med en gang scenen åpnes. Brukes til å hente innformasjon fra gameholderen dersom man kommer fra blackjack-siden. Dersom man kommer fra mainmenu siden er game i gameholder satt til null, og det er dermed ikke noe tilstand å hente.
        receiveData();
    }

    @FXML
    private void receiveData() {
        GameHolder holder = GameHolder.getInstance();
        this.game = holder.getGame();
        updateUserAndBankGUI();
    }

    @FXML
    private void handleDepositButton() {
        if (game != null) {
            try {
                int inputField = Integer.parseInt(depositField.getText());
                if (inputField <= 0) {
                    alertMessage("Feil deposit-verdi.", "Du må skrive inn et positivt heltall.");
                } else {
                    game.getUser().updateChipCount(inputField);
                    updateUserAndBankGUI();
                }
            } catch (Exception e) {
                alertMessage("Feil deposit-verdi", "Kunne ikke gjøre om det du skrev til et heltall, sjekk at du skrev inn riktig.");
            }
        }
    }

    @FXML
    private void handleSaveGame() {
        if (game != null) {
            try {
                String fileName = saveFile.getText();
                if (fileName.equals("")) {
                    alertMessage("Feil i filinput", "Du må skrive inn et navn til filen det skal lagres til.");
                } else {
                    SaveBlackJackGame saver = new SaveBlackJackGame();
                    saver.saveToFile(fileName, this.game);
                }
            } catch (Exception e) {
                alertMessage("Feil i lagring til fil", e.getMessage());
            }
        }
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
