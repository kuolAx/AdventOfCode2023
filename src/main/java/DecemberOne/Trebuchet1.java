package DecemberOne;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

//My Alternative Ideas:
//CharacterIterator characterIterator = new StringCharacterIterator(currentLine);
//stream with lambda
//split at each int and take first + last char -> faster?
//regex searching and direct splitting to an Array

public class Trebuchet1 {
    public static void main(String[] args) {

        String content;
        try {
            content = Files.readString(Paths.get("./src/main/java/DecemberOne/input.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Scanner scan = new Scanner(content);
        String currentLine;
        int finalResult = 0;
        StringBuilder currentNumbers;

        //outer layer -> iterates through indivdual lines of input String.
        while( scan.hasNext() ){
            currentLine = scan.next();
            System.out.println("----------------------------------------------------");
            System.out.println("CURRENT LINE: " + currentLine); //DEBUG
            currentNumbers = new StringBuilder();

            //inner layer -> iterates through each line and condenses numbers.
            for ( int i = 0; i < currentLine.length(); i++ ) {
                char currentChar = currentLine.charAt(i);

                //Abbruchbedingung -> letzter Character
                if (i == currentLine.length()-1) {

                    if( Character.isDigit( currentChar ) )
                        currentNumbers.append( currentChar );

                    while( currentNumbers.length() > 2 ){
                        currentNumbers.deleteCharAt(1);
                    }
                    //set String to 0 to avoid Nunmberformatexception if no numbers contained
                    if ( currentNumbers.length() == 0 )
                        currentNumbers.append(0);
                    //double if only one number in current line
                    if ( currentNumbers.length() == 1 )
                        currentNumbers.append( currentNumbers );

                    finalResult += Integer.parseInt( String.valueOf( currentNumbers ) );
                    System.out.println("RESULT CURRENT LINE= " + currentNumbers);
                    break;
                }

                if( !Character.isDigit( currentChar ) ){
                    continue;
                } else {
                    currentNumbers.append( currentChar );
                }

                System.out.println(currentNumbers); //DEBUG

            }
        }

        System.out.println( "Sum of all calibration values is: " + finalResult );
        scan.close();
    }
}
