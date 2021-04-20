package DataTypes;

import Algorithms.DetectSV;

import java.util.*;
import java.util.stream.Collectors;

/*
* @author Anisha
*
* Detects indels by calculating distance ratios between adjacent label sites on reference and query contigs
* */

public class Indel extends SV {

    public Indel() {
        super();
        int mumMolecules = 1;
    }

    // For LARGE INDELS

    // compute difference between adjacent label sites on ref sequence
    public static Map<String, Double> getRefSiteDiffs(Map<Integer, List<Integer>> qryAlignments,
                                               Map<Integer, Double> refSites){
        // Extract aligned ref sites with selected qry (matches on ref)
        List<Integer> refAlignedSites = qryAlignments.values().stream().flatMapToInt(
                refSite -> refSite.stream().mapToInt(i -> i)).boxed().collect(Collectors.toList());
        List<Integer> refSiteList = new ArrayList<>(refSites.keySet());
        Map<String, Double> refDiffs = new HashMap<>();
        for (int i = 0, j = 1; j < refSiteList.size(); i+=2, j+=2) {
            double diff = refSites.get(refSiteList.get(j)) - refSites.get(refSiteList.get(i));
            String sitesKey = refSiteList.get(i) + "-" + refSiteList.get(j);
            refDiffs.put(sitesKey, diff);
        }
        return refDiffs;
    }

    // compute distances between adjacent label sites on queries
    public static Map<String, Double> getQrySiteDiffs(Map<Integer, List<Integer>> qryAlignments, Map<Integer,
            Double> qrySites) {
        List<Integer> qrySiteList = new ArrayList<>(qryAlignments.keySet());
        Map<String ,Double> qryDiffs = new HashMap<>();
        for (int i = 0, j = 1; j < qrySiteList.size(); i+=2, j+=2) {
            double diff = qrySites.get(qrySiteList.get(j)) - qrySites.get(qrySiteList.get(i));
            String sitesKey = qrySiteList.get(i) + "-" + qrySiteList.get(j);
            qryDiffs.put(sitesKey, diff);
        }
        return qryDiffs;
    }

    // compare the differences with corresponding label distances on query
    public static List<Double> compareDiffs(Map<Integer, List<Integer>> qryAlignments, Map<Integer, Double> refSites,
                                            Map<Integer, Double> qrySites) {
        List<Double> refDiffs = new ArrayList<>(getRefSiteDiffs(qryAlignments, refSites).values());
        List<Double> qryDiffs = new ArrayList<>(getQrySiteDiffs(qryAlignments, qrySites).values());
        List<Double> ratios = new ArrayList<>();
        for (int i = 0, j = 0; i < qryDiffs.size() && j < refDiffs.get(i); i++) {
            ratios.add((qryDiffs.get(i) / refDiffs.get(i)));
        }
        return ratios;
    }

    // then measure the difference between unchecked and compared with aligned sites on reference
    public Map<String, Double> getMissedDiffs(Map<Integer, List<Integer>> qryAlignments,
                          Map<Integer, Double> refSites) {
        List<Integer> refSiteList = new ArrayList<>(refSites.keySet());
        Map<String, Double> refDiffs = new HashMap<>();
        for (int i = 1, j = 2; j < refSiteList.size(); i+=2, j+=2) {
            double diff = refSites.get(refSiteList.get(j)) - refSites.get(refSiteList.get(i));
            String sitesKey = refSiteList.get(i) + "-" + refSiteList.get(j);
            refDiffs.put(sitesKey, diff);
        }
        return refDiffs;
    }

    // SV is called only if LHR < threshold
    public void checkIndel() {

    }

    // returns type of indel - insertion or deletion based on ratio size
    String getType(double ratio) {
        if (ratio > 1) {
            return "indel";
        } else if (ratio < 1) {
            return "deletion";
        } else {
            return null;
        }
    }



    // calculate the number of molecules covering the aligned region region

    // filter indels based on user-specified indel size range
    void filterSV(){

    }

    @Override
    void setSVRegion(SV sv) {

    }
}
