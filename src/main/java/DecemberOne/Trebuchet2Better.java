package DecemberOne;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trebuchet2Better {
    public static void main(String[] args) {

        String content;
        try {
            content = Files.readString(Paths.get("./src/main/java/DecemberOne/Input.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scan = new Scanner(content);
        String currentLine;
        List<String> currentLineNumbers = new ArrayList<>();
        int finalResult = 0;

        Pattern patternForwards = Pattern.compile( "[0-9]|zero|one|two|three|four|five|six|seven|eight|nine");
        Pattern patternBackwards = Pattern.compile( "[0-9]|orez|eno|owt|eerht|ruof|evif|xis|neves|thgie|enin");

        while (scan.hasNext()) {
            currentLine = scan.next();
            System.out.println();
            System.out.println("----------------------------------------------------");
            System.out.println("CURRENT LINE: " + currentLine); //DEBUG

            Matcher forwardsMatcher = patternForwards.matcher(currentLine);
            forwardsMatcher.results().map(MatchResult::group).findFirst()
                    .map(x -> {
                        if (!x.matches("\\d")) {
                            x = x.replace("one", "1")
                                    .replace("two", "2")
                                    .replace("three", "3")
                                    .replace("four", "4")
                                    .replace("five", "5")
                                    .replace("six", "6")
                                    .replace("seven", "7")
                                    .replace("eight", "8")
                                    .replace("nine", "9")
                                    .replace("zero", "0");
                        }
                        return x;
                    })
                    .map(currentLineNumbers::add);

            Matcher backwardsMatcher = patternBackwards.matcher( new StringBuilder(currentLine).reverse().toString() );
            backwardsMatcher.results().map(MatchResult::group).findFirst()
                    .map(x -> {
                        if (!x.matches("\\d")) {
                            x = x.replace("eno", "1")
                                    .replace("owt", "2")
                                    .replace("eerht", "3")
                                    .replace("ruof", "4")
                                    .replace("evif", "5")
                                    .replace("xis", "6")
                                    .replace("neves", "7")
                                    .replace("thgie", "8")
                                    .replace("enin", "9")
                                    .replace("orez", "0");
                        }
                        return x;
                    })
                    .map(currentLineNumbers::add);

            System.out.print("Final Calibration number for this line: ");
            currentLineNumbers.forEach(System.out::print);
            if ( !currentLineNumbers.isEmpty() )
                finalResult += Integer.parseInt(currentLineNumbers.get(0) + currentLineNumbers.get(1));

            currentLineNumbers.clear();
        }

        System.out.println();
        System.out.println("Sum of all calibration values is: " + finalResult);
        scan.close();
    }

}
