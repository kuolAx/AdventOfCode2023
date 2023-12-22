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

    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december10/input.txt");
        content = testInput1;
        content = content.replace("\n","");

        List<Pipe> path1 = new ArrayList<>();
        List<Pipe> path2 = new ArrayList<>();
    }
}
