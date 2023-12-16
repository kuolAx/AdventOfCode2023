package december7;

import utils.AdventHelper;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class CamelCards1 {
    public static enum Card{
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JOKER, QUEEN, KING, ACE
    }
    public static enum HandType {
        HIGHCARD, ONEPAIR, TWOPAIR, THREEOAK, FULLHOUSE, FOUROAK, FIVEOAK
    }
    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december7/input.txt");

        //read input lines and create map of rank to cards, handtype and bid for further computing
        // -> Map { [ Integer rank -> [card1,2,3,4,5,Handtype(int),Bid(int)] }
        Map<Integer, List<Integer>> rankMap = readInputAndCreateRankToCardsHandTypeBidMap( content );

        //sort lists with cards, handType and bid by given parameters using custom comparator "cardComparator"
        List<List<Integer>> sortedList = rankMap.values().stream().sorted( cardComparator ).toList();

        //extract bid values and multiply by corresponding rank -> final result is the sum of the multiplied values
        AtomicInteger counter = new AtomicInteger(1);
        Integer sumOfMultipliedBids = sortedList.stream().map(list -> list.get(6)).map(bid -> bid * counter.getAndIncrement()).reduce(0, Integer::sum);

        System.out.println(counter.get());
        System.out.println("All hands ranked by values bids multiplied are: " + sumOfMultipliedBids);
    }

    private static Map<Integer, List<Integer>> readInputAndCreateRankToCardsHandTypeBidMap(String content) {
        Map<Integer, List<Integer>> rankMap = new HashMap<>();

        int i = 1;
        for (String contentLine : content.split("\n")) {

            List<Integer> cardList = new ArrayList<>();

            //generate cardList
            for (int j = 0; j < 5; j++) {
                char currentChar = contentLine.charAt(j);

                int currentCardValue = switch( currentChar ){
                    case '2' -> Card.TWO.ordinal();
                    case '3' -> Card.THREE.ordinal();
                    case '4' -> Card.FOUR.ordinal();
                    case '5' -> Card.FIVE.ordinal();
                    case '6' -> Card.SIX.ordinal();
                    case '7' -> Card.SEVEN.ordinal();
                    case '8' -> Card.EIGHT.ordinal();
                    case '9' -> Card.NINE.ordinal();
                    case 'T' -> Card.TEN.ordinal();
                    case 'J' -> Card.JOKER.ordinal();
                    case 'Q' -> Card.QUEEN.ordinal();
                    case 'K' -> Card.KING.ordinal();
                    case 'A' -> Card.ACE.ordinal();
                    default -> throw new RuntimeException("Switch default reached.");
                };
                cardList.add(currentCardValue);
            }

            //generate handtype and add at the end of cardList
            cardList.add( getHandTypeOfCurrentHand(cardList) );

            //find bid and add to end of cardList
            int bid = Integer.parseInt(contentLine.substring(6).trim());
            cardList.add( bid );

            rankMap.put(i, cardList);
            i++;
        }

        return rankMap;
    }

    private static int getHandTypeOfCurrentHand(List<Integer> cardList) {
        StringBuilder cardType = new StringBuilder();

        Map<Integer, Long> counts =
                cardList.stream().collect( Collectors.groupingBy( val -> val, Collectors.counting()));

        counts.values().stream().filter(val -> val != 0).sorted().forEach(cardType::append);

        switch( cardType.toString() ){
            case "5"        -> { return HandType.FIVEOAK.ordinal(); }
            case "14"       -> { return HandType.FOUROAK.ordinal(); }
            case "23"       -> { return HandType.FULLHOUSE.ordinal(); }
            case "113"      -> { return HandType.THREEOAK.ordinal(); }
            case "122"      -> { return HandType.TWOPAIR.ordinal(); }
            case "1112"     -> { return HandType.ONEPAIR.ordinal(); }
            case "11111"    -> { return HandType.HIGHCARD.ordinal(); }
            default         -> throw new RuntimeException("HandType could not be determined");
        }
    }

    //o1 > o2 -> return 1
    //o1 < o2 -> return -1
    public static Comparator<List<Integer>> cardComparator = new Comparator<List<Integer>>() {
        @Override
        public int compare(List<Integer> o1, List<Integer> o2) {

            int HandTypeO1 = o1.get(5);
            int HandTypeO2 = o2.get(5);

            if ( HandTypeO1 > HandTypeO2 )
                return 1;
            if ( HandTypeO1 < HandTypeO2 )
                return -1;

            int count = 0;
            do {
                int cardValueO1 = o1.get(count);
                int cardValueO2 = o2.get(count);

                if( cardValueO1 > cardValueO2 )
                    return 1;
                if ( cardValueO1 < cardValueO2 )
                    return -1;
            } while( count++ < 5 );

            throw new RuntimeException("could not decide on which hand is better");
        }
    };
}
