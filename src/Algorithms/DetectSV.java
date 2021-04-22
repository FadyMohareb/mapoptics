package Algorithms;

import DataTypes.*;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
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
    private List<Translocation> translocation;
    private List<CNV> cnv;
    private List<SV> svList;
    private double pValueThresh;
    private double FPR;
    private double FNR;
    private int minCoverage;

    public DetectSV(MapOpticsModel model) {
        this.model = model;
        chosenRef = model.getSelectedRef();
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
    public void setParameters(double minIndelSize, double maxIndelSize, int flankSig, int minSupport,
                              int minCoverage, double pValueThresh, double LHRThresh, double FPR, double FNR,
                              double minIndelRatio){
        this.minIndelSize = minIndelSize;
        this.maxIndelSize = maxIndelSize;
        this.flankSig = flankSig;
        this.minSupport = minSupport;
        this.minCoverage = minCoverage;
        this.minIndelRatio = minIndelRatio;
        this.pValueThresh = pValueThresh;
        this.LHRThresh = LHRThresh;
        this.FPR = FPR;
        this.FNR = FNR;
    }

   public static void setChosenRef(Reference chosenRef) {
        DetectSV.chosenRef = chosenRef;
   }

   public void classifyLabels() {

   }

    public void detectIndels() {
        for (Query qry : chosenRef.getQueries()) {
            System.out.println("refID: " + chosenRef.getRefID() + " qryID: " + qry.getID());
            // for a given qry and ref get indels
            Map<Integer, Double> refSites = chosenRef.getSites();
            Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();
            Map<Integer, Double> qrySites = qry.getQryViewSites();
            // Extract aligned ref sites with selected qry (matches on ref) with duplicate refsites removed
            List<Integer> refAlignedSites = qryAlignments.values().stream().flatMapToInt(
                    refSite -> refSite.stream().mapToInt(i -> i)).boxed().distinct().collect(Collectors.toList());
            double refStartPos = refAlignedSites.get(0);
            double refEndPos = refSites.get(refAlignedSites.get(refAlignedSites.size() - 1));

            List<Indel> indels = Indel.getPutativeIndels(qryAlignments, chosenRef.getSites(),
                    qry.getQryViewSites());
            // cigar
            Cigar cig = new Cigar(qry.getHitEnum());
            cig.parseHitEnum();

            cig.mapCigSites(refSites, qrySites, refStartPos, refEndPos);
            Map<Double, String> refCigar = cig.getCigRefPos();
            // filter the indels
            List<Indel> filteredIndels = filterIndels(indels, this.minIndelSize, this.maxIndelSize, refCigar,
                    this.flankSig);
            // add qry and ref IDs to indels
            for (Indel indel : filteredIndels) {
                indel.setQryID(qry.getID());
                indel.setRefID1(chosenRef.getRefID());
                indel.setRefID2(chosenRef.getRefID());
                // XMAP IDs necessary?
                indel.setXmapID1("Placeholder");
                indel.setXmapID2("Placeholder");
                indel.setType();
                System.out.println(indel.getType());
            }
            this.indels.addAll(filteredIndels);
        }
    }

    // filter indels based on user-specified indel size range and flanking signals
    public static List<Indel> filterIndels(List<Indel> indelList, double minSize, double maxSize,
                                           Map<Double, String> refCig, int flankSig){
        List<Indel> filteredIndels = new ArrayList<>();
        for (Indel indel : indelList) {
            if (indel.svSize >= minSize && indel.svSize <= maxSize) {
                // get index of pos in Cigar
                List<Double> refPos = new ArrayList<>(refCig.keySet());
                int startIndex = refPos.indexOf(indel.getRefStartPos());
                System.out.println(indel.getRefStartPos());
                System.out.println(indel.getRefEndPos());
                int endIndex = refPos.indexOf(indel.getRefEndPos());
                // check the number of flanking signals either side of the indel
                /*
                if (startIndex > (flankSig - 1) && refCig.keySet().size() - endIndex >= flankSig) {
                    filteredIndels.add(indel);
                }
                 */
                filteredIndels.add(indel);
            }
        }
        return filteredIndels;
    }
    public List<Indel> getIndels() {
        return indels;
    }

    public void setIndels() {
        detectIndels();
    }
    public List<SV> getSVList() {
        return svList;
    }

    public void setSVList() {
        setIndels();
        svList.addAll(indels);

    }
}
