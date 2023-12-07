package december3;

import utils.AdventHelper;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GearRatios2 {

    private static final Pattern PATTERN_STAR_SYMBOL = Pattern.compile("[*]");
    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+");

    //Map -> { lineNumber : List( IndexOfStar1, IndexOfStar2, ...) }
    private static final HashMap<Integer, List<Integer>> starIndices = new HashMap<>();

    //Map -> { lineNumber : List( [startIndex, endIndex], [startIndex, endIndex], ...) }
    private static final HashMap<Integer, List<int[]>> numberStartAndEndIndexMap = new HashMap<>();

    //Map -> { star-id<Integer> : List( adjacentNumber1, adjacentNumber2... ) }. star-id = "lineNumber" + "star-index"
    private static final HashMap<Integer, List<Integer>> starIDToAdjacentNumbersMap = new HashMap<>();
    private static List<String> schematicLines;

    public static void main(String[] args) {

        String testinput = """
                467..114.3
                ...*....*.
                ..35..633.
                ......#2..
                617*.....3
                .....+.58*
                ..592.....
                ..-...755.
                ...$.*...2
                .664.598.*
                ..........
                .......*..
                """;

        String content = AdventHelper.readFile("./src/main/java/december3/input.txt");

//        schematicLines = Arrays.asList( content.split("\\n") );
        schematicLines = Arrays.asList( testinput.split("\\n") );

        //extract indices of star-symbols and numbers, then map to lineNumber
        for (int i = 0; i < schematicLines.size(); i++) {
            String schematicLine = schematicLines.get(i);
            int currentLineNumber = i+1;

            //fill stars indices Map
            Matcher matchStars = PATTERN_STAR_SYMBOL.matcher(schematicLine);
            starIndices.put( currentLineNumber , matchStars.results().map(MatchResult::start).toList() );

            //if no stars are present in the current line -> put an empty list to indicate "no stars found"
            if (starIndices.getOrDefault( currentLineNumber, List.of()).isEmpty() )
                starIndices.put( currentLineNumber, List.of() );

            //fill numbers indices Map
            Matcher matchNumbers = PATTERN_NUMBERS.matcher(schematicLine);
            numberStartAndEndIndexMap.put( currentLineNumber, matchNumbers.results().map(x -> new int[]{x.start(), x.end()}).toList());
        }

        //level: lineNumber
        for (int lineNumber = 1; lineNumber <= schematicLines.size(); lineNumber++) {
            System.out.print( "current line: " + lineNumber + " " );

            //level: value of Map (List<int[]>) -> "loop through every number (int[]) found in the line we are looking at"
            for (int j = 0; j < numberStartAndEndIndexMap.get(lineNumber).size(); j++) {
                System.out.print( Arrays.toString(numberStartAndEndIndexMap.get(lineNumber).get(j) ));

                //int[v1,v2] - always has 2 values
                int[] currentNumberStartEndIndex = numberStartAndEndIndexMap.get(lineNumber).get(j);

                //prepare list with all indices to look at for symbols in the line above,same,below.
                Set<Integer> indicesToCheck = retrieveSearchIndexList( currentNumberStartEndIndex );

                //fill Map<star-id, adjacentNumbers> for the current line
                compareStarIndicesWithNumberIndices( lineNumber, indicesToCheck, currentNumberStartEndIndex );
            }
            System.out.println();
            System.out.println("currentMap: " + starIDToAdjacentNumbersMap);
        }

        List<Integer> gearRatioList = new ArrayList<>();

        //extract star symbols with EXACTLY 2 adjacent numbers. Then multiply those 2 numbers for the resulting gear ratio
        starIDToAdjacentNumbersMap.forEach( (k, v) -> { if( v.size() == 2 )
                                                            gearRatioList.add(v.stream().reduce(1, Math::multiplyExact)); });
        int gearRatioSum = gearRatioList.stream().reduce(0,Integer::sum);

        System.out.println();
        System.out.println("Sum of gear ratios: " + gearRatioSum);
    }

    private static Set<Integer> retrieveSearchIndexList(int[] currentNumberStartEndIndex) {
        Set<Integer> indicesToCheck = new HashSet<>();

        for (int i = currentNumberStartEndIndex[0]; i < currentNumberStartEndIndex[1]; i++) {

            if( i != 0 )
                indicesToCheck.add(i-1);

            if( i != schematicLines.get(0).length() )
                indicesToCheck.add(i+1);

            indicesToCheck.add(i);
        }
        return indicesToCheck;
    }

    private static void compareStarIndicesWithNumberIndices(int line, Set<Integer> indicesToCheck, int[] currentNumberStartEndIndex) {
        //do for previous, current, and next line
        for (int currentLine = line-1; currentLine < line+2; currentLine++) {

            //do for every star-symbol found in the current line being looked at
            for (Integer starIndex : starIndices.getOrDefault( currentLine, List.of() )) {

                Integer starID = Integer.valueOf(currentLine + "" + starIndex);

                starIDToAdjacentNumbersMap.computeIfAbsent( starID, x -> new ArrayList<>());

                if (indicesToCheck.contains(starIndex)) {

                    //Map.put( Integer, List<Integer>)
                    starIDToAdjacentNumbersMap
                            .computeIfPresent( starID, (key, value) -> {
                                                                        value.add( Integer.valueOf( schematicLines.get(line-1).substring( currentNumberStartEndIndex[0], currentNumberStartEndIndex[1] )));
                                                                        return value;
                                                                });
                }
            }
        }
    }
}
