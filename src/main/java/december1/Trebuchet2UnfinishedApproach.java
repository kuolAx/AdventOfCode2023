package december1;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Trebuchet2UnfinishedApproach {

    static String testinput = """
onetwothreefourfivesixseveneightninezero
oneighthreefourfivesixseveninetwozero
mqtwoneight7sevenfourht
stzmqplr8gvmxblz
""";

    public static void main(String[] args) {

        String content;
        try {
            content = Files.readString(Paths.get("./src/main/java/december1/input.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scan = new Scanner(testinput);
        String currentLine;
        List<String> currentLineNumbers = new ArrayList<>();
        int finalResult = 0;

//        Pattern pattern = Pattern.compile( "[0-9]|zero|one|two|three|four|five|six|seven|eight|nine");
        Pattern pattern = Pattern.compile( "(?=(\\d|one|two|three|four|five|six|seven|eight|nine))");

        while( scan.hasNext() ) {
            currentLine = scan.next();
            System.out.println();
            System.out.println("----------------------------------------------------");
            System.out.println("CURRENT LINE: " + currentLine); //DEBUG

            Matcher matcher = pattern.matcher(currentLine);
            matcher.results().map(MatchResult::group)
                    .map( x -> { if ( !x.matches("\\d") ){
                                    x = x.replace("one","1")
                                            .replace("two","2")
                                            .replace("three","3")
                                            .replace("four","4")
                                            .replace("five","5")
                                            .replace("six","6")
                                            .replace("seven","7")
                                            .replace("eight","8")
                                            .replace("nine","9")
                                            .replace("zero","0");
                                    }
                                 return x;} )
                    .forEach(currentLineNumbers::add);

            currentLineNumbers.forEach(System.out::print);

            System.out.println();
            System.out.println("First Element:" + currentLineNumbers.get(0));
            System.out.println("Last Element:" + currentLineNumbers.get(currentLineNumbers.size() - 1));

            //get rid of unnecessary values by only ending up with a maximum of 2 values in the list.
            while ( currentLineNumbers.size() > 2 )
                currentLineNumbers.remove(1);
            //set String to 0 to avoid Numberformatexception if no numbers contained
            if ( currentLineNumbers.isEmpty() )
                currentLineNumbers.add("0");
            //double if only one number in current line
            if ( currentLineNumbers.size() == 1 )
                currentLineNumbers.add( currentLineNumbers.get(0) );

            System.out.print("Final Calibration number for this line: ");
            currentLineNumbers.forEach(System.out::print);
            finalResult += Integer.parseInt(currentLineNumbers.get(0) + currentLineNumbers.get(1));

            currentLineNumbers.clear();
        }

        System.out.println();
        System.out.println( "Sum of all calibration values is: " + finalResult );
        scan.close();
    }
}
