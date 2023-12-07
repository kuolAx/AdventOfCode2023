package decemberThree;

import utils.AdventHelper;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GearRatios2 {

    private static final Pattern PATTERN_STAR_SYMBOL = Pattern.compile("[*]");
    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+");

    //Map -> { lineNumber : List( IndexOfStar1, IndexOfStar2, ...) }
    private static final HashMap<Integer, List<Integer>> STAR_INDICES = new HashMap<>();

    //Map -> { lineNumber : List( [startIndex, endIndex], [startIndex, endIndex], ...) }
    private static final HashMap<Integer, List<int[]>> NUMBER_START_AND_END_INDEX_MAP = new HashMap<>();

    //Map -> { star-id<Integer> : List( adjacentNumer1, adjacentNumber2... ) }. star-id = "linenumber" + "star-index"
    private static final HashMap<Integer, List<Integer>> STAR_ID_TO_ADJACENT_NUMBERS_MAP = new HashMap<>();

    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/DecemberThree/input.txt");

        List<String> schematicLines = Arrays.asList( content.split("\\n") );
        int currentLine = 1;

        //extract indices of star-symbols and numbers mapped to lineNumber
        for ( String schematicLine: schematicLines ) {

            //fill stars indices Map
            Matcher matchStars = PATTERN_STAR_SYMBOL.matcher(schematicLine);
            STAR_INDICES.put( currentLine, matchStars.results().map(MatchResult::start).toList() );

            //if no stars are present in the current line -> put an empty list to indicate "no stars found"
            if( STAR_INDICES.getOrDefault( currentLine, List.of() ).isEmpty() )
                STAR_INDICES.put( currentLine, List.of() );

            //fill numbers indices Map
            Matcher matchNumbers = PATTERN_NUMBERS.matcher(schematicLine);
            NUMBER_START_AND_END_INDEX_MAP.put( currentLine, matchNumbers.results().map(x -> new int[] {x.start(),x.end()} ).toList() );

            currentLine++;
        }

        //level: lineNumber
        for (int i = 1; i <= NUMBER_START_AND_END_INDEX_MAP.size(); i++) {
            System.out.print( "current line: " + i + " " );

            //level: value of Map (List<int[]>)
            for (int j = 0; j < NUMBER_START_AND_END_INDEX_MAP.get(i).size(); j++) {
                System.out.print( Arrays.toString(NUMBER_START_AND_END_INDEX_MAP.get(i).get(j) ));

                //int[v1,v2] - always has 2 values
                int[] currentNumberStartEndIndex = NUMBER_START_AND_END_INDEX_MAP.get(i).get(j);

                Set<Integer> indicesToCheck = new HashSet<>();

                //prepare list with all indices to look at for symbols
                for (int l = currentNumberStartEndIndex[0]; l < currentNumberStartEndIndex[1]; l++) {

                    if( l != 0 )
                        indicesToCheck.add(l-1);

                    if( l != schematicLines.get(0).length() )
                        indicesToCheck.add(l+1);

                    indicesToCheck.add(l);
                }

                //sameLine
                for ( Integer starIndex : STAR_INDICES.getOrDefault( i, List.of() ) ) {
                    if ( indicesToCheck.contains(starIndex) ) {
                        //Map.put( Integer, List<Integer>)
                        STAR_ID_TO_ADJACENT_NUMBERS_MAP.put( Integer.valueOf(i+""+starIndex), STAR_ID_TO_ADJACENT_NUMBERS_MAP.getOrDefault( Integer.valueOf(i+""+starIndex), new ArrayList<>() ));
                        //Map.get(Integer) -> List<Integer>.add( Integer(currentAdjacentNumber) )
                        STAR_ID_TO_ADJACENT_NUMBERS_MAP.get( Integer.valueOf(i+""+starIndex) ).add( Integer.valueOf( schematicLines.get(i-1).substring( currentNumberStartEndIndex[0], currentNumberStartEndIndex[1] ) ) );
                    }
                }
                //lineAbove
                for ( Integer starIndex : STAR_INDICES.getOrDefault( i-1, List.of() ) ) {
                    if ( indicesToCheck.contains(starIndex) ) {
                        //Map.put( Integer, List<Integer>)
                        STAR_ID_TO_ADJACENT_NUMBERS_MAP.put( Integer.valueOf((i-1)+""+starIndex), STAR_ID_TO_ADJACENT_NUMBERS_MAP.getOrDefault( Integer.valueOf((i-1)+""+starIndex), new ArrayList<>() ));
                        //Map.get(Integer) -> List<Integer>.add( Integer(currentAdjacentNumber) )
                        STAR_ID_TO_ADJACENT_NUMBERS_MAP.get( Integer.valueOf((i-1)+""+starIndex) ).add( Integer.valueOf( schematicLines.get(i-1).substring( currentNumberStartEndIndex[0], currentNumberStartEndIndex[1] ) ) );
                    }
                }
                //lineBelow
                for ( Integer starIndex : STAR_INDICES.getOrDefault( i+1, List.of() ) ) {
                    if ( indicesToCheck.contains(starIndex) ) {
                        //Map.put( Integer, List<Integer>)
                        STAR_ID_TO_ADJACENT_NUMBERS_MAP.put( Integer.valueOf((i+1)+""+starIndex), STAR_ID_TO_ADJACENT_NUMBERS_MAP.getOrDefault( Integer.valueOf((i+1)+""+starIndex), new ArrayList<>() ));
                        //Map.get(Integer) -> List<Integer>.add( Integer(currentAdjacentNumber) )
                        STAR_ID_TO_ADJACENT_NUMBERS_MAP.get( Integer.valueOf((i+1)+""+starIndex) ).add( Integer.valueOf( schematicLines.get(i-1).substring( currentNumberStartEndIndex[0], currentNumberStartEndIndex[1] ) ) );
                    }
                }
            }
            System.out.println();
            System.out.println("currentMap: " + STAR_ID_TO_ADJACENT_NUMBERS_MAP);
        }

        List<Integer> gearRatioList = new ArrayList<>();

        //extract star symbols with exactly 2 adjacent numbers AND multiply those 2 numbers for the resulting gear ratio
        STAR_ID_TO_ADJACENT_NUMBERS_MAP.forEach( (k, v) -> { if( v.size() == 2 )
                                                                gearRatioList.add( v.stream().reduce(1, Math::multiplyExact) ); });

        int finalresult = gearRatioList.stream().reduce(0,Integer::sum);

        System.out.println();
        System.out.println("Sum of gear ratios: " + finalresult);
        System.out.println("Number of Lines" + schematicLines.size());
    }
}
