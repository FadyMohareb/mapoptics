package Algorithms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variant {

    private final String hitEnum;
    private final List<String> qrySiteCig;
    private final Map<Integer, String> qryAlignCigs;

    public Variant(String hitEnum) {
        this.hitEnum = hitEnum;
        qrySiteCig = new ArrayList<>();
        qryAlignCigs = new HashMap<>();

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
        for (String iter : cigPairs) {
            int freq = Integer.parseInt(iter.substring(0, iter.length() - 1));
            for (int i = 0; i < freq; i++) {
                qrySiteCig.add(iter.substring(iter.length() - 1));
            }


        }
        return qrySiteCig;
    }

    public Map<Integer, String> getQryAlignsCigar() {
        return qryAlignCigs;
    }

    // Classify Qry site alignments as matches or insertions
    public void setQryAlignsCigar(Map<Integer, List<Integer>> qryAlignments) {
        // loop through the query sites that have alignments with ref contig
        for (int site1 : qryAlignments.keySet()) {
            // add the qrySite key to Alignment cigar hashmap (default value - Match)
            qryAlignCigs.put(site1, "M");
            int refSite1 = qryAlignments.get(site1).get(0);
            // loop through the qry alignments again
            for (int site2: qryAlignments.keySet()) {
                if (site1 == site2) {
                    continue; // excludes first site
                }
                if (refSite1 == qryAlignments.get(site2).get(0)) {
                    qryAlignCigs.put(site1, "I");
                    qryAlignCigs.put(site2, "I");
                }
            }
        }
        // if the second site has a refsite that matches with first then it is an insertion
        // mark the first refsite as an insertion too
        System.out.println(qryAlignCigs.size() + "");
        for (Integer key : qryAlignCigs.keySet()) {
            System.out.println(key + "");
        }


        // otherwise if no other qry sites align with the same ref then it's just a match
        }

    Boolean isInversion(String orientation) {
        // check for an inversion
        return orientation.equals("-");
    }
    // color inversion as a whole

}
