package december9;

import utils.AdventHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class MirageMaintenance1 {
    private static final Pattern MATCH_NUMBERS = Pattern.compile("(\\d+)");

    static String testInput = """
            0 3 6 9 12 15
            1 3 6 10 15 21
            10 13 16 21 30 45""";
    public static void main(String[] args) {

//        String[] contentLines = AdventHelper.readFile("./src/main/java/december9/input.txt").split("\n");
        String[] testLines = testInput.split("\n");

        List<List<Integer>> sensorHistories = getSensorHistories( testLines );

        List<Integer> extrapolatedSensorValues = new ArrayList<>();
        for (List<Integer> sensorHistory : sensorHistories) {
            extrapolatedSensorValues.add( extrapolateSensorValue( sensorHistory ) );
        }

        System.out.println( extrapolatedSensorValues );
        System.out.println("sum of extrapolated values: " + extrapolatedSensorValues.stream().reduce(0, Integer::sum));
    }

    private static Integer extrapolateSensorValue(List<Integer> sensorHistory, int lastElement) {

        List<Integer> differences = new ArrayList<>();

        IntStream
                .range( 0, sensorHistory.size()-1 )
                .forEach( i -> differences.add( Math.abs(sensorHistory.get(i) - sensorHistory.get(i+1)) ) );

        if( differences.stream().distinct().toList().size() > 1 && differences.get(0) != 0)
            return extrapolateSensorValue( differences, differences.get( differences.size()-1 ) );

        return lastElement + differences.get(0);
    }
    private static Integer extrapolateSensorValue(List<Integer> sensorHistory) {
        return extrapolateSensorValue( sensorHistory, sensorHistory.get( sensorHistory.size()-1 ) );
    }

    private static List<List<Integer>> getSensorHistories( String[] contentLines ) {

        List<List<Integer>> sensorHistories = new ArrayList<>();

        for (String contentLine : contentLines) {
            Matcher matchNumbers = MATCH_NUMBERS.matcher(contentLine);

            List<Integer> sensorHistory = matchNumbers.results().map(MatchResult::group).map(Integer::valueOf).toList();
            sensorHistories.add( sensorHistory );
        }

        return sensorHistories;
    }
}
