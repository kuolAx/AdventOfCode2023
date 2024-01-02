package december10;

import utils.AdventHelper;

import java.util.Arrays;
import java.util.List;

public class PipeMaze1 {
    public static int lineLength;

    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december10/input.txt");
        String[] contentLines = content.split("\n");
        lineLength = contentLines[0].length();

        int line = 0;
        int index = 0;

        //get line and index of "S" symbol
        for (int i = 0; i < contentLines.length; i++) {
            if (contentLines[i].contains("S")) {
                line = i;
                index = contentLines[i].indexOf("S");
            }
        }
        System.out.println("start " + line + " " + index);

        List<Character> rightConnectors = Arrays.asList('J', '7', '-', 'S');
        List<Character> leftConnectors = Arrays.asList('L', 'F', '-', 'S');
        List<Character> aboveConnectors = Arrays.asList('7', 'F', '|', 'S');
        List<Character> belowConnectors = Arrays.asList('L', 'J', '|', 'S');

        int stepNumber = 0;
        //preventBacktracking to only move along the path in one direction: 1 - skip right, 2 - skip left, 3 - skip above, 4 - skip below
        int preventBacktracking = 0;

        char charRight = 0, charLeft = 0, charAbove = 0, charBelow = 0 ;
        char currentChar = 'S';

        do {
            System.out.println("currentChar: " + currentChar);

            if (index + 1 < lineLength) charRight = contentLines[line].charAt(index + 1);
            else charRight = 0;

            if (index - 1 >= 0) charLeft = contentLines[line].charAt(index - 1);
            else charLeft = 0;

            if (line - 1 >= 0) charAbove = contentLines[line - 1].charAt(index);
            else charAbove = 0;

            if (line + 1 < contentLines.length) charBelow = contentLines[line + 1].charAt(index);
            else charBelow = 0;

            //right
            if (preventBacktracking != 1 && rightConnectors.contains(charRight) && leftConnectors.contains(currentChar)) {
                index++;
                preventBacktracking = 2;
            }
            //left
            else if (preventBacktracking != 2 && leftConnectors.contains(charLeft) && rightConnectors.contains(currentChar)) {
                index--;
                preventBacktracking = 1;
            }
            //above
            else if (preventBacktracking != 3 && aboveConnectors.contains(charAbove) && belowConnectors.contains(currentChar)) {
                line--;
                preventBacktracking = 4;
            }
            //below
            else if (preventBacktracking != 4 && belowConnectors.contains(charBelow) && aboveConnectors.contains(currentChar)) {
                line++;
                preventBacktracking = 3;
            }

            currentChar = contentLines[line].charAt(index);
            stepNumber++;

        } while (currentChar != 'S');

        System.out.println("final stepnumber: " + stepNumber);
        System.out.println("final stepnumber/2: " + stepNumber / 2);


    }
}
