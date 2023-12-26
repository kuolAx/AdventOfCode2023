package december10;

import utils.AdventHelper;

import java.util.ArrayList;
import java.util.List;

public class PipeMaze1 {
    static String testInput1= """
            .....
            .S-7.
            .|.|.
            .L-J.
            .....""";
    static String testInput2= """
            -L|F7
            7S-7|
            L|7||
            -L-J|
            L|-JF""";
    static String testInput3= """
            ..F7.
            .FJ|.
            SJ.L7
            |F--J
            LJ...""";
    static String testInput4= """
            7-F7-
            .FJ|7
            SJLL7
            |F--J
            LJ.LJ""";

    public enum Pipe {
        NOPIPE, I_VERTICAL, _HORIZONTAL, L_NE, J_NW, SVN_SW, F_SE, START
    }

    public static int lineLength;

    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december10/input.txt");
        content = testInput1;
        String[] contentLines = content.split("\n");
        lineLength = contentLines[0].length();

        int startLine = 0;
        int startIndex = 0;
        //get line and index of "S" symbol
        for (int i = 0; i < contentLines.length; i++) {
            if (contentLines[i].contains("S")) {
                startLine = i;
                startIndex = contentLines[i].indexOf("S");
            }
        }
        System.out.println("start " + startLine + " " + startIndex);

        //scan adjacent positions for connected pipes
        int previousindex = 0;

        if ( contentLines[startLine].charAt(startIndex+1) == 'J' || contentLines[startLine].charAt(startIndex+1) == '7' || contentLines[startLine].charAt(startIndex+1) == '-'){
            previousindex = startIndex;
            startIndex++;
        } else if ( contentLines[startLine].charAt(startIndex-1) == 'L' || contentLines[startLine].charAt(startIndex-1) == 'F' || contentLines[startLine].charAt(startIndex-1) == '-'){
            previousindex = startIndex;
            startIndex--;
        } else if ( contentLines[startLine-1].charAt(startIndex) == '7' || contentLines[startLine-1].charAt(startIndex) == 'F' || contentLines[startLine-1].charAt(startIndex) == '|' ) {
            previousindex = startIndex;
            startLine--;
        }

        System.out.println("start " + startLine + " " + startIndex);

        //follow path until S is reached again
        int stepNumber = 1;
        char currentChar = 0;

        while ( currentChar != 'S' ) {



        }


    }
}
