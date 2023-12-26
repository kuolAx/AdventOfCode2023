package december25;

import utils.AdventHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Snowverload1 {

    private static String testinput = """
            jqt: rhn xhk nvd
            rsh: frs pzl lsr
            xhk: hfx
            cmg: qnr nvd lhk bvb
            rhn: xhk bvb hfx
            bvb: xhk hfx
            pzl: lsr hfx nvd
            qnr: nvd
            ntq: jqt hfx bvb xhk
            nvd: lhk
            lsr: lhk
            rzs: qnr cmg lsr rsh
            frs: qnr lhk lsr""";

    private static final Pattern MATCH_WORDS = Pattern.compile("\\w+");

    public static void main(String[] args) {

        String content = AdventHelper.readFile("./src/main/java/december25/input.txt");
        content = testinput;

        Map<String,List<String>> components = new HashMap<>();

        for (String contentLine : content.split("\n")) {

            String[] splitLine = contentLine.split(":");

            Matcher matchWords = MATCH_WORDS.matcher(splitLine[1]);
            List<String> connectedNodes = matchWords.results().map(MatchResult::group).distinct().toList();

            components.put(splitLine[0], connectedNodes);
        }



    }
}
