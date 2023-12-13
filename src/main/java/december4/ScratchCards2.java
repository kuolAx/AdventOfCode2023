package december4;

import utils.AdventHelper;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ScratchCards2 {
    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+");
    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december4/input.txt");

        String[] scratchCardLines = content.split("\\n");

        //Map< cardNumber, List( winningNumbersCount, CopiesOfCardAmount )
        Map<Integer,List<Integer>> cardNumberToWinningNumbersAndCopiesOfCardMap = getNumberOfWinningsForAllScratchCards( scratchCardLines );

        System.out.println("Card : (WinningNumberCount, Copies) before alterations" + cardNumberToWinningNumbersAndCopiesOfCardMap);

        Integer totalNumberOfScratchCards = computeTotalNumberOfScratchCards( cardNumberToWinningNumbersAndCopiesOfCardMap );

        System.out.println("Card : (WinningNumberCount, Copies) after alterations" + cardNumberToWinningNumbersAndCopiesOfCardMap);
        System.out.println("Total number of scratchcards: " + totalNumberOfScratchCards);
    }

    private static Map<Integer,List<Integer>> getNumberOfWinningsForAllScratchCards( String[] scratchCardLines ) {

        Map<Integer,List<Integer>> cardNumberTowinningNumberCountMap = new HashMap<>();

        int cardNumber = 1;

        for (String scratchCardLine : scratchCardLines) {

            String[] numberSequences = scratchCardLine.split("\\|");
            Matcher matchWinningNumbers = PATTERN_NUMBERS.matcher( numberSequences[0].replaceAll("Card\s+\\d+:", "") );
            Matcher matchMyNumbers      = PATTERN_NUMBERS.matcher( numberSequences[1] );

            List<Integer> winningNumbers = matchWinningNumbers.results().map(MatchResult::group).map(Integer::valueOf).toList();
            List<Integer> myNumbers      = matchMyNumbers.results().map(MatchResult::group).map(Integer::valueOf).toList();

            Set<Integer> actuallyWinningNumbers = winningNumbers.stream().filter(myNumbers::contains).collect(Collectors.toSet());

            System.out.println( scratchCardLine );
            System.out.println( actuallyWinningNumbers.size() + " match(es)" );

            cardNumberTowinningNumberCountMap.put( cardNumber, Arrays.asList( actuallyWinningNumbers.size(), /*defaultAmountOfCardCopies*/ 1 ) );

            cardNumber++;
        }

        return cardNumberTowinningNumberCountMap;
    }

    private static Integer computeTotalNumberOfScratchCards(Map<Integer,List<Integer>> map) {

        //has to be looped for every card individually
        for (int i = 1; i < map.size()+1; i++) {
            List<Integer> winningNumbersCount = map.get(i);

            //has to be looped for every copy of the card currently existing
            Integer amountOfCardCopies = map.get(i).get(1);
            for (int j = 0; j < amountOfCardCopies; j++) {

                //has to be looped for the amount of winning numbers to increment following cards
                for (int k = 1; k < winningNumbersCount.get(0) + 1; k++) {

                    //increment following "number of copies" by 1 for each winningNumber in current card
                    if (i + k < map.size() + 1) {
                        map.put( i + k, incrementCardCopiesAmount(map.get(i + k)) );
                    }
                }
            }
        }

        //compute total number of cards from altered Map
        return map.values().stream().map( v -> v.get(1) ).reduce(0, Integer::sum);
    }

    private static List<Integer> incrementCardCopiesAmount(List<Integer> winningNumbersAndCardCopiesList){

        Integer cardCopies = winningNumbersAndCardCopiesList.get(1);
        cardCopies++;
        winningNumbersAndCardCopiesList.set(1,cardCopies);

        return winningNumbersAndCardCopiesList;
    }

}
