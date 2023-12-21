package december9;

import utils.AdventHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

//almost identical to part1 -> just reversed the lists before computing new history entry
public class MirageMaintenance2 {
    private static final Pattern MATCH_NUMBERS = Pattern.compile("(-*\\d+)");
    public static void main(String[] args) {

        String[] contentLines = AdventHelper.readFile("./src/main/java/december9/input.txt").split("\n");

        List<List<Integer>> sensorHistories = getSensorHistories( contentLines );
        List<Integer> extrapolatedSensorValues = new ArrayList<>();

        for (List<Integer> history : sensorHistories) {

            int lastElement = history.get( history.size()-1 );
            try {
                extrapolatedSensorValues.add( extrapolateSensorValue( history, lastElement ) );
            } catch (Exception e) {
                System.out.println("exception history: " + history);
            }
        }

        System.out.println( extrapolatedSensorValues );
        System.out.println("sum of extrapolated values: " + extrapolatedSensorValues.stream().reduce(0, Integer::sum));
    }

    private static Integer extrapolateSensorValue(List<Integer> sensorHistory, int lastElement) {

        List<Integer> differences = new ArrayList<>();

        IntStream
                .range( 0, sensorHistory.size()-1 )
                .forEach( i -> differences.add( sensorHistory.get(i+1) - sensorHistory.get(i) ) );

        if( differences.stream().distinct().toList().size() == 1 && differences.get(0) == 0)
            return lastElement + differences.get( differences.size()-1 );

        if( differences.size() == 1 && differences.get(0) != 0 )
            throw new RuntimeException("list could not be reduced to only zeroes");

        //if differences list is not all zeroes -> see if the next differences layer is and get me the result of that
        return lastElement + extrapolateSensorValue( differences, differences.get( differences.size()-1 ) );
    }

    private static List<List<Integer>> getSensorHistories( String[] contentLines ) {

        List<List<Integer>> sensorHistories = new ArrayList<>();

        for (String contentLine : contentLines) {

            Matcher matchNumbers = MATCH_NUMBERS.matcher(contentLine);

            List<Integer> sensorHistory = matchNumbers.results().map(MatchResult::group).map(Integer::valueOf).collect(Collectors.toList());

            Collections.reverse(sensorHistory);
            sensorHistories.add(sensorHistory);
        }

        return sensorHistories;
    }
}
