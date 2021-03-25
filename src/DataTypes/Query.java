package DataTypes;

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
    private TreeMap<Integer, Double> refViewSites;
    private Rectangle2D rectangle;
    private Map<Integer, List<Integer>> alignmentSiteIds;
    private Rectangle2D refViewRect = null;
    private int refViewOffsetY;
    private int refViewOffsetX;
    private boolean isFlipped = false;


    public Query(String queryID) {
        this.queryID = queryID;
        sites = new TreeMap<>();
        refViewSites = new TreeMap<>();
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
        refViewSites.put(siteID, siteData.get(0));
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

    public void setRefViewRect(Rectangle2D refViewRect) {
        this.refViewRect = refViewRect;
    }

    public Rectangle2D getRefViewRect() {
        return refViewRect;
    }

    public void setRefViewOffsetX(int refViewOffsetX) {
        this.refViewOffsetX += refViewOffsetX;
    }

    public int getRefViewOffsetX() {
        return refViewOffsetX;
    }

    public void setRefViewOffsetY(int refViewOffsetY) {
        this.refViewOffsetY += refViewOffsetY;
    }

    public int getRefViewOffsetY() {
        return refViewOffsetY;
    }

    public TreeMap<Integer, Double> getRefViewSites() {
        return refViewSites;
    }

    public void reOrientate() {
        if (isFlipped) {
            refViewSites.replaceAll((i, v) -> sites.get(i).get(0));
            isFlipped = false;
        } else {
            for (Integer id : refViewSites.keySet()) {
                double newPos = length - refViewSites.get(id);
                refViewSites.put(id, newPos);
            }
            isFlipped = true;
        }
    }
}

