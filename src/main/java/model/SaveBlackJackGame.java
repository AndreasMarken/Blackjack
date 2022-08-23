package model;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

public class SaveBlackJackGame implements ISaveToFile{

    @Override
    public void saveToFile(String fileName, Game game) throws Exception {
        //Hvis filområdet/mappen der filen skal lagres ikke eksisterer så opprettes denne
        Files.createDirectories(getSaveFolderPath());

        //Skriver til fil i formatet:
        //Brukernavn
        //Antall sjetonger
        //Korstokken med mellomrom mellom hvert kort. Der kortet skrives ved hjelp av dens toString. Som vil skrive kortet som suit+value
        try (PrintWriter printWriter = new PrintWriter(getSaveFilePath(fileName).toFile())) {
                printWriter.println(game.getUser().getUserName());
                printWriter.println(game.getUser().getChipCount());
                for (Card card : game.getGameDeck().getGameDeck()) {
                    printWriter.print(card.toString()+" ");
                }
                //printWriter.print('\n');
                printWriter.print(System.getProperty("line.separator"));
        } 
    }

    @Override
    public Game loadGame(String fileName) throws Exception, FileNotFoundException  {
        Game game;
        try (Scanner scanner = new Scanner(getSaveFilePath(fileName).toFile())) {
            //Henter informasjonen og splitter ved hjelp av ny linje
		    //scanner.useDelimiter("\n");
            scanner.useDelimiter(System.getProperty("line.separator"));
            
            String name = scanner.next();
            int balance = Integer.parseInt(scanner.next());
            String string = scanner.next();

            //Splitter kortene ved hjelp av mellomrommene som blir skrevet til filen
            String[] tmp = string.split(" ");

            game = new Game(new Player(name, balance));
            game.getGameDeck().clearDeck();

            for (int i = 0; i < tmp.length; i++) {
                char suit = tmp[i].charAt(0);
                int face = Integer.parseInt(tmp[i].substring(1));
                game.getGameDeck().addCard(new Card(suit, face));
            }
            scanner.close();
            return game;
            }
    }

    @Override
    public Path getSaveFilePath(String fileName) {
        return getSaveFolderPath().resolve(fileName + ".txt");
    }

    private Path getSaveFolderPath() {
        return Path.of(System.getProperty("user.home"), "tdt4100files", "blackjack", "saveFiles");
    }
}
