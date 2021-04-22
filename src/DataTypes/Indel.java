package DataTypes;

import java.util.*;
import java.util.stream.Collectors;

/*
* @author Anisha
*
* Detects indels by calculating distance ratios between adjacent label sites on reference and query contigs
* */

public class Indel extends SV {
    int mumMolecules = 1;
    
    public Indel(double qryPos1, double qryPos2, double refPos1, double refPos2, double qryDist,
                 double refDist, double size) {
        super();
        this.qryStartPos = qryPos1;
        this.qryEndPos = qryPos2;
        this.refStartPos = refPos1;
        this.refEndPos = refPos2;
        this.svSize = size;
        this.orientation = "NA";
    }

    // For LARGE INDELS

    // compute difference between adjacent label sites on ref sequence and their corresponding query sites
    public static List<Indel> setRefSiteDists(Map<Integer, List<Integer>> qryAlignments,
                                       Map<Integer, Double> refSites, Map<Integer, Double> qrySites){
        // Extract aligned ref sites with selected qry (matches on ref) with duplicate refsites removed
        List<Integer> refAlignedSites = qryAlignments.values().stream().flatMapToInt(
                refSite -> refSite.stream().mapToInt(i -> i)).boxed().distinct().collect(Collectors.toList());

        // map refsite to qrysites, i.e. swap qryAlignments key with values e.g. ref1 : [qry1, qry2]
        Map<Integer, List<Integer>> refAlignments = new HashMap<>();
        // loop through alignments and get each alignment pair
        for (Map.Entry<Integer, List<Integer>> alignPair : qryAlignments.entrySet()) {
            // loop through the refSites for a given query
            for (int site : alignPair.getValue()) {
                // if another query site has the ref site
                if (refAlignments.containsKey(site)) {
                    refAlignments.get(site).add(alignPair.getKey());
                } else {
                    // create a list for queries mapping to a single ref
                    List<Integer> queries = new ArrayList<>();
                    queries.add(alignPair.getKey());
                    refAlignments.put(site, queries);
                }
            }
        }

        List<Integer> qrySiteList = new ArrayList<>(qryAlignments.keySet());
        // loop through the matched refsites and calculate the distance between adjacent refsites that have a match
        ListIterator<Integer> refIter = refAlignedSites.listIterator();
        int refSite1 = refIter.next();
        List<Indel> indelList = new ArrayList<>();
        while (refIter.hasNext()) {
            int refSite2 = refIter.next();
            // get the positions for the adjacent ref aligned sites
            double refPos1 = refSites.get(refSite1);
            double refPos2 = refSites.get(refSite2);
            double refDist = refPos2 - refPos1;

            // get the matching query site position for the refsite
            int qrySite1 = refAlignments.get(refSite1).get(0);
            int qrySite2 = refAlignments.get(refSite2).get(0);
            double qryPos1 = qrySites.get(qrySite1);
            double qryPos2 = qrySites.get(qrySite2);
            double qryDist = qryPos2 - qryPos1;
            double size = Math.abs(qryDist - refDist);
            double roundSize = Math.round(size * 10) / 10;
            Indel indel = new Indel(qryPos1, qryPos2, refPos1, refPos2, refDist, qryDist, roundSize);
            indelList.add(indel);
            refSite1 = refSite2;
            }
        return indelList;
    }

    // measure the unchecked distances and compare with aligned sites on reference
    // this will detect distances missed by setRefSiteDists
    public static List<Indel> setResidualSiteDiffs(Map<Integer, List<Integer>> qryAlignments,
                                            Map<Integer, Double> refSites, Map<Integer, Double> qrySites) {
        List<Integer> qryAlignedSites = new ArrayList<>(qryAlignments.keySet());
        List<Indel> indelList = new ArrayList<>();
        ListIterator<Integer> qryIter = qryAlignedSites.listIterator();
        int qrySite1 = qryIter.next();
        while (qryIter.hasNext()) {
            double qryPos1 = qrySites.get(qrySite1);
            int qrySite2 = qryIter.next();
            double qryPos2 = qrySites.get(qrySite2);
            double qryDist = qryPos2 - qryPos1;

            // get corresponding refSites for those query sites and compute the difference
            int refSite1 = qryAlignments.get(qrySite1).get(0);
            int refSite2 = qryAlignments.get(qrySite2).get(0);
            // get respective positions
            double refPos1 = refSites.get(refSite1);
            double refPos2 = refSites.get(refSite2);
            double refDist = refPos2 - refPos1;
            double size = Math.abs(qryDist - refDist);
            double roundSize = Math.round(size * 10.0) / 10.0;
            Indel indel = new Indel(qryPos1, qryPos2, refPos1, refPos2, refDist, qryDist, roundSize);
            indelList.add(indel);
            //if (!isDuplicate(indel, indelList))  indelList.add(indel);
            qrySite1 = qrySite2;
        }
        return indelList;
    }

    // SV is called only if LHR < threshold
    void calculateLHR() {

    }

    double calculateDistRatio(double refDist, double qryDist) {
        double distRatio = Math.round(((qryDist / refDist) * 10) / 10);
        return distRatio;
    }


    // returns type of indel - insertion or deletion based on size change relative to reference distance
    @Override
    public String setType() {
        double qryDist = Math.abs(this.getQryEndPos() - this.getQryStartPos());
        double refDist = Math.abs(this.getRefEndPos() - this.getRefStartPos());
        if (refDist < qryDist) {
            return "insertion";
        } else if (refDist > qryDist) {
            return "deletion";
        } else {
            System.out.println("Not an indel");
            return null;
        }
    }


    // calculate the number of molecules covering the aligned region region

    static Boolean isDuplicate(Indel indel, List<Indel> indelList) {
        // compare each indel with the others to remove duplicate indels
        List<Indel> duplicates = new ArrayList<>();
        for (Indel listIndel : indelList) {
            // if all the qry and ref positions are the same the indel is already in the list
            if (indel.qryStartPos == listIndel.qryStartPos && indel.qryEndPos == listIndel.qryEndPos
                    && indel.refStartPos == listIndel.refStartPos && indel.refEndPos == listIndel.refEndPos) {
                return true;
            }
        }
        return false;
    }


    public static List<Indel> getPutativeIndels(Map<Integer, List<Integer>> qryAlignments, Map<Integer, Double> refSites,
                                                Map<Integer, Double> qrySites) {
        List<Indel> indelList = new ArrayList<>();
        List<Indel> indels = Indel.setRefSiteDists(qryAlignments, refSites, qrySites);
        List<Indel> missingIndels = Indel.setResidualSiteDiffs(qryAlignments, refSites, qrySites);
        indelList.addAll(indels);
        indelList.addAll(missingIndels);
        return indelList;

    }

    @Override
    void setSVRegion(SV sv) {
        // use start and end sites / positions of the indel to create filled polygon
        double qrySite1 = sv.getQryStartPos();
        double qrySite2 = sv.getQryEndPos();
        double refSite1 = sv.getRefStartPos();
        double refSite2 = sv.getRefEndPos();

    }



}
