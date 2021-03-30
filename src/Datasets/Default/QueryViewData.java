package Datasets.Default;

//import Algorithms.SortOrientation;
import DataTypes.*;
import java.awt.geom.Rectangle2D;
import java.util.*;

/*
    @author Josie
    Class containing hashmaps of references and queries where rectangle2D objects
    are scaled relative to the query view panel and query contig
 */
public class QueryViewData {

    // scale variables
    private static double panelLength;
    private static double panelHeight;
    private static double hScale;
    private static double vScale;
    private static double hShift;

    // Query alignment view data hashmaps set with those coordinates
    private static LinkedHashMap<String, QryContig> queries = new LinkedHashMap();
    private static LinkedHashMap<String, RefContig> references = new LinkedHashMap();
    private static LinkedHashMap<String, String[]> qryConnections = new LinkedHashMap();

    public static void resetData() {
        QueryViewData.references = new LinkedHashMap();
        QueryViewData.queries = new LinkedHashMap();
        QueryViewData.qryConnections = new LinkedHashMap();
    }

    /*public static void setData() {
        ArrayList<Rectangle2D> labels = new ArrayList();
        ArrayList<String> qryIds = new ArrayList();
        for (String refqryId : RawFileData.getQueries().keySet()) {
            String refId = refqryId.split("-")[0];
            String qryId = refqryId.split("-")[1];
            // get raw data
            QryContig qry1 = RawFileData.getQueries(refqryId);
            RefContig ref1 = RawFileData.getReferences(refId) ;
            // set scale variables
            setScaleVariables(qry1, ref1);
            // scale rectangles and set query data
            QryContig qry2 = qry1.copy(hScale);
            qry2.setRectangle(scaleRectangle(qry2.getRectangle().getBounds2D(), 7, false));
            for (Rectangle2D label : qry2.getLabels()) {
                labels.add(scaleRectangle(label.getBounds2D(), 7, true));
            }
            qry2.setLabels(labels.toArray(new Rectangle2D[labels.size()]));
            labels.clear();
            // scale rectangles and set reference data
            RefContig ref2 = ref1.copy();
            ref2.setRectangle(scaleRectangle(ref1.getRectangle().getBounds2D(), 3, false));
            for (Rectangle2D label : ref1.getLabels()) {
                labels.add(scaleRectangle(label.getBounds2D(), 3, true));
            }
            ref2.setLabels(labels.toArray(new Rectangle2D[labels.size()]));
            labels.clear();
            // if negatively oriented, reorientate for query view
            if (qry2.getOrientation().equals("-")) {
                SortOrientation.sortOneOrientation(qry2, ref2);
            }
            if (!qryIds.contains(qryId)) {
                qryIds.add(qryId);
            }
            queries.put(refqryId, qry2);
            references.put(refqryId, ref2);
        }
*/
        /*// add all references to query connections (unique to this view)
        ArrayList<String> connections = new ArrayList();
        String[] refConnections;
        for (int i = 0; i < qryIds.size(); i++) {
            String qryId1 = qryIds.get(i);
            for (String refId : RawFileData.getReferences().keySet()) {
                refConnections = RawFileData.getReferences(refId).getConnections();
                for (String qryId2 : refConnections) {
                    if (qryId2.equals(qryId1)) {
                        connections.add(refId);
                    }
                }
            }
            qryConnections.put(qryId1, connections.toArray(new String[connections.size()]));
            connections.clear();
        }
    }*/

    private static void setScaleVariables(QryContig qry, RefContig ref) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double rectMinX;
        double rectMaxX;

        // check size of query rectangle and find the max and min
        if (qry.getOrientation().equals("-")) {
            double qryShift = qry.getRectangle().getMinX() + qry.getRectangle().getWidth() - qry.getQryAlignEnd();
            double refShift = ref.getRectangle().getMinX() + qry.getRefAlignStart();
            double shift = refShift - qryShift;

            rectMinX = qry.getRectangle().getMinX() + shift;
            rectMaxX = qry.getRectangle().getMaxX() + shift;
        } else {
            rectMinX = qry.getRectangle().getMinX();
            rectMaxX = qry.getRectangle().getMaxX();
        }

        if (rectMinX < minX) {
            minX = rectMinX;
        }
        if (rectMaxX > maxX) {
            maxX = rectMaxX;
        }

        // check regions where query connects to reference and find max and min
        rectMinX = ref.getRectangle().getMinX() + qry.getRefAlignStart();
        if (rectMinX < minX) {
            minX = rectMinX;
        }
        rectMaxX = ref.getRectangle().getMinX() + qry.getRefAlignEnd();
        if (rectMaxX > maxX) {
            maxX = rectMaxX;
        }

        hScale = (panelLength - 200) / (maxX - minX);
        vScale = (panelHeight - 20) / 10;
        hShift = 100 - minX * hScale;
    }

    private static Rectangle2D scaleRectangle(Rectangle2D rect, int pos, boolean label) {
        double newX = rect.getMinX() * hScale + hShift;
        double newY = pos * vScale;
        double newW;
        if (label) {
            newW = 1;
        } else {
            newW = rect.getWidth() * hScale;
        }
        double newH = vScale;
        rect.setRect(newX, newY, newW, newH);
        return rect;
    }

    public static void setPanelLength(double panelLength) {
        QueryViewData.panelLength = panelLength;
    }

    public static void setPanelHeight(double panelHeight) {
        QueryViewData.panelHeight = panelHeight;
    }

    public static LinkedHashMap<String, QryContig> getQueries() {
        return queries;
    }

    public static LinkedHashMap<String, RefContig> getReferences() {
        return references;
    }

    public static QryContig getQueries(String refqryId) {
        return queries.get(refqryId);
    }

    public static RefContig getReferences(String refqryId) {
        return references.get(refqryId);
    }

    public static LinkedHashMap<String, String[]> getQryConnections() {
        return qryConnections;
    }

    public static String[] getQryConnections(String qryId) {
        return qryConnections.get(qryId);
    }

}