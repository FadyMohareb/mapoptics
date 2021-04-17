package DataTypes;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cigar {
    /*
    * @author Anisha
    *
    * */

    private final String hitEnum;
    private final List<String> parsedCigar;
    private final Map<Integer, String> cigRefSites;
    private final Map<Integer, String> cigQrySites;



    public Cigar(String hitEnum) {
        this.hitEnum = hitEnum;
        parsedCigar = new ArrayList<>();
        cigRefSites = new HashMap<>();
        cigQrySites = new HashMap<>();

    }
    public void parseHitEnum() {
        // create regex pattern (for 1 or more digits followed by a letter (non-digit)
        Pattern pattern = Pattern.compile("\\d+\\D");
        Matcher m = pattern.matcher(hitEnum);

        List<String> cigPairs= new ArrayList<>();

        // retrieve the matches start and end positions
        while (m.find()) {
            String var = hitEnum.substring(m.start(), m.end());
            cigPairs.add(var);
        }
        // Change extract number from each Cigar variant and use it as frequency in list
        for (String iter : cigPairs) {
            int freq = Integer.parseInt(iter.substring(0, iter.length() - 1));
            for (int i = 0; i < freq; i++) {
                parsedCigar.add(iter.substring(iter.length() - 1));
            }


        }
    }

    public void mapCigSites(Map<Integer, Double> refSites, Set<Integer> qrySites, Double startPos,
                              Double endPos) {
        // Loop through cigar string, ref and qry sites
        // convert sets to lists for easier access
        List<Integer> refSitesSub = new ArrayList<>();
        // extract relevant subset of ref sites for query region
        refSites.forEach((key, value) -> {
            if (value >= startPos && value <= endPos) {
                refSitesSub.add(key);
            }
        });
        Iterator<Integer> refIter = refSitesSub.iterator();
        Iterator<String> cigIter = parsedCigar.iterator();
        Iterator<Integer> qryIter = qrySites.iterator();
        while (cigIter.hasNext()) {
            String next = cigIter.next();
            if ("M".equals(next)) {
                if (qryIter.hasNext() && refIter.hasNext()) {
                    cigRefSites.put(refIter.next(), "M");
                    cigQrySites.put(qryIter.next(), "M");
                }
            } else if ("D".equals(next)) {
                if (refIter.hasNext()) {
                    cigRefSites.put(refIter.next(), "D");
                }
            } else if ("I".equals(next)) {
                if (qryIter.hasNext()) {
                    cigQrySites.put(qryIter.next(), "I");
                }
            }
        }

    }

    public Map<Integer, String> getCigRefSites() {
        return cigRefSites;
    }

    public Map<Integer, String> getCigQrySites() {
        return cigQrySites;
    }

    public void colorCigSites(Map<Integer, Double> refSites, Set<Integer> keySet, double start, double end) {
    }
}

