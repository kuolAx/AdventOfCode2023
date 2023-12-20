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

        String[] contentLines = AdventHelper.readFile("./src/main/java/december9/input.txt").split("\n");
//        String[] contentLines = testInput.split("\n");

        List<List<Integer>> sensorHistories = getSensorHistories( contentLines );
        List<Integer> extrapolatedSensorValues = new ArrayList<>();

        for (List<Integer> history : sensorHistories) {

//            history = List.of(12, 12, 11, 5, 5, 5, 40, 194, 558, 1278, 2553, 4643, 7877, 12661, 19486, 28936, 41696, 58560, 80439, 108369, 143519);

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
        if( differences.size() == 1 && differences.stream().distinct().toList().get(0) != 0 )
            throw new RuntimeException("list could not be reduced to only zeroes");

        return lastElement + extrapolateSensorValue( differences, differences.get( differences.size()-1 ) );
    }

    private static List<List<Integer>> getSensorHistories( String[] contentLines ) {

        List<List<Integer>> sensorHistories = new ArrayList<>();

        for (int i = 0; i < contentLines.length; i++) {
            String contentLine = contentLines[i];
            Matcher matchNumbers = MATCH_NUMBERS.matcher(contentLine);

            List<Integer> sensorHistory = matchNumbers.results().map(MatchResult::group).map(Integer::valueOf).toList();
            sensorHistories.add(sensorHistory);
        }

        return sensorHistories;
    }
}
