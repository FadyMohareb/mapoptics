package Algorithms;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variants {

    private final String hitEnum;
    private final List<String> qrySiteCig;

    public Variants(String hitEnum) {
        this.hitEnum = hitEnum;
        qrySiteCig = new ArrayList<>();

    }
    public List<String> parseHitEnum() {
        // create regex pattern (for 1 or more digits followed by a letter (non-digit)
        Pattern pattern = Pattern.compile("\\d+\\D");
        Matcher m = pattern.matcher(hitEnum);

        List<String> cigPairs= new ArrayList<>();

        // retrieve the matches start and end positions
        while (m.find()) {
            String var = hitEnum.substring(m.start(), m.end());
            cigPairs.add(var);
        }
        // ma
        for (String iter : cigPairs) {
            int freq = Integer.parseInt(iter.substring(0, iter.length() - 1));
            for (int i = 0; i < freq; i++) {
                qrySiteCig.add(iter.substring(iter.length() - 1));
            }


        }
        System.out.println(qrySiteCig + " ");
        return qrySiteCig;
    }

    // loop throught the alignments in pairs and compute the distances within each alignment pair
    // on the ref side and qry side then compare distances
    // draw SV region filled polygon
    // add SV metrics to other class

    /*
    Boolean isInversion(String orientation) {
        // check for an inversion
        return orientation.equals("-");
    }
     */
}
