package december8;

import utils.AdventHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class HauntedWasteland2_2 {
    private static final Pattern MATCH_WORD_CHARACTERS = Pattern.compile("(\\w+)");

    //Map<String, List<String>> - { NodeName, [L-Instruction, R-Instruction] }
    private static final Map<String, List<String>> elementMap = new HashMap<>();
    //char array of "RLLLRLLRRLR...."
    private static char[] instructions;
    private static int startingKeysSize;
    private static long numberOfSteps = 0;
    private static List<String> startingKeysList;
    private static String currentKey;

    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december8/input.txt");
        extractInstructionsAndElementsMap( content );

        System.out.println( "instructions created" );
        System.out.println( "element-map created" );

        startingKeysList = getStartingNodes();
        System.out.println("initial Starting Keys: " + startingKeysList);

        startingKeysSize = startingKeysList.size();

        System.out.println("stepsToEndOnZ Lists: ");
        List<List<Long>> toZStepsLists = new ArrayList<>();
        for (String startNode : startingKeysList) {

            String tempKey = startNode;
            currentKey = tempKey;
            numberOfSteps = 0;
            int instructionIndex = 0;
            List<Long> stepsUntilZList = new ArrayList<>();

            while ( stepsUntilZList.size() < 10 ) {

                while ( !currentKey.endsWith("Z") ) {
                    List<Object> results = getNumberOfStepsNeeded( numberOfSteps, instructionIndex );

                    tempKey             = (String) results.get(0);
                    numberOfSteps       = (long) results.get(1);
                    instructionIndex    = (int) results.get(2);
                }

                if( tempKey.endsWith("Z") ) {
                    stepsUntilZList.add( numberOfSteps );

                    if ( instructions[instructionIndex] == 'R' ) {
                        currentKey = elementMap.get( currentKey ).get(1);
                    } else {
                        currentKey = elementMap.get( currentKey ).get(0);
                    }
                }
            }

            toZStepsLists.add( stepsUntilZList );
            System.out.println( stepsUntilZList );
        }

        //compare found stepnumbers that end on Z and extract commons, if existent
//        Set<Long> stepsSet = new HashSet<>( toZStepsLists.get(0) );
//
//        for (int i = 1; i < toZStepsLists.size(); i++) {
//            stepsSet.retainAll( toZStepsLists.get(i) );
//
//            if( stepsSet.isEmpty() ) {
//                System.out.println("no matches found");
//                break;
//            } else {
//                System.out.println("current matches: " + stepsSet);
//            }
//        }
    }

    public static List<Object> getNumberOfStepsNeeded( long numberOfSteps, int instructionIndex ) {

        String tempKey = null;

        if( instructionIndex != 0) {
            System.out.println(currentKey + " " + instructionIndex);
        }

        for ( int i = instructionIndex; i < instructions.length; i++) {
            char instruction = instructions[i];

            tempKey = "" + currentKey;

            //never executed??
            if ( tempKey.endsWith("Z") ) {
                return List.of( tempKey, numberOfSteps, i );
            }

            //set currentKey to the next value depending on the instruction 'R' or 'L'
            if ( instruction == 'R' ) {
                currentKey = elementMap.get( currentKey ).get(1);
            } else {
                currentKey = elementMap.get( currentKey ).get(0);
            }

            numberOfSteps++;
        }

        tempKey = "" + currentKey;
        return List.of( tempKey, numberOfSteps, 0 );
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

    //my implementation to find the lowest common multiple:
    private static long getLowestCommonMultiple(List<Long> numbers) {

        int listSize = numbers.size();

        for (int j = 0; j < listSize; j++) {

            Map<Integer, Long> lcms = new HashMap<>();

            for (int i = 1; i < listSize - j; i++) {
                long a = Math.abs( numbers.get( j ) );
                long b = Math.abs( numbers.get( j+i ) );

                long lcm = getLowestCommonMultiple( a, b );

                lcms.put( i, lcm );
                System.out.println(lcms);
            }

            if ( lcms.size() > 1 )
                return getLowestCommonMultiple( lcms.values().stream().toList() );

        }

        return 0;
    }

    private static long getLowestCommonMultiple(long x, long y){

        //greatest common divisor
        long gcd = getGreatestCommonDivisor( x, y );

        //lowest common multiple = x * y / gcd
        return x * y / gcd;
    }

    private static long getGreatestCommonDivisor(long x, long y){

        if (x == 0 || y == 0) {
            return x + y;
        } else {
            long num1 = Math.abs(x);
            long num2 = Math.abs(y);
            long bigNum = Math.max(num1, num2);
            long smallNum = Math.min(num1, num2);
            return getGreatestCommonDivisor( bigNum % smallNum, smallNum );
        }
    }
}


