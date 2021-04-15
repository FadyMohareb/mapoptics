package Algorithms;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Variants {

    private final String hitEnum;
    private final List<String> parsedCigar;
    private final Map<Integer, String> cigRefSites;
    private final Map<Integer, String> cigQrySites;



    public Variants(String hitEnum) {
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

    public void colorCigSites(Map<Integer, Double> refSites, Set<Integer> qrySites, Double startPos,
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
                switch (cigIter.next()) {
                    case "M" -> {
                        if (qryIter.hasNext() && refIter.hasNext()) {
                            cigRefSites.put(refIter.next(), "M");
                            cigQrySites.put(qryIter.next(), "M");
                        }
                    }
                    case "D" -> {
                        if (refIter.hasNext()) {
                            cigRefSites.put(refIter.next(), "D");
                        }
                    }
                    case "I" -> {
                        if (qryIter.hasNext()) {
                            cigQrySites.put(qryIter.next(), "I");
                        }
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

    // loop throught the alignments in pairs and compute the distances within each alignment pair
    // on the ref side and qry side then compare distances
    // draw SV region filled polygon
    // add SV metrics to other class

    }

