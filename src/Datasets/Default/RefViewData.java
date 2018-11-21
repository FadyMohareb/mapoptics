package Datasets.Default;

import Algorithms.SortOverlap;
import DataTypes.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/*
    @author Josie
    Class containing hashmaps of references and queries where rectangle2D objects
    are scaled relative to the reference view panel
 */
public class RefViewData {

    // scale variables
    private static double panelLength;
    private static double panelHeight;
    private static double hScale;
    private static double vScale;
    private static double hShift;

    // hashmaps where rectangles are scaled relative to ref and overlaps sorted
    private static LinkedHashMap<String, RefContig> references = new LinkedHashMap();
    private static LinkedHashMap<String, QryContig> queries = new LinkedHashMap();

    public static void resetData() {
        RefViewData.references = new LinkedHashMap();
        RefViewData.queries = new LinkedHashMap();
    }

    public static void setPanelLength(double panelLength) {
        RefViewData.panelLength = panelLength;
    }

    public static void setPanelHeight(double panelHeight) {
        RefViewData.panelHeight = panelHeight;
    }

    public static LinkedHashMap<String, RefContig> getReferences() {
        return references;
    }

    public static LinkedHashMap<String, QryContig> getQueries() {
        return queries;
    }

    public static RefContig getReferences(String refId) {
        return references.get(refId);
    }

    public static QryContig getQueries(String refqryId) {
        return queries.get(refqryId);
    }

    public static void setData() {
        // get data from overlap algorithm so queries don't overlap
        LinkedHashMap<String, Rectangle2D> sortedRects = SortOverlap.getSortedRects();
        LinkedHashMap<String, Rectangle2D[]> sortedLabels = SortOverlap.getSortedLabels();
        
        ArrayList<Rectangle2D> labels = new ArrayList();
        
        // loop through each reference, rescale and recentre rectangles
        for (String refId : RawFileData.getReferences().keySet()) {
            RefContig ref1 = RawFileData.getReferences(refId);
            RefContig ref2 = ref1.copy();
            
            setScaleVariables(refId, sortedRects);
            ref2.setRectangle(scaleRectangle(ref2.getRectangle().getBounds2D(), false));
            for (Rectangle2D label : ref2.getLabels()) {
                labels.add(scaleRectangle(label.getBounds2D(), true));
            }
            ref2.setLabels(labels.toArray(new Rectangle2D[labels.size()]));
            references.put(refId, ref2);
            labels.clear();

            // loop through connecting queries and rescale those
            for (String qryId : ref2.getConnections()) {
                QryContig qry1 = RawFileData.getQueries(refId + "-" + qryId);
                QryContig qry2 = qry1.copy(hScale);
                qry2.setRectangle(scaleRectangle(sortedRects.get(refId + "-" + qryId).getBounds2D(), false));
                for (int i = 0; i < sortedLabels.get(refId + "-" + qryId).length; i++) {
                    labels.add(scaleRectangle(sortedLabels.get(refId + "-" + qryId)[i].getBounds2D(), true));
                }
                qry2.setLabels(labels.toArray(new Rectangle2D[labels.size()]));
                queries.put(refId + "-" + qryId, qry2);
                labels.clear();
            }
        }
    }

    private static void setScaleVariables(String refId, LinkedHashMap<String, Rectangle2D> sortedRects) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double rectMinX;
        double rectMaxX;

        // loop through all reference contigs and find the max and min
        rectMinX = RawFileData.getReferences(refId).getRectangle().getMinX();
        if (rectMinX < minX) {
            minX = rectMinX;
        }
        rectMaxX = RawFileData.getReferences(refId).getRectangle().getMaxX();
        if (rectMaxX > maxX) {
            maxX = rectMaxX;
        }

        // loop through all query contigs and find the max and min
        for (String qryId : RawFileData.getReferences(refId).getConnections()) {
            rectMinX = sortedRects.get(refId + "-" + qryId).getMinX();
            if (rectMinX < minX) {
                minX = rectMinX;
            }
            rectMaxX = sortedRects.get(refId + "-" + qryId).getMaxX();
            if (rectMaxX > maxX) {
                maxX = rectMaxX;
            }
        }

        double maxY = Double.NEGATIVE_INFINITY;
        double rectMaxY;

        // loop through all reference contigs and find the max and min
        rectMaxY = RawFileData.getReferences(refId).getRectangle().getMinY();
        if (rectMaxY > maxY) {
            maxY = rectMaxY;
        }

        // loop through all query contigs and find the max and min
        for (String qryId : RawFileData.getReferences(refId).getConnections()) {
            rectMaxY = sortedRects.get(refId + "-" + qryId).getMinY();
            if (rectMaxY > maxY) {
                maxY = rectMaxY;
            }
        }

        hScale = (panelLength - panelLength / 12) / (maxX - minX);
        vScale = (panelHeight - 20) / (maxY + 5);
        hShift = panelLength / 24 - minX * hScale;

    }

    private static Rectangle2D scaleRectangle(Rectangle2D rect, boolean label) {
        double newX = rect.getMinX() * hScale + hShift;
        double newY = rect.getMinY() * vScale;
        double newW;
        if (label) {
            newW = rect.getWidth();
        } else {
            newW = rect.getWidth() * hScale;
        }
        double newH = rect.getHeight() * vScale;
        rect.setRect(newX, newY, newW, newH);
        return rect;
    }
}
