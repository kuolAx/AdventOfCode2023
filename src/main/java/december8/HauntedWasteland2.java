package december8;

import utils.AdventHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HauntedWasteland2 {
    private static final Pattern MATCH_WORD_CHARACTERS = Pattern.compile("(\\w+)");

    //Map<String, List<String>> - { Instruction, [L-Instruction, R-Instruction] }
    private static final Map<String, List<String>> elementMap = new HashMap<>();
    private static char[] instructions;
    private static int currentKeysSize;
    private static int numberOfSteps = 0;
    static String testInput = """
            LR
                        
            11A = (11B, XXX)
            11B = (XXX, 11Z)
            11Z = (11B, XXX)
            22A = (22B, XXX)
            22B = (22C, 22C)
            22C = (22Z, 22Z)
            22Z = (22B, 22B)
            XXX = (XXX, XXX)""";

    private static List<String> currentKeys;

    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december8/input.txt");
        extractInstructionsAndElementsMap( content );

        System.out.println( instructions );
        System.out.println( elementMap );

        currentKeys = getStartingNodes();
        System.out.println("initial Starting Keys: " + currentKeys);

        currentKeysSize = currentKeys.size();

        while ( currentKeys.stream().filter( x -> x.endsWith("Z") ).count() != currentKeysSize ) {
            numberOfSteps = getNumberOfStepsNeeded( numberOfSteps );
        }

        System.out.println("How many steps are needed to simultaneously end on Z with all paths: " + numberOfSteps);
    }

    private static int getNumberOfStepsNeeded( int numberOfSteps) {
        for (char instruction : instructions) {

            numberOfSteps++;

            //do for each path
            for (int i = 0; i < currentKeysSize; i++) {
                String currentKey = currentKeys.get(i);

                String nodeLeftEntry = elementMap.get(currentKey).get( 0 );
                String nodeRightEntry = elementMap.get(currentKey).get( 1 );

                if ( instruction == 'R' ){
                    currentKeys.set( i, nodeRightEntry );
                } else {
                    currentKeys.set( i, nodeLeftEntry );
                }
            }

            if ( currentKeys.stream().filter( x -> x.endsWith("Z") ).count() == currentKeysSize ) {
                return numberOfSteps;
            }
        }

        System.out.println(currentKeys.stream().filter( x -> x.endsWith("Z") ).count());

        return numberOfSteps;
    }

    private static List<String> getStartingNodes() {
        //using Collectors with specified ArrayList to retrieve a mutable List
        return elementMap.keySet().stream().filter( x -> x.endsWith("A")).collect( Collectors.toCollection(ArrayList::new) );
    }

    private static void extractInstructionsAndElementsMap(String content) {
        String[] contentLines = content.split("\n");

        for (int i = 0; i < contentLines.length; i++) {
            String contentLine = contentLines[i];

            Matcher matchWordCharacters = MATCH_WORD_CHARACTERS.matcher(contentLine);

            if ( i != 0 && i != 1 ) {
                List<String> matchesList = matchWordCharacters.results().map(MatchResult::group).toList();
                elementMap.put( matchesList.get(0), List.of( matchesList.get(1), matchesList.get(2) ) );
            } else {
                while(matchWordCharacters.find()) {
                    instructions = matchWordCharacters.group().toCharArray();
                }
            }

        }
    }
}
