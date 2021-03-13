package DataTypes;

import java.util.HashMap;
import java.util.Map;

public class Query {

    private final String queryID;
    private String orientation, hitEnum, alignments;
    private double confidence, length;
    private int numMatches, labels;
    private Map<Integer, Double> sites;



    public Query(String queryID) {
        this.queryID = queryID;
        sites = new HashMap<>();
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public void getHitEnum(String hitEnum) {
        this.hitEnum = hitEnum;
    }

    public void getAlignments(String alignments) {
        this.alignments = alignments;
        numMatches = alignments.length() - alignments.replace(",", "").length();
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void addSite(int siteID, double position) {
        sites.put(siteID, position);
    }

    public void setLabels(int labels) {
        this.labels = labels;
    }

    public String getID() {
        return queryID;
    }

    public double getLength() {
        return length;
    }

    public String getOrientation() {
        return orientation;
    }

    public double getConfidence() {
        return confidence;
    }

    public String getHitEnum() {
        return hitEnum;
    }

    public int getLabels() {
        return labels;
    }

    public int getNumMatches() {
        return numMatches;
    }

    public Map<Integer, Double> getSites() {
        return sites;
    }

    public String getAlignments() {
        return alignments;
    }
}
