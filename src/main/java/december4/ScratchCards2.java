package december4;

import utils.AdventHelper;

import java.util.ArrayList;
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

        String testInput = """
                Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
                Card 2: 13 32 20 16 61 | 61 30 68 82 17 32 24 19
                Card 3:  1 21 53 59 44 | 69 82 63 72 16 21 14  1
                Card 4: 41 92 73 84 69 | 59 84 76 51 58  5 54 83
                Card 5: 87 83 26 28 32 | 88 30 70 12 93 22 82 36
                Card 6: 31 18 13 56 72 | 74 77 10 23 35 67 36 11
                """;

        String content = AdventHelper.readFile("./src/main/java/december4/input.txt");

        String[] scratchCardLines = content.split("\\n");
//        String[] scratchCardLines = testInput.split("\\n");

        //Map< cardNumber, List( winningNumbersCount, CopiesOfCardAmount )
        Map<Integer,List<Integer>> cardNumberToWinningNumbersAndCopiesOfCardMap = getNumberOfWinningsForAllScratchCards( scratchCardLines );

        System.out.println("Card : WinningNumberCount before alterations" + cardNumberToWinningNumbersAndCopiesOfCardMap);

        Integer totalNumberOfScratchCards = computeTotalNumberOfScratchCards( cardNumberToWinningNumbersAndCopiesOfCardMap );

        System.out.println("Card : WinningNumberCount after alterations" + cardNumberToWinningNumbersAndCopiesOfCardMap);
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

            cardNumberTowinningNumberCountMap.put( cardNumber, Arrays.asList( actuallyWinningNumbers.size(), /*amountOfCardCopies*/ 1 ) );

            cardNumber++;
        }

        return cardNumberTowinningNumberCountMap;
    }

    private static Integer computeTotalNumberOfScratchCards(Map<Integer,List<Integer>> map) {

        //has to be looped for every card individually
        for (int i = 1; i < map.size()+1; i++) {
            List<Integer> winningNumbersCount = map.get(i);

            //has to be looped for every copy of the card currently existing
            for (int j = 0; j < map.get(i).get(1); j++) {

                //has to be looped for the amount of winning numbers to increment following cards
                for (int k = 1; k < winningNumbersCount.get(0) + 1; k++) {
                    //increment following amount of cards by 1 for each winningNumber in current card
                    if (i + k < map.size() + 1) {
                        map.put( i + k, incrementCardCopiesAmount(map.get(i + k)) );
//                        System.out.println("current Iteration: Card : WinningNumberCount" + map);
                    }
                }
            }
        }

        //compute total number of cards from altered Map
        return map.values().stream().map( v -> v.get(1) ).reduce(0, Integer::sum);
    }

    private static Integer computeTotalNumberOfScratchCards(Map<Integer, List<Integer>> map, int x) {
        int totalCards = 0;

        for (int i = 1; i <= map.size(); i++) {
            List<Integer> winningNumbersAndCopiesList = map.get(i);

            int copiesOfCard = winningNumbersAndCopiesList.get(1);

            if (copiesOfCard > 0) {
                totalCards += copiesOfCard;

                int winningNumbersCount = winningNumbersAndCopiesList.get(0);

                // Increment following amount of cards by 1 for each winningNumber in current card
                for (int k = 1; k <= winningNumbersCount; k++) {
                    int nextCardIndex = i + k;

                    if (nextCardIndex <= map.size()) {
                        map.computeIfPresent(nextCardIndex, (key, value) -> incrementCardCopiesAmount(value));
                    }
                }
            }
        }

        return totalCards;
    }

    //increase number of copies of given card by 1
    private static List<Integer> incrementCardCopiesAmount(List<Integer> amountList){

        Integer cardCopies = amountList.get(1);
        cardCopies++;
        amountList.set(1,cardCopies);

        return amountList;
    }

}
