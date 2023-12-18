package december8;

import utils.AdventHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HauntedWasteland1 {
    private static final Pattern MATCH_WORD_CHARACTERS = Pattern.compile("(\\w+)");

    //Map<String, List<String>> - { Instruction, [L-Instruction, R-Instruction] }
    private static final Map<String, List<String>> elementMap = new HashMap<>();
    private static char[] instructions;

    static String testInput = """
            RL
                        
            AAA = (BBB, CCC)
            BBB = (DDD, EEE)
            CCC = (ZZZ, GGG)
            DDD = (DDD, DDD)
            EEE = (EEE, EEE)
            GGG = (GGG, GGG)
            ZZZ = (ZZZ, ZZZ)""";
    static String testInput2 = """
            LLR
                        
            AAA = (BBB, BBB)
            BBB = (AAA, ZZZ)
            ZZZ = (ZZZ, ZZZ)""";
    private static String currentKey;


    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december8/input.txt");
        extractInstructionsAndElementsMap( content );

        System.out.println( instructions );
        System.out.println( elementMap );

        currentKey = "AAA";
        int numberOfSteps = 0;

        while ( !currentKey.equals("ZZZ") ) {
            numberOfSteps = getNumberOfStepsNeeded( numberOfSteps );
        }

        System.out.println("How many steps are needed to reach ZZZ with the given instructions: " + numberOfSteps);
    }

    private static int getNumberOfStepsNeeded( int numberOfSteps) {
        for (char instruction : instructions) {

            if( currentKey.equals( "ZZZ" ) )
                break;
            if ( instruction == 'R' ){
                currentKey = elementMap.get(currentKey).get( 1 );
            } else {
                currentKey = elementMap.get(currentKey).get( 0 );
            }

            numberOfSteps++;
        }
        return numberOfSteps;
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
