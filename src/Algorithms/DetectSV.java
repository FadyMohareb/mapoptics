package Algorithms;

import DataTypes.*;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.util.List;
import java.util.Map;

/*
* @author Anisha
*
* Detects SV from alignments using user specified parameters.
* */

public class DetectSV {
    private static List<Query> queries;
    private MapOpticsModel model;
    private static List<Reference> references;
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
    private int minNumMolecules;

    public DetectSV(MapOpticsModel model) {
        this.model = model;
        references = model.getReferences();
        this.minSupport = 0;
        this.minIndelSize = 500;
        this.maxIndelSize = 0;
        this.flankSig = 5;
        // research optimum default values
        this.minNumMolecules = 2;
        this.pValueThresh = 0;
        this.LHRThresh = 0;
        this.FPR = 0;
        this.FNR = 0;
    }


    // apply user-defined parameters to SV detection model
    public void setParameters(double minIndelSize, double maxIndelSize, int flankSig, int minSupport,
                              int minNumMolecules, double pValueThresh, double LHRThresh, double FPR, double FNR){
        this.minIndelSize = minIndelSize;
        this.maxIndelSize = maxIndelSize;
        this.flankSig = flankSig;
        this.minSupport = minSupport;
        this.minNumMolecules = minNumMolecules;
        this.pValueThresh = pValueThresh;
        this.LHRThresh = LHRThresh;
        this.FPR = FPR;
        this.FNR = FNR;
    }

    public List<Reference> getReferences() {
        return references;
    }

    public static void setReferences(List<Reference> references) {
        DetectSV.references = references;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public static void setQueries(List<Query> queries) {
        DetectSV.queries = queries;
    }


    public void detectIndels() {
        for (Reference ref : references) {
            model.setSelectedRef(ref.getRefID());
            for (Query qry : ref.getQueries()) {
                Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();
                Indel.compareDiffs(qryAlignments, ref.getSites(), qry.getQryViewSites());
            }
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



    // get seeds from query using sliding window, like BLAST find them in reference and then extend them
    // create a database of k-tuples for reference
    // query the k-tuples with those in the reference database
}
