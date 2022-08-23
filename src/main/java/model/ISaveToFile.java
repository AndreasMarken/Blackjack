package model;

import java.io.FileNotFoundException;
import java.nio.file.Path;

public interface ISaveToFile {
    
    void saveToFile(String fileName, Game game) throws Exception;
    Game loadGame(String fileName) throws FileNotFoundException, Exception;
    Path getSaveFilePath(String fileName);

}