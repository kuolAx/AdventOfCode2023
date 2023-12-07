package december3;

import utils.AdventHelper;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GearRatios1 {
    public static void main(String[] args) {

        String testinput = """
                467..114.3
                ...*......
                ..35..633.
                ......#2..
                617*.....3
                .....+.58.
                ..592.....
                ..-...755.
                ...$.*....
                .664.598..
                """;

        String content = AdventHelper.readFile("./src/main/java/december3/input.txt");

        Pattern patternSymbols = Pattern.compile("[@!§&%$+\\-/=#*]");
        Pattern patternNumbers = Pattern.compile("\\d+");

        //Map -> { lineNumber : List( IndexOfSymbol1, IndexOfSymbol2, ...) }
        HashMap<Integer, List<Integer>> symbolsIndices = new HashMap<>();

        //Map -> { lineNumber : List( [startIndex, endIndex], [startIndex, endIndex], ...) }
        HashMap<Integer, List<int[]>> numberStartAndEndIndexMap = new HashMap<>();

        List<String> schematicLines = Arrays.asList(content.split( "\\n" ));
        int currentLine = 1;

        //extract indices of symbols and numbers
        for ( String schematicLine: schematicLines ) {

            //fill symbols Map
            Matcher matchSymbols = patternSymbols.matcher(schematicLine);
            symbolsIndices.put( currentLine, matchSymbols.results().map(MatchResult::start).toList() );

            //if no symbols are present in the current line -> put an empty list to indicate "no symbols found"
            if( symbolsIndices.getOrDefault( currentLine, List.of() ).isEmpty() )
                symbolsIndices.put( currentLine, List.of() );

            //fill numbers Map
            Matcher matchNumbers = patternNumbers.matcher(schematicLine);
            numberStartAndEndIndexMap.put( currentLine, matchNumbers.results().map( x -> new int[] {x.start(),x.end()} ).toList() );

            currentLine++;
        }

        List<Integer> partNumbersList = new ArrayList<>();

        //level: lineNumber
        for (int i = 1; i <= numberStartAndEndIndexMap.size(); i++) {
            System.out.print( "number indices for line " + i + ": " );

            //level: value of Map (List<int[]>)
            for (int j = 0; j < numberStartAndEndIndexMap.get(i).size(); j++) {
                System.out.print( Arrays.toString(numberStartAndEndIndexMap.get(i).get(j) ));

                //int[v1,v2] - always has 2 values
                int[] currentNumberStartEndIndex = numberStartAndEndIndexMap.get(i).get(j);

                Set<Integer> indicesToCheck = new HashSet<>();

                //prepare list with all indices to look at for symbols
                for (int l = currentNumberStartEndIndex[0]; l < currentNumberStartEndIndex[1]; l++) {

                    if( l != 0 )
                        indicesToCheck.add(l-1);

                    if( l != schematicLines.get(0).length() )
                        indicesToCheck.add(l+1);

                    indicesToCheck.add(l);
                }

                //compare symbolsIndices and numbersIndices to evaluate wether number is a part number
                //disjoint( Collection c1, Collection c2 ) -> Returns true if the two specified collections have no elements in common.
                boolean adjacentSymbolsSameLine = !Collections.disjoint( symbolsIndices.getOrDefault( i, List.of() ), indicesToCheck );
                boolean adjacentSymbolsAbove    = !Collections.disjoint( symbolsIndices.getOrDefault(i - 1, List.of() ), indicesToCheck );
                boolean adjacentSymbolsBelow    = !Collections.disjoint( symbolsIndices.getOrDefault(i + 1, List.of() ), indicesToCheck );

                if( adjacentSymbolsAbove || adjacentSymbolsSameLine || adjacentSymbolsBelow ) {
                    //i-1 because lines start at "1", but list-index starts at 0
                    partNumbersList.add( Integer.parseInt( schematicLines.get(i-1).substring( currentNumberStartEndIndex[0], currentNumberStartEndIndex[1] ) ));
                }

            }
            System.out.println();
        }

        int finalresult = partNumbersList.stream().reduce(0, Integer::sum);
        System.out.print("Part numbers: ");
        partNumbersList.forEach(x -> System.out.print(x + " "));
        System.out.println();
        System.out.println("Sum of Part Numbers: " + finalresult);

        }
    }
