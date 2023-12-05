package DecemberTwo;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class CubeConundrum1 {
    public static void main(String[] args) {

        String testInput = """
                           Game 1: 1 green, 2 red, 6 blue; 4 red, 1 green, 3 blue; 7 blue, 5 green; 6 blue, 2 red, 1 green
                           Game 2: 1 green, 17 red; 1 blue, 6 red, 7 green; 2 blue, 4 red, 7 green; 1 green, 6 red, 2 blue
                           Game 3: 6 red, 15 blue, 15 green; 1 green, 4 red, 12 blue; 14 blue, 9 red, 1 green; 2 red, 15 blue, 12 green
                           """;

        String content;
        try {
            content = Files.readString(Paths.get("./src/main/java/DecemberTwo/Input.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        Stream.of(content.split("\\R")).map();

    }
}
