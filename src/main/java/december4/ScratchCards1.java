package december4;

import utils.AdventHelper;

import java.util.List;
import java.util.Set;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ScratchCards1 {
    private static final Pattern PATTERN_NUMBERS = Pattern.compile("\\d+");
    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december4/input.txt");

        String[] scratchCardLines = content.split("\\n");

        int pointValueOfScratchCards = 0;

        for (String scratchCardLine : scratchCardLines) {

            System.out.println("cur: " + scratchCardLine);

            String[] numberSequences = scratchCardLine.split("\\|");
            Matcher matchWinningNumbers = PATTERN_NUMBERS.matcher( numberSequences[0].replaceAll("Card\s+\\d+:", "") );
            Matcher matchMyNumbers      = PATTERN_NUMBERS.matcher( numberSequences[1] );

            List<Integer> winningNumbers = matchWinningNumbers.results().map(MatchResult::group).map(Integer::valueOf).toList();
            List<Integer> myNumbers      = matchMyNumbers.results().map(MatchResult::group).map(Integer::valueOf).toList();

            Set<Integer> actuallyWinningNumbers = winningNumbers.stream().filter(myNumbers::contains).collect(Collectors.toSet());
            System.out.print( actuallyWinningNumbers.size() + " match(es) - ");

            pointValueOfScratchCards += (int) Math.pow( 2, actuallyWinningNumbers.size()-1 );
            System.out.println("worth: " + (int) Math.pow( 2, actuallyWinningNumbers.size()-1));
        }

        System.out.println("Point value of all scratch cards: " + pointValueOfScratchCards);
    }
}
