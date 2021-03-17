package Datasets.Default;

import Algorithms.SortOverlap;
import DataTypes.QryContig;
import DataTypes.Query;
import DataTypes.RefContig;
import DataTypes.Reference;
import FileHandling.CmapReader;
import FileHandling.XmapReader;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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

    public static void setReferenceData(MapOpticsModel model) {
        Reference ref = model.getSelectedRef();

        Map<Integer, Query> queriesMap = XmapReader.getReferenceData(model);
        if (queriesMap == null) {
            System.out.println("Error Reading File");
            return;
        }

        List<Query> queries = new ArrayList<>(queriesMap.values());
        CmapReader.getReferenceData(model.getQryFile(), queriesMap);

        ref.setQueries(queries);
        setRectangles(ref, model);
    }

    public static void setRectangles(Reference ref, MapOpticsModel model) {
        // start ref rectangle with offset 0,0 size length, 50
        double lowestOffsetX = 0.0;
        double highestOffSetX = ref.getLength();
        int refAlignIndex = model.isReversed() ? 1 : 0;
        int qryAlignIndex = model.isReversed() ? 0 : 1;
        List<Rectangle2D> rects = new ArrayList<>();

        Rectangle2D refRect = new Rectangle2D.Double(0, 10, ref.getLength(), 50);
        ref.setRectangle(refRect);
        rects.add(refRect);

        for (Query qry : ref.getQueries()) {
            // isNegative maybe not needed?? - could just reverse site list when reorientate button pressed??
            boolean isNegative = qry.getOrientation().equals("-");
            String[] firstAlignment = qry.getFirstAlignment();

            // Get position of alignment on ref and qry contig.
            double refPos = ref.getSites().get(Integer.parseInt(firstAlignment[refAlignIndex]));
            double qryPos = qry.getSites()
                    .get(Integer.parseInt(firstAlignment[qryAlignIndex]))
                    .get(0);
            double qryOffsetX  = refPos - qryPos;

            if (isNegative) {
                int earliestSite = qry.getSites().firstKey();
                double earliestSitePos = qry.getSites().get(earliestSite).get(0);
                qryOffsetX = qryOffsetX + (qryPos - earliestSitePos);
            }

            Rectangle2D qryRect = new Rectangle2D.Double(qryOffsetX, 150, qry.getLength(), 50);

            if (qryOffsetX < lowestOffsetX) {
                lowestOffsetX = qryOffsetX;
            }

            double maxX = qryOffsetX + qry.getLength();

            if (maxX > highestOffSetX) {
                highestOffSetX = maxX;
            }

            qry.setRectangle(qryRect);
            rects.add(qryRect);
        }

        if (lowestOffsetX < 0.0) {
            refRect = new Rectangle2D.Double(refRect.getX() - lowestOffsetX,
                    refRect.getY(), refRect.getWidth(), refRect.getHeight());
            ref.setRectangle(refRect);

            for (Query qry : ref.getQueries()) {
                Rectangle2D old = qry.getRectangle();
                Rectangle2D rect = new Rectangle2D.Double(old.getX() - lowestOffsetX,
                        old.getY(), old.getWidth(), old.getHeight());
                qry.setRectangle(rect);
            }
        }

        model.totalRectangleWidth((highestOffSetX - lowestOffsetX));
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
}
