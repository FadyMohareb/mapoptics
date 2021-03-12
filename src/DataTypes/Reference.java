package DataTypes;

import Algorithms.CalculateOverlaps;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Reference {

    private final int refID;

    private int overlaps;
    private int labels;
    private List<Double> regions;
    private List<Integer> queryIDs;
    private double length, density;
    private Map<Integer, Double> sites;

    public Reference(int refID) {
        this.refID = refID;
        regions = new ArrayList<>();
        queryIDs = new ArrayList<>();
        sites = new HashMap<>();
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

    public int getRefID() {
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
}
