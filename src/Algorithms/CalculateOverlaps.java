package Algorithms;

import DataTypes.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

/*
 * @author Josie
 */
public class CalculateOverlaps {

    public static LinkedHashMap<String, Integer> countAllOverlaps(LinkedHashMap<String, RefContig> references,
                                                                  LinkedHashMap<String, QryContig> queries) {
        ArrayList<String> done = new ArrayList<>();
        int count;
        RefContig ref;
        QryContig qry1;
        QryContig qry2;
        LinkedHashMap<String, Integer> numOverlaps = new LinkedHashMap<>();

        for (String refId : references.keySet()) {
            count = 0;
            ref = references.get(refId);
            for (String qryId1 : ref.getConnections()) {
                qry1 = queries.get(refId + "-" + qryId1);
                for (String qryId2 : ref.getConnections()) {
                    if (!qryId1.equals(qryId2) && !done.contains(qryId2 + "-" + qryId1)) {
                        done.add(qryId1 + "-" + qryId2);
                        qry2 = queries.get(refId + "-" + qryId2);
                        // check overlaps
                        double qry1start = qry1.getRefAlignStart();
                        double qry2start = qry2.getRefAlignStart();
                        double qry1end = qry1.getRefAlignEnd();
                        double qry2end = qry2.getRefAlignEnd();
                        boolean overlap = SortOverlap.isOverLappingX(qry1start, qry1end, qry2start, qry2end);
                        if (overlap) {
                            count++;
                        }
                    }
                }
            }
            numOverlaps.put(refId, count);
        }
        return numOverlaps;
    }

    public static LinkedHashMap<String, Rectangle2D[]> calculateRefOverlap(String refId, RefContig ref,
                                                                           LinkedHashMap<String, QryContig> queries) {
        LinkedHashMap<String, Rectangle2D[]> overlapRegions = new LinkedHashMap<>();
        ArrayList<String> done = new ArrayList<>();
        QryContig qry1;
        QryContig qry2;
        for (String qryId1 : ref.getConnections()) {
            qry1 = queries.get(refId + "-" + qryId1);

            for (String qryId2 : ref.getConnections()) {
                if (!qryId1.equals(qryId2) && !done.contains(qryId2 + "-" + qryId1)) {
                    done.add(qryId1 + "-" + qryId2);
                    qry2 = queries.get(refId + "-" + qryId2);
                    // check regions of overlaps and add to arraylist
                    double qry1start = qry1.getRefAlignStart();
                    double qry2start = qry2.getRefAlignStart();
                    double qry1end = qry1.getRefAlignEnd();
                    double qry2end = qry2.getRefAlignEnd();
                    boolean overlap = SortOverlap.isOverLappingX(qry1start, qry1end, qry2start, qry2end);
                    if (overlap) {
                        Rectangle2D[] regionOverlaps = calculateRegionOverlap(qry1, qry2, ref);
                        overlapRegions.put(refId + "-" + qryId1 + "-" + qryId2, regionOverlaps);
                    }
                }
            }
        }

        return overlapRegions;
    }

