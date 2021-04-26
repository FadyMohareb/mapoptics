package DataTypes;

import java.util.*;
import java.util.List;
import java.util.stream.Collectors;
//import org.apache.commons.math3.distribution.CauchyDistribution;

/*
 * @author Anisha
 *
 * Detects Large indels by calculating distance ratios between adjacent label sites on reference and query contigs
 * */

public class Indel extends SV {
    private final double qryDist;
    private final double refDist;
    private final int qryStartSite;
    public int refStartSite;
    public int refEndSite;
    private static Map<Integer, List<Double>> refQryPos = new HashMap<>();
    int mumMolecules = 1;

    public Indel(double qryPos1, double qryPos2, double refPos1, double refPos2, double qryDist,
                 double refDist, double size, int qryStartSite, int qryEndSite, int refStartSite, int refEndSite) {
        super();
        this.qryStartPos = qryPos1;
        this.qryEndPos = qryPos2;
        this.refStartPos = refPos1;
        this.refEndPos = refPos2;
        this.svSize = size;
        this.orientation = "+";
        this.qryDist = qryDist;
        this.refDist = refDist;
        this.qryStartSite = qryStartSite;
        this.qryEndSite = qryEndSite;
        this.refStartSite = refStartSite;
        this.refEndSite = refEndSite;

    }

    // For LARGE INDELS

    // compute difference between adjacent label sites on ref sequence and their corresponding query sites
    // check orientation of query contig
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
        int index = 1;
        List<Indel> indelList = new ArrayList<>();
        while (refIter.hasNext()) {
            int refSite2 = refIter.next();
            // get the positions for the adjacent ref aligned sites
            double refPos1 = refSites.get(refSite1);
            double refPos2 = refSites.get(refSite2);
            double refDist = Math.abs(refPos2 - refPos1);

            // get the matching query site position for the refsite
            int qrySite1 = refAlignments.get(refSite1).get(0);
            int qrySite2 = refAlignments.get(refSite2).get(0);
            double qryPos1 = qrySites.get(qrySite1);
            double qryPos2 = qrySites.get(qrySite2);
            index++;
            List<Double> positions = new ArrayList();
            for (double v : new double[]{refPos1, refPos2, qryPos1, qryPos2}) {
                positions.add(v);
            }
            refQryPos.put(index, positions);
            double qryDist = Math.abs(qryPos2 - qryPos1);
            double size = Math.abs(qryDist - refDist);
            double roundSize = Math.round(size * 10.0) / 10.0;

            // make indel
            Indel indel = new Indel(qryPos1, qryPos2, refPos1, refPos2, refDist, qryDist, roundSize, qrySite1,
                    qrySite2, refSite1, refSite2);
            indel.setRefStartSite(refSite1);
            indel.setRefEndSite(refSite2);
            indel.setQryStartSite(qrySite1);
            indel.setQryEndSite(qrySite2);
            indel.setType();
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

            // get corresponding refSites for those query sites and compute the difference
            int refSite1 = qryAlignments.get(qrySite1).get(0);
            int refSite2 = qryAlignments.get(qrySite2).get(0);
            // get respective positions
            double refPos1 = refSites.get(refSite1);
            double refPos2 = refSites.get(refSite2);

            Boolean duplicate = false;
            // screen for duplicates by only adding indels which have at least one different position
            for (List<Double> positions : refQryPos.values()) {
                if (positions.contains(refPos1) && positions.contains(refPos2) && positions.contains(qryPos1)
                    && positions.contains(qryPos2)) {
                    duplicate = true;
                    break;
                }
            }
            if (!duplicate) {
                double qryDist = Math.abs(qryPos2 - qryPos1);
                double refDist = Math.abs(refPos2 - refPos1);
                double size = Math.abs(qryDist - refDist);
                double roundSize = Math.round(size * 10.0) / 10.0;
                // make indel
                Indel indel = new Indel(qryPos1, qryPos2, refPos1, refPos2, refDist, qryDist, roundSize,
                        qrySite1, qrySite2, refSite1, refSite2);
                indel.setRefStartSite(refSite1);
                indel.setRefEndSite(refSite2);
                indel.setQryStartSite(qrySite1);
                indel.setQryEndSite(qrySite2);
                indel.setType();
                indelList.add(indel);
            }

            qrySite1 = qrySite2;
        }
        return indelList;
    }

    // SV is called only if LHR < threshold
    void calculateLHR() {

    }

    double calculateDistRatio(double refDist, double qryDist) {
        double distRatio = Math.round(((qryDist / refDist) * 10.0) / 10.0);
        return distRatio;
    }


    // returns type of indel - insertion or deletion based on size change relative to reference distance
    @Override
    public void setType() {
        double qryDist = Math.abs(this.getQryEndPos() - this.getQryStartPos());
        double refDist = Math.abs(this.getRefEndPos() - this.getRefStartPos());
        if (refDist < qryDist) {
            this.type = "insertion";
        } else if (refDist > qryDist) {
            this.type = "deletion";
        }
    }


    // calculate the number of molecules covering the aligned region region




    public static List<Indel> getIndels(Map<Integer, List<Integer>> qryAlignments, Map<Integer, Double> refSites,
                                                Map<Integer, Double> qrySites) {
        List<Indel> indelList = new ArrayList<>();
        List<Indel> indels = Indel.setRefSiteDists(qryAlignments, refSites, qrySites);
        List<Indel> missingIndels = Indel.setResidualSiteDiffs(qryAlignments, refSites, qrySites);
        indelList.addAll(indels);
        indelList.addAll(missingIndels);
        return indelList;

    }

    public void setRefStartSite(int refStartSite){
        this.refStartSite = refStartSite;
    }

    public int getRefStartSite() {
        return refStartSite;
    }

    public void setRefEndSite(int refEndSite) {
        this.refEndSite = refEndSite;
    }

    public int getRefEndSite() {
        return refEndSite;
    }

}
