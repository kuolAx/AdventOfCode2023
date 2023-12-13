package december6;

import java.util.stream.LongStream;

public class BoatRace2 {
    public static void main(String[] args) {

        long time = 62649190;
        long minDistance = 553101014731074L;

        long numberOfWaysToWinCurrentRace = LongStream
                .rangeClosed(1, time)
                .map(x -> x * (time - x))
                .filter(x -> x > minDistance)
                .count();

        System.out.println("Number of ways to beat the game: " + numberOfWaysToWinCurrentRace);
    }
}
