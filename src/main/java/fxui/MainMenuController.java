package fxui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Game;
import model.SaveBlackJackGame;

public class MainMenuController {
    private Stage stage;
    
    @FXML
    private Button quitButton, playButton, loadGameButton;

    @FXML 
	private void playButton() {
        stage = (Stage) playButton.getScene().getWindow();

		try {
			GameHolder holder = GameHolder.getInstance();
            holder.setGame(null);
			AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("UserAndBank.fxml"));
			Scene scene = new Scene(root, 1280, 800);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			alertMessage("Feil i åpning av ny fane", e.getMessage());
		}
	}

    @FXML
	private void quitButton() {
        Stage stage = (Stage) quitButton.getScene().getWindow();
		stage.close();
    }

	@FXML 
	private void loadButton() {
		String filNavn = "";
		try {
			TextInputDialog dialog = new TextInputDialog();
        	dialog.setTitle("Last opp spill");
        	dialog.setHeaderText("Skriv inn navnet på filen du vil åpne");
        	dialog.setContentText("Filnavn:");
			filNavn = dialog.showAndWait().get();
		} catch (NoSuchElementException e) {
			//Do nothing
		}
		
		SaveBlackJackGame saver = new SaveBlackJackGame();

		if (!filNavn.equals(""))  {
			try {
				Game game = saver.loadGame(filNavn);
				stage = (Stage) loadGameButton.getScene().getWindow();
				try {
					GameHolder holder = GameHolder.getInstance();
					holder.setGame(game);
					AnchorPane root = (AnchorPane) FXMLLoader.load(getClass().getResource("BlackJack.fxml"));
					Scene scene = new Scene(root, 1280, 800);
					stage.setScene(scene);
					stage.show();
				} catch (IOException e) {
					alertMessage("Feil i apning av ny fane", "Det oppstod en feil i åpning av fanen, prøv på nytt.");
				}
			} catch (FileNotFoundException e) {
				alertMessage("Fant ikke filen.", "Fant ikke filen du skrev inn.");
			} catch (Exception e) {
				alertMessage("Feil i henting av fil", "Det er en feil i formatet på innholdet i filen din, prøv å velg en annen fil.");
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
