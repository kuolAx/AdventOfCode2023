package december2;

import utils.AdventHelper;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class CubeConundrum2 {
    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december2/input.txt");

        Pattern patternRed = Pattern.compile("(\\d+).red");
        Pattern patternGreen = Pattern.compile("(\\d+).green");
        Pattern patternBlue = Pattern.compile("(\\d+).blue");

        List<List<Integer>> minCubesPerGame = new ArrayList<>();

        Stream.of(content.split("\\n")).forEach( x -> {
                                                            Matcher matchRed = patternRed.matcher(x);
                                                            Matcher matchGreen = patternGreen.matcher(x);
                                                            Matcher matchBlue = patternBlue.matcher(x);

                                                            List <Integer> minCubesThisGame = getMinimumPerColor( matchRed, matchGreen, matchBlue );
                                                            minCubesPerGame.add(minCubesThisGame);
                                                            });

        //sum each sublist and get power
        int power = 0;
        power = minCubesPerGame.stream().map( x -> x.get(0) * x.get(1) * x.get(2) ).reduce(0, Integer::sum);
        System.out.println("Power of all games combined: " + power);
    }

    public static List<Integer> getMinimumPerColor(Matcher matchRed, Matcher matchGreen, Matcher matchBlue){
        List<Integer> minimumCubesNeeded = new ArrayList<>();

        //1. reduce matches from "2 red" to "2" and parse in new list 2. iterate list and find maximum in extra method
        List<Integer> redNumbersList = matchRed.results().map(MatchResult::group).map(x -> Integer.parseInt(x.replaceAll("[^0-9]", ""))).toList();
        minimumCubesNeeded.add( getMaxValueOfColor(redNumbersList) );

        List<Integer> greenNumbersList = matchGreen.results().map(MatchResult::group).map(x -> Integer.parseInt(x.replaceAll("[^0-9]", ""))).toList();
        minimumCubesNeeded.add( getMaxValueOfColor(greenNumbersList) );

        List<Integer> blueNumbersList = matchBlue.results().map(MatchResult::group).map(x -> Integer.parseInt(x.replaceAll("[^0-9]", ""))).toList();
        minimumCubesNeeded.add( getMaxValueOfColor(blueNumbersList) );

        //3. return list with maximum for each color
        return minimumCubesNeeded;
    }

    private static Integer getMaxValueOfColor(List<Integer> colorNumbersList) {
        Integer maxCubesNeeded = 0;

        for (Integer ncurrentNmberOfColor : colorNumbersList) {
            if (ncurrentNmberOfColor > maxCubesNeeded) {
                maxCubesNeeded = ncurrentNmberOfColor;
            }
        }
        return maxCubesNeeded;
    }

}
