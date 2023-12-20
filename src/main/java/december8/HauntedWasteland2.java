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
    private static final Map<String, List<String>> elementMap = new HashMap<>(); //Map<String, List<String>> - { NodeName, [L-Instruction, R-Instruction] }
    private static char[] instructions; //char array of "RLLLRLLRRLR...."
    private static String currentKey;

    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december8/input.txt");
        extractInstructionsAndElementsMap( content );

        System.out.println( "instructions created - length: " + instructions.length );
        System.out.println( "element-map created" );

        List<String> startingKeysList = getStartingNodes();
        System.out.println("initial Starting Keys: " + startingKeysList);

        List<Long> toZStepsList = new ArrayList<>();

        for (String startNode : startingKeysList) {

            String tempKey = startNode;
            setCurrentKey( tempKey );
            long numberOfSteps = 0;

            while ( !getCurrentKey().endsWith("Z") ) {
                List<Object> results = getNumberOfStepsNeeded(numberOfSteps );

                tempKey             = (String) results.get(0);
                numberOfSteps       = (long) results.get(1);
            }

            if( tempKey.endsWith("Z") ) {
                toZStepsList.add( numberOfSteps );
            }
        }

        System.out.println( "number of steps needed per starting node: " + toZStepsList );
        long lowestCommonMultiple = lcm( toZStepsList );
        System.out.println("lowest common multiple of all steps needed: " + lowestCommonMultiple);
    }

    public static List<Object> getNumberOfStepsNeeded( long numberOfSteps ) {

        for (char instruction : instructions) {

            //set currentKey to the next value depending on the instruction 'R' or 'L'
            if ( instruction == 'R' ) {
                setCurrentKey( elementMap.get( getCurrentKey() ).get(1) );
            } else {
                setCurrentKey( elementMap.get( getCurrentKey() ).get(0) );
            }
            numberOfSteps++;

            if ( getCurrentKey().endsWith("Z") ) {
                return List.of( getCurrentKey(), numberOfSteps );
            }
        }

        return List.of( getCurrentKey(), numberOfSteps );
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
                List<String> matchesList = matchWordCharacters.results().map(MatchResult::group).map(String::trim).toList();
                elementMap.put( matchesList.get(0), List.of( matchesList.get(1), matchesList.get(2) ) );
            } else {
                while(matchWordCharacters.find()) {
                    instructions = matchWordCharacters.group().toCharArray();
                }
            }
        }
    }

    //greatestCommonDivisor
    static long gcd(long a, long b)
    {
        if (b == 0)
            return a;
        return gcd(b, a % b);
    }

    //lowestCommonMultiple
    static long lcm(List<Long> numbers)
    {
        return numbers.stream().reduce(
                1L, (x, y) -> (x * y) / gcd(x, y) );
    }

    public static String getCurrentKey() {
        return currentKey;
    }

    public static void setCurrentKey(String currentKey) {
        HauntedWasteland2.currentKey = currentKey;
    }
}


