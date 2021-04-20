package Algorithms;

import DataTypes.*;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.util.List;
import java.util.Map;

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
        this.minSupport = 0;
        this.minIndelSize = 500;
        this.maxIndelSize = 0;
        this.flankSig = 5;
        // research optimum default values
        this.minCoverage = 10;
        this.pValueThresh = 1e-09;
        this.LHRThresh = 1e+06; // threshold for likelihood ratio for all SV hypotheses
        this.digestionRate = 0.875;
        this.cauchyScale = 0.0291;
        this.FPR = 0;
        this.FNR = 0;
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

    public void detectIndels() {
        for (Query qry : chosenRef.getQueries()) {
            List<Double> ratios = Indel.compareDiffs(qry.getAlignmentSites(), chosenRef.getSites(),
                    qry.getQryViewSites());
            System.out.println("qryID: " + qry.getID());
            System.out.println(ratios + "");
        }
    }


    public List<Indel> getIndels() {
        return indels;
    }

    public List<SV> getSVList() {
        return svList;
    }

    public void setSVList(List<SV> svList) {
        detectIndels();
        svList.add((SV) indels);
        this.svList = svList;

    }
    //
}
