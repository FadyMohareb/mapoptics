package DataTypes;

import Algorithms.SortOverlap;

import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Query {

    private final String queryID;
    private String orientation, hitEnum, alignments;
    private double confidence, length;
    private int numMatches, labels;
    private TreeMap<Integer, List<Double>> sites;
    private Rectangle2D rectangle;
    private Map<Integer, List<Integer>> alignmentSiteIds;


    public Query(String queryID) {
        this.queryID = queryID;
        sites = new TreeMap<>();
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public void setHitEnum(String hitEnum) {
        this.hitEnum = hitEnum;
    }

    public void setAlignments(String alignments) {
        this.alignments = alignments;
        numMatches = alignments.length() - alignments.replace(",", "").length();
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void addSite(int siteID, List<Double> siteData) {
        sites.put(siteID, siteData);
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

    public TreeMap<Integer, List<Double>> getSites() {
        return sites;
    }

    public String[] getFirstAlignment() {
        String tuple = alignments.split("\\)")[0];
        tuple = tuple.replace("(", "");
        return tuple.split(",");
    }

    public void setRectangle(Rectangle2D rectangle) {
        this.rectangle = rectangle;


    }

    public Rectangle2D getRectangle() {
        return rectangle;
    }

    public String getAlignments() {
        return alignments;
    }

    public void setAlignmentSites(Map<Integer, List<Integer>> alignmentSiteIds) {
        this.alignmentSiteIds = alignmentSiteIds;
    }

    public Map<Integer, List<Integer>> getAlignmentSites() {
        return alignmentSiteIds;
    }
}
