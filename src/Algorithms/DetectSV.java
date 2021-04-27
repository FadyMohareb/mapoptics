package Algorithms;

import DataTypes.*;
import UserInterface.MapOptics;
import UserInterface.ModelsAndRenderers.MapOpticsModel;
//import org.apache.commons.math3.distribution;

import java.util.*;
import java.util.stream.Collectors;

/*
 * @author Anisha
 *
 * Detects SV from alignments using user-specified parameters.
 * */

public class DetectSV {
    private static List<Query> queries;
    private final double digestionRate;
    private final double cauchyScale;
    private double resolutionLim;
    private double cauchyMean;
    private double minIndelRatio;
    private MapOpticsModel model;
    private static Reference chosenRef;
    private double LHRThresh;
    private int minSupport;
    private double minIndelSize;
    private double maxIndelSize;
    private int flankSig;
    private List<Indel> indels;
    private List<Inversion> inversions;
    private List<Duplication> duplications;
    private List<Translocation> translocations;
    private List<CNV> cnv;
    private List<SV> svList;
    private double pValueThresh;
    private double FPR;
    private double FNR;
    private int minCoverage;
    private List<Double> ratios;


    public DetectSV(MapOpticsModel model) {
        this.model = model;
        chosenRef = null;
        this.minSupport = 1;
        this.minIndelSize = 500.0;
        this.maxIndelSize = 1000000.0;
        this.flankSig = 5;
        // research optimum default values
        this.minCoverage = 10;
        this.pValueThresh = 1e-09;
        this.LHRThresh = 1e+06; // threshold for likelihood ratio for all SV hypotheses
        this.digestionRate = 0.875;
        this.cauchyScale = 0.0291;
        this.FPR = 0;
        this.FNR = 0;
        indels = new ArrayList<>();
        this.svList = new ArrayList<>();
    }


    // apply user-defined parameters to SV detection model
    public void setFlankSig(int flankSig){
        this.flankSig = flankSig;
    }

    public void setMinIndelSize(int minIndelSize) {
        this.minIndelSize = (double) minIndelSize;
    }

    public void setMaxIndelSize(int maxIndelSize) {
        this.maxIndelSize = (double) maxIndelSize;
    }
    public void setChosenRef(Reference chosenRef) {
        this.chosenRef = chosenRef;
    }
    public static Reference getChosenRef() {
        return chosenRef;
    }

    public void classifyLabels() {

    }

    public void detectIndels() {
        for (Query qry : chosenRef.getQueries()) {
            // don't run indel functions on deleted queries
            if (chosenRef.getDelQryIDs().contains(Integer.parseInt(qry.getID())) ||
                    chosenRef.getSavedDelQryIDs().contains(Integer.parseInt(qry.getID()))) {
                continue;
            }
            // for a given qry and ref get Alignment info
            Map<Integer, Double> refSites = chosenRef.getSites();
            Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();
            // check orientation
            if (qry.getOrientation().equals("-")) {
                Map<Integer, List<Integer>> revQryAlignment = changeOrientation(qryAlignments);
                qryAlignments = revQryAlignment;
            }

            Map<Integer, Double> qrySites = qry.getQryViewSites();
            // Extract aligned ref sites with selected qry (matches on ref) with duplicate refsites removed
            List<Integer> refAlignedSites = qryAlignments.values().stream().flatMapToInt(
                    refSite -> refSite.stream().mapToInt(i -> i)).boxed().distinct().sorted()
                    .collect(Collectors.toList());
            int refStartSite = refAlignedSites.get(0);
            // get the last site(s) to get the last site in case the last query site maps to multiple refSites
            int  refEndSite = refAlignedSites.get(refAlignedSites.size() - 1);

            List<Indel> indels = Indel.getIndels(qryAlignments, chosenRef.getSites(),
                    qry.getQryViewSites());
            // cigar
            Cigar cig = new Cigar(qry.getHitEnum());
            cig.parseHitEnum();

            cig.mapCigSites(refSites, qrySites, qryAlignments);
            Map<Integer, String> refCigar = cig.getCigRefSites();
            Map<Integer, String> qryCigar = cig.getCigQrySites();
            // filter the indels
            List<Indel> filteredIndels = filterIndels(indels, this.minIndelSize, this.maxIndelSize, refCigar,
                    this.flankSig);
            // add qry and ref IDs to indels
            for (Indel indel : filteredIndels) {
                indel.setQryID(qry.getID());
                indel.setRefID1(chosenRef.getRefID());
                indel.setType();
            }
                this.indels.addAll(filteredIndels);
            }
        }

    // filter indels based on user-specified indel size range and flanking signals
    public static List<Indel> filterIndels(List<Indel> indelList, double minSize, double maxSize,
                                           Map<Integer, String> refCigar, int flankSig){
        List<Indel> filteredIndels = new ArrayList<>();
        for (Indel indel : indelList) {
            if (indel.svSize >= minSize && indel.svSize <= maxSize) {
                // get index of pos in Cigar
                List<Integer> refCig = new ArrayList<>(refCigar.keySet());
                int refStartInd = refCig.indexOf(indel.refStartSite);
                int refEndInd = refCig.indexOf(indel.refEndSite);

                // check the number of flanking signals either side of the indel
                if (refStartInd >= (flankSig - 1) && (refCig.size() - refEndInd >= flankSig)) {
                    filteredIndels.add(indel);
                }
            }
        }
        return filteredIndels;
    }
    public List<Indel> getIndels() {
        return indels;
    }


    public void setIndels() {
        this.indels = new ArrayList<>();
    }

    public void detectInversions() {
        for (Query qry : getChosenRef().getQueries()) {
            // loop through queries and identify reverse palindromes
            if (chosenRef.getDelQryIDs().contains(Integer.parseInt(qry.getID())) ||
                    chosenRef.getSavedDelQryIDs().contains(Integer.parseInt(qry.getID()))) {
                continue;
            }
            // for a given qry and ref get Alignment info
            Map<Integer, Double> refSites = chosenRef.getSites();
            Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();
            Map<Integer, Double> qrySites = qry.getQryViewSites();

            // set the reverse complement CIGAR string
            String hitEnum = qry.getHitEnum();
            Cigar cigar = new Cigar(hitEnum);
            // set the reverse comp cigar
            Inversion.setRevCompCigar(cigar);

        }
    }

    public List<Inversion> getInversions() {
        return inversions;
    }

    public void setInversions() {
        this.inversions = new ArrayList<>();
    }

    public List<SV> filterSVs() {
        //TODO: filter out SVs in gap locations, if fasta provided
        return svList;
    }

    public SV getSV(String qryStartPos, String qryEndPos, String refStartPos, String refEndPos, String type) {
        for (SV sv : this.svList) {
            double qryStart = Double.parseDouble(qryStartPos);
            double qryEnd = Double.parseDouble(qryEndPos);
            double refStart = Double.parseDouble(refStartPos);
            double refEnd = Double.parseDouble(refEndPos);
            if (sv.qryStartPos == qryStart && sv.qryEndPos == qryEnd
                    && sv.refStartPos == refStart && sv.refEndPos == refEnd && sv.type.equals(type)) {
                return sv;
            }
        }
        return null;
    }

    public List<Duplication> getDuplications() {
        return duplications;
    }

    public List<Translocation> getTranslocations() {
        return translocations;
    }

    public List<SV> getSVList() {
        return svList;
    }

    public void setSVList() {
        setIndels();
        detectIndels();
        // setInversions();
        // detectInversions();
        List<SV> svList = new ArrayList<>();
        svList.addAll(this.indels);
        //svList.addAll(this.inversions);
        this.svList = svList;
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
