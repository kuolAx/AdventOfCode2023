package utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AdventHelper {

    public static String readFile(String path){
        String content;
        try {
            content = Files.readString(Paths.get(path));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return content;
    }
}
