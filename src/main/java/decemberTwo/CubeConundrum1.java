package decemberTwo;

import utils.AdventHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CubeConundrum1 {

    static final int MAXNUMBEROFREDS = 12;
    static final int MAXNUMBEROFGREENS = 13;
    static final int MAXNUMBEROFBLUES = 14;
    public static void main(String[] args) {

        String testInput = """
                           Game 1: 1 green, 2 red, 6 blue; 4 red, 1 green, 3 blue; 7 blue, 5 green; 6 blue, 2 red, 1 green
                           Game 2: 1 green, 17 red; 1 blue, 6 red, 7 green; 2 blue, 4 red, 7 green; 1 green, 6 red, 2 blue
                           Game 3: 6 red, 15 blue, 15 green; 1 green, 4 red, 12 blue; 14 blue, 9 red, 1 green; 2 red, 15 blue, 12 green
                           """;

        String content = AdventHelper.readFile("./src/main/java/DecemberTwo/input.txt");

        Pattern patternGameNumber = Pattern.compile("Game.(\\d+)");
        Pattern patternRed = Pattern.compile("(\\d+).red");
        Pattern patternGreen = Pattern.compile("(\\d+).green");
        Pattern patternBlue = Pattern.compile("(\\d+).blue");

        List<Integer> playableGamesList = new ArrayList<>();

        Stream.of(content.split("\\n")).forEach( x -> {
                                                          System.out.println("current line: " + x);
                                                          Matcher matchRed = patternRed.matcher(x);
                                                          Matcher matchGreen = patternGreen.matcher(x);
                                                          Matcher matchBlue = patternBlue.matcher(x);

                                                          Matcher matchGameNumber = patternGameNumber.matcher(x);

                                                          if( isPlayableGame( matchRed, matchGreen, matchBlue ) )
                                                              playableGamesList.add( Integer.parseInt( matchGameNumber.results().toList().get(0).group(1) ));
                                                          });

        System.out.println("Playable Games Sum: " + playableGamesList.stream().reduce(0, Integer::sum));
    }

    public static boolean isPlayableGame(Matcher matchRed, Matcher matchGreen, Matcher matchBlue){
        boolean isGamePlayable = true;

        List<Integer> redNumbersList = matchRed.results().map(MatchResult::group).map(x -> Integer.parseInt(x.replaceAll("[^0-9]", ""))).toList();
        isGamePlayable = listContainsNumberGreaterThan(isGamePlayable, redNumbersList, MAXNUMBEROFREDS);

        List<Integer> greenNumbersList = matchGreen.results().map(MatchResult::group).map(x -> Integer.parseInt(x.replaceAll("[^0-9]", ""))).toList();
        isGamePlayable = listContainsNumberGreaterThan(isGamePlayable, greenNumbersList, MAXNUMBEROFGREENS);

        List<Integer> blueNumbersList = matchBlue.results().map(MatchResult::group).map(x -> Integer.parseInt(x.replaceAll("[^0-9]", ""))).toList();
        isGamePlayable = listContainsNumberGreaterThan(isGamePlayable, blueNumbersList, MAXNUMBEROFBLUES);

        return isGamePlayable;
    }

    private static boolean listContainsNumberGreaterThan(boolean isGamePlayable, List<Integer> colorNumbersList, int maxAmountOfColor) {
        for (Integer numberOfColor : colorNumbersList) {
            if (numberOfColor > maxAmountOfColor) {
                isGamePlayable = false;
                break;
            }
        }
        return isGamePlayable;
    }

}
