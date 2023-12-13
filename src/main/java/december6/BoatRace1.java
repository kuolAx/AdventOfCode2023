package december6;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

public class BoatRace1 {
    public static void main(String[] args) {

        Map<Integer, Integer> timeMap       = Map.of(0,62, 1,64, 2,91, 3,90);
        Map<Integer, Integer> distanceMap   = Map.of(0,553, 1,1010, 2,1473, 3,1074);

        List<Integer> numberOfWaysToBeatEachRace = new ArrayList<>();

        for ( int i = 0; i < timeMap.size() ; i++) {

            int timeOfCurrentRace = timeMap.get(i);
            int distanceOfCurrentRace = distanceMap.get(i);
            long numberOfWaysToWinCurrentRace = IntStream
                    .rangeClosed(1, timeOfCurrentRace)
                    .map(x -> x * (timeOfCurrentRace - x))
                    .filter(x -> x > distanceOfCurrentRace)
                    .count();

            numberOfWaysToBeatEachRace.add( (int) numberOfWaysToWinCurrentRace );
        }

        Integer result = numberOfWaysToBeatEachRace.stream().reduce(1, Math::multiplyExact);
        System.out.println("Number of ways to beat each game multiplied: " + result);

    }
}
