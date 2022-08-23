package fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class BlackJackApp extends Application {
    @Override
	public void start(final Stage primaryStage) throws Exception {
		primaryStage.setTitle("BlackJack");
		primaryStage.setScene(new Scene(FXMLLoader.load(getClass().getResource("MainMenu.fxml"))));
		primaryStage.show();
		primaryStage.setResizable(false);
	}
	
	public static void main(final String[] args) {
		Application.launch(args);
	}
}
