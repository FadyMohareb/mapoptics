package DataTypes;

import Algorithms.CalculateOverlaps;

import java.awt.geom.Rectangle2D;
import java.util.*;

public class Reference {

    private final String refID;
    private final List<Double> regions;
    private final List<Integer> queryIDs;
    private final Map<Integer, Double> sites;
    private final List<Integer> delQryIDs;

    private int overlaps;
    private int labels;
    private double length, density;
    private List<Query> queries;
    private Rectangle2D rectangle;
    private Set<Integer> alignmentSiteIds;
    private Rectangle2D refViewRect = null;
    private Rectangle2D qryViewRect = null;
    private double coverage;
    private double chimQual;
    private List<Integer> savedDelQryIDs;
    private Map<Integer, String> refAlignRegion;

    public Reference(String refID) {
        this.refID = refID;
        regions = new ArrayList<>();
        queryIDs = new ArrayList<>();
        queries = new ArrayList<>();
        sites = new HashMap<>();
        delQryIDs = new ArrayList<>();
        savedDelQryIDs = new ArrayList<>();
        refAlignRegion = new HashMap<>();
    }

    public void addQueryRegion(double start, double stop) {
        if (regions.isEmpty()) {
            regions.add(start);
            regions.add(stop);
        } else {
            for (int i = 0; i < regions.size(); i += 2) {

                if (start < regions.get(i)) {
                    regions.add(i, start);
                    regions.add(i + 1, stop);
                    break;
                } else if (start == regions.get(i) && stop < regions.get(i + 1)) {
                    regions.add(i, start);
                    regions.add(i + 1, stop);
                    break;
                } else if (i == regions.size() - 2) {
                    regions.add(start);
                    regions.add(stop);
                    break;
                }
            }
        }
    }

    public String getRefID() {
        return refID;
    }

    public int getOverlaps() {
        return overlaps;
    }

    public int getLabels() {
        return labels;
    }

    public List<Integer> getQueryIDs() {
        return queryIDs;
    }

    public double getLength() {
        return length;
    }

    public void setDensity() {
        density = (labels / length) * 100000;
    }

    public double getDensity() {
        return density;
    }

    public void addQuery(int queryIndex) {
        queryIDs.add(queryIndex);
    }

    public void setOverlaps() {
        overlaps = CalculateOverlaps.countAllOverlaps(regions);
    }

    public void setLength(double length) {
        this.length = length;
    }

    public void setLabels(int labels) {
        this.labels = labels;
    }

    public void addSite(int siteID, double position) {
        sites.put(siteID, position);
    }

    public void setQueries(List<Query> queries) {
        this.queries = queries;
    }

    public List<Query> getQueries() {
        return queries;
    }

    public Map<Integer, Double> getSites() {
        return sites;
    }

    public Query getQuery(String queryID) {
        for (Query query : queries) {
            if (query.getID().equals(queryID)) {
                return query;
            }
        }
        return null;
    }

    public void setRectangle(Rectangle2D rectangle) {
        this.rectangle = rectangle;
    }

    public Rectangle2D getRectangle() {
        return rectangle;
    }

    public void setAlignmentSites(Set<Integer> alignmentSiteIds) {
        this.alignmentSiteIds = alignmentSiteIds;
    }

    public Set<Integer> getAlignmentSites() {
        return alignmentSiteIds;
    }

    public void setRefViewRect(Rectangle2D refViewRect) {
        this.refViewRect = refViewRect;
    }

    public Rectangle2D getRefViewRect() {
        return refViewRect;
    }

    public void setQryViewRect(Rectangle2D qryViewRect) {
        this.qryViewRect = qryViewRect;
    }

    public Rectangle2D getQryViewRect() {
        return qryViewRect;
    }

    // Methods for manipulating deleted contigs

    public List<Integer> getDelQryIDs() { return delQryIDs; }

    // Add method for storing deleted query IDs
    public void setDelQryIDs(Integer queryID) {
        delQryIDs.add(queryID);
    }

    public List<Integer> getSavedDelQryIDs() { return savedDelQryIDs; }


    // method for copying delQryIDs to savedQryIDs
    public void setSavedDelQryIDs(List<Integer> delQryIDs) { this.savedDelQryIDs = delQryIDs; }

    public void setRefAlignPos(Map<Integer, String> refAlignRegion){
        this.refAlignRegion= refAlignRegion;
    }
    public Double[] getRefAlignPos(String ChosenQry){
        int qry= Integer.parseInt(ChosenQry);
        String[] StartEnd = refAlignRegion.get(qry).split("-");
        double Start = Double.parseDouble(StartEnd[0]);
        double End = Double.parseDouble(StartEnd[1]);

        if(Start>End){
            return new Double[]{End,Start};
        }else{
            return new Double[]{Start,End};
        }
    }

    public void setCoverage(double coverage) {
        this.coverage = coverage;
    }

    public void setChimQual(double chimQual) {
        this.chimQual = chimQual;
    }

    public double getCoverage() {
        return coverage;
    }

    public List<Double> getRegions() {
        return regions;
    }
}
