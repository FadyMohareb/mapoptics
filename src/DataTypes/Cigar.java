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
    private List<String> revCompCigar;
    private Map<Integer, String> cigRefSites;
    private Map<Integer, String> cigQrySites;
    private final Map<Double, String> cigRefPos;
    private final Map<Double, String> cigQryPos;



    public Cigar(String hitEnum) {
        this.hitEnum = hitEnum;
        parsedCigar = new ArrayList<>();
        revCompCigar = new ArrayList<>();
        cigRefSites = new LinkedHashMap<>();
        cigRefPos = new LinkedHashMap<>();
        cigQrySites = new LinkedHashMap<>();
        cigQryPos = new LinkedHashMap<>();

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
        // Change extract number from each Cigar variant and use it as frequency in list
        for (String iter : cigPairs) {
            int freq = Integer.parseInt(iter.substring(0, iter.length() - 1));
            for (int i = 0; i < freq; i++) {
                parsedCigar.add(iter.substring(iter.length() - 1));
            }


        }
        return parsedCigar;
    }

    public void mapCigSites(Map<Integer, Double> refSites, Map<Integer, Double> qrySites, Map<Integer,
            List<Integer>> qryAlignments, String orientation) {
        // Loop through cigar string, ref and qry sites
        // convert sets to lists for easier access
        // extract relevant subset of ref sites for query region
        List<Integer> refSitesSub = new ArrayList<>();
        // get list of queries that are matches
        List<Integer> alignedQrys = new ArrayList<>(qryAlignments.keySet());
        Collections.sort(alignedQrys);

        List<Integer> revQryAligned = new ArrayList<>();
        // check orientation, if negative reverse


        int qryStart = alignedQrys.get(0);
        int qryEnd = alignedQrys.get(alignedQrys.size() - 1);

        if(qryStart>alignedQrys.get(alignedQrys.size()-1) ){
            qryStart = alignedQrys.get(alignedQrys.size()-1);
            qryEnd = alignedQrys.get(0);

        }
        int refStartSite = qryAlignments.get(qryStart).get(0);
        int refEndSite = qryAlignments.get(qryEnd).get(0);





        // get the last ref site
        //List<Integer> refEndSites = qryAlignments.get(qryEnd);
        //int refEndSite = refEndSites.get(refEndSites.size() - 1);
        //
        if(refStartSite>refEndSite){
            refStartSite=refEndSite;
            refEndSite=qryAlignments.get(qryStart).get(0);
        }

        // loop through refsites in alignment range
        for (int site : refSites.keySet()) {
            if (site >= refStartSite && site <= refEndSite) {
                refSitesSub.add(site);
            }
        }

        // get refsites
        ListIterator<Integer> refIter = refSitesSub.listIterator();
        ListIterator<String> cigIter = parsedCigar.listIterator();
        List<Integer> qrySitesList = new ArrayList<>(qrySites.keySet());

        int QrySiteEnd;
        if (orientation.equals("-")){
         Collections.reverse(qrySitesList);
            QrySiteEnd=qrySitesList.get(0);
      }else{
        Collections.sort(qrySitesList);
            QrySiteEnd=qrySitesList.get(qrySitesList.size()-1);
       }


        ListIterator<Integer> qryIter = qrySitesList.listIterator();
        while (cigIter.hasNext()) {
            String next = cigIter.next();
            if ("M".equals(next)) {
                if (qryIter.hasNext() && refIter.hasNext()) {
                    int nextRef = refIter.next();
                    int nextQry = qryIter.next();
                    cigRefSites.put(nextRef, "M");
                    if (orientation.equals("-")){
                        cigQrySites.put(QrySiteEnd-nextQry+qryStart, "M");
                    }else{cigQrySites.put(nextQry+qryStart-1, "M");}
                }
            } else if ("D".equals(next)) {
                if (refIter.hasNext()) {
                    int nextRef = refIter.next();
                    cigRefSites.put(nextRef, "D");
                }
            } else if ("I".equals(next)) {
                if (qryIter.hasNext()) {
                    int nextQry = qryIter.next();
                    if (orientation.equals("-")){
                        cigQrySites.put(QrySiteEnd-nextQry+qryStart, "I");
                    }else{cigQrySites.put(nextQry+qryStart-1, "I"); }
                }
            }
        }
        //reverse the site num for negative orientation
        if (orientation.equals("-")){
            List<Integer> revQrylist = new ArrayList<>(cigQrySites.keySet());
            Collections.sort(revQrylist);
            int Minsite= revQrylist.get(0);
            int Maxsite = revQrylist.get(revQrylist.size()-1);
            for(int i :revQrylist){
                if(cigQrySites.get(i).equals("I")){
                    int site = Maxsite-(i-Minsite);
                    String rev = cigQrySites.get(site);
                    cigQrySites.replace(i,rev);
                    cigQrySites.replace(site,"I");
                }
            }
        }


        this.setCigRefSites(cigRefSites);
        this.setCigQrySites(cigQrySites);
    }

    public Map<Integer, String> getCigRefSites() {
        return cigRefSites;
    }

    public void setCigQrySites(Map<Integer, String> cigQrySites) {
        this.cigQrySites = cigQrySites;
    }

    public Map<Integer, String> getCigQrySites() {
        return cigQrySites;
    }

    public void setCigRefSites(Map<Integer, String> cigRefSites) {
        this.cigRefSites = cigRefSites;

    }

    public Map<Integer, List<Integer>> mapParsedCigarSites(Map<Integer, List<Integer>> qryAlignments, Map<Integer, Double>
            qrySites, Map<Integer, Double> refSites) {
        Map<Integer, List<Integer>> mappedCigar = new HashMap<>();
        List<Integer> qrySiteList = new ArrayList<>(qrySites.keySet());
        List<Integer> refSiteList = new ArrayList<>(refSites.keySet());
        ListIterator<Integer> refIt = refSiteList.listIterator();
        ListIterator<Integer> qryIt = qrySiteList.listIterator();
        // loop through cigar and get sites for each letter to produce a map where key: index of cigar letter and
        // value: corresponding sites
        for (int i = 1; i < parsedCigar.size(); i++) {
            String letter = parsedCigar.get(i - 1);
            while (refIt.hasNext() && qryIt.hasNext()) {
                switch (letter) {
                    case "I":
                        int qrySite = qryIt.next();
                        mappedCigar.put(i, new ArrayList<>(qrySite));
                        break;
                    case "D":
                        int refSite = refIt.next();
                        mappedCigar.put(i, new ArrayList<>(refSite));
                        break;
                    default:
                        refSite = refIt.next();
                        qrySite = qryIt.next();
                        List<Integer> matchPair = new ArrayList<>();
                        matchPair.add(refSite, qrySite);
                        mappedCigar.put(i, matchPair);
                }
            }

        }
        return mappedCigar;
    }

    // computes the reverse complement of the CIGAR string
    public List<String> reverseComplement(List<String> parsedCigar) {
        // loop through reversed Cigar sites
        for (int i = parsedCigar.size() - 1; i >= 0; i--) {
            // replace I with D and D with I
            if (parsedCigar.get(i).equals("I")) {
                this.revCompCigar.add("D");
            } else if (parsedCigar.get(i).equals("D")) {
                this.revCompCigar.add("I");
            } else {
                this.revCompCigar.add("M"); // matches remain the same
            }
        }
        return revCompCigar;
    }

    // find sequences which are palindromic within the CIGAR string
    public List<String> getRevPalindromes(List<String> parsedCigar, List<String> revCompCigar) {
        List<String> revPalindromes = new ArrayList<>();
        // concatenate individual string letters of cigar into a single string
        String revCompStr = String.join("", revCompCigar);
        // loop through the parsed cigar string list
        // loop through a window of size 4
        for (int i = 0; i + 4 < parsedCigar.size(); i++) {
            List<String> window = parsedCigar.subList(i, i + 4);
            String windowStr = String.join("", window);
            // check the sequence window is in the reverse string
            if (revCompStr.contains(windowStr)) {
                List<String> residualSeq = parsedCigar.subList(i, parsedCigar.size());
                ListIterator<String> winIter = residualSeq.listIterator();
                StringBuilder sb = new StringBuilder(windowStr);
                while (winIter.hasNext()) {
                    String nextLetter = winIter.next();
                    sb.append(nextLetter);
                    if (!revCompStr.contains(sb.toString())) {
                        // remove the last char that makes the string not match with a subsequence in the revComplement
                        sb.deleteCharAt(sb.length() - 1);
                        revPalindromes.add(sb.toString());
                        break;
                    }
                }
            }
        }
        return revPalindromes;
    }

    public static Map<Integer, List<Integer>> changeOrientation(Map<Integer, List<Integer>> qryAlignments) {
        // reverse the order of the query sites
        Map<Integer, List<Integer>> reverseMap = new LinkedHashMap<>();
        ArrayList<Integer> qrySites = new ArrayList<>(qryAlignments.keySet());
        for(int i = qrySites.size() - 1; i >= 0; i--){
            int revQrySite = qrySites.get(i);
            reverseMap.put(revQrySite, qryAlignments.get(revQrySite));

        }
        return reverseMap;
    }
}