    private static Rectangle2D[] calculateRegionOverlap(QryContig qry1, QryContig qry2, RefContig ref) {
        Rectangle2D[] regionOverlaps = new Rectangle2D[3];
        double qry1start = qry1.getRefAlignStart();
        double qry2start = qry2.getRefAlignStart();
        double qry1end = qry1.getRefAlignEnd();
        double qry2end = qry2.getRefAlignEnd();
        Rectangle2D refRegionOverlap = new Rectangle2D.Double();
        if ((qry1start <= qry2start) && (qry2end <= qry1end)) {
            // calculate region overlapping in reference
            refRegionOverlap = new Rectangle2D.Double(
                    ref.getRectangle().getMinX() + qry2start,
                    ref.getRectangle().getMinY(),
                    qry2end - qry2start,
                    ref.getRectangle().getHeight());
            regionOverlaps[0] = refRegionOverlap;
        } else if ((qry2start <= qry1start) && (qry1end <= qry2end)) {
            // calculate region overlapping in reference
            refRegionOverlap = new Rectangle2D.Double(
                    ref.getRectangle().getMinX() + qry1start,
                    ref.getRectangle().getMinY(),
                    qry1end - qry1start,
                    ref.getRectangle().getHeight());
            regionOverlaps[0] = refRegionOverlap;
        } else if ((qry1.getRefAlignStart() <= qry2.getRefAlignStart())
                && (qry1.getRefAlignEnd() >= qry2.getRefAlignStart())
                && (qry2.getRefAlignEnd() >= qry1.getRefAlignEnd())) {
            // calculate region overlapping in reference
            refRegionOverlap = new Rectangle2D.Double(
                    ref.getRectangle().getMinX() + qry2start,
                    ref.getRectangle().getMinY(),
                    qry1end - qry2start,
                    ref.getRectangle().getHeight());
            regionOverlaps[0] = refRegionOverlap;
        } else if ((qry2.getRefAlignStart() <= qry1.getRefAlignStart()) && (qry2end >= qry1.getRefAlignStart())
                && (qry1.getRefAlignEnd() >= qry2.getRefAlignEnd())) {
            // calculate region overlapping in reference
            refRegionOverlap = new Rectangle2D.Double(
                    ref.getRectangle().getMinX() + qry1start,
                    ref.getRectangle().getMinY(),
                    qry2end - qry1start,
                    ref.getRectangle().getHeight());
            regionOverlaps[0] = refRegionOverlap;
        }

        //calculate region overlapping on query1
        Rectangle2D qry1RegionOverlap = calculateQryOverlap(qry1, ref, refRegionOverlap);
        regionOverlaps[1] = qry1RegionOverlap;
        //calculate region overlapping on query2
        Rectangle2D qry2RegionOverlap = calculateQryOverlap(qry2, ref, refRegionOverlap);
        regionOverlaps[2] = qry2RegionOverlap;

        return regionOverlaps;
    }

    private static Rectangle2D calculateQryOverlap(QryContig qry, RefContig ref, Rectangle2D refRegionOverlap) {
        Rectangle2D overlap ;

        // loop through labels and work out which labels are within the ref region of overlap
        ArrayList<Integer> allOverlappingRefLabels = new ArrayList<>();
        int[] refLabelIndexes = new int[2];
        for (int i = 0; i < ref.getLabels().length; i++) {
            double labelpos = ref.getLabels()[i].getMinX();
            if (labelpos >= refRegionOverlap.getMinX() && labelpos <= refRegionOverlap.getMaxX()) {
                allOverlappingRefLabels.add(i);
            }
        }

        if (!allOverlappingRefLabels.isEmpty()) {
            refLabelIndexes[0] = Collections.min(allOverlappingRefLabels);
            refLabelIndexes[1] = Collections.max(allOverlappingRefLabels);

            // loop through alignments and work out which regions are within the ref region of overlap
            ArrayList<Integer> allOverlappingQryLabels = new ArrayList<>();
            int[] qryLabelIndexes = new int[2];
            for (String[] alignment : qry.getAlignments()) {
                int reflabel = Integer.parseInt(alignment[0]) - 1;
                if (reflabel >= refLabelIndexes[0] && reflabel <= refLabelIndexes[1]) {
                    allOverlappingQryLabels.add(Integer.parseInt(alignment[1]) - 1);
                }
            }
            if (qry.isReOrientated()) {
                qryLabelIndexes[1] = Collections.min(allOverlappingQryLabels);
                qryLabelIndexes[0] = Collections.max(allOverlappingQryLabels);
            } else {
                qryLabelIndexes[0] = Collections.min(allOverlappingQryLabels);
                qryLabelIndexes[1] = Collections.max(allOverlappingQryLabels);
            }

            double minX = qry.getLabels()[qryLabelIndexes[0]].getMinX();
            double maxX = qry.getLabels()[qryLabelIndexes[1]].getMinX();

            overlap = new Rectangle2D.Double(
                    minX,
                    qry.getRectangle().getMinY(),
                    maxX - minX,
                    qry.getRectangle().getHeight());

        } else {
            overlap = new Rectangle2D.Double(0, 0, 0, 0);
        }

        return overlap;
    }
}
