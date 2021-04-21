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
    public final List<String> parsedCigar;
    private final Map<Integer, String> cigRefSites;
    private Map<Integer, String> cigQrySites;
    private final Map<Double, String> cigRefPos;
    private final Map<Double, String> cigQryPos;



    public Cigar(String hitEnum) {
        this.hitEnum = hitEnum;
        parsedCigar = new ArrayList<>();
        cigRefSites = new LinkedHashMap<>();
        cigRefPos = new LinkedHashMap<>();
        cigQrySites = new LinkedHashMap<>();
        cigQryPos = new LinkedHashMap<>();

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

    public void mapCigSites(Map<Integer, Double> refSites, Map<Integer, Double> qrySites, Double refStartPos,
                              Double refEndPos) {
        // Loop through cigar string, ref and qry sites
        // convert sets to lists for easier access
        List<Integer> refSitesSub = new ArrayList<>();
        // extract relevant subset of ref sites for query region
        refSites.forEach((key, value) -> {
            if (value >= refStartPos && value <= refEndPos) {
                refSitesSub.add(key);
            }
        });
        Iterator<Integer> refIter = refSitesSub.iterator();
        Iterator<String> cigIter = parsedCigar.iterator();
        Iterator<Integer> qryIter = qrySites.keySet().iterator();
        while (cigIter.hasNext()) {
            String next = cigIter.next();
            if ("M".equals(next)) {
                if (qryIter.hasNext() && refIter.hasNext()) {
                    int nextRef = refIter.next();
                    int nextQry = qryIter.next();
                    cigRefSites.put(nextRef, "M");
                    cigQrySites.put(nextQry, "M");
                    cigRefPos.put(refSites.get(nextRef), "M");
                    cigQryPos.put(qrySites.get(nextQry), "M");
                }
            } else if ("D".equals(next)) {
                if (refIter.hasNext()) {
                    int nextRef = refIter.next();
                    cigRefSites.put(nextRef, "D");
                    cigRefPos.put(refSites.get(nextRef), "D");
                }
            } else if ("I".equals(next)) {
                if (qryIter.hasNext()) {
                    int nextQry = qryIter.next();
                    cigQrySites.put(nextQry, "I");
                    cigQryPos.put(qrySites.get(nextQry), "I");
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

    public Map<Double, String> getCigRefPos() {
        return cigRefPos;
    }

    public Map<Double, String> getCigQryPos() {
        return cigQryPos;
    }

}

