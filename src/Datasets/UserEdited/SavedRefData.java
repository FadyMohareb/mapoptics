package Datasets.UserEdited;

import DataTypes.*;
import Datasets.Default.SummaryViewData;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;

/*
 * @author Josie
   stores all information from the user edited reference view when saved
   the summary view panel reads from this class
 */
@Deprecated
public class SavedRefData {

    // RefAlignData hashmaps scaled and overlaps sorted
    private static LinkedHashMap<String, RefContig> references = new LinkedHashMap();
    private static LinkedHashMap<String, QryContig> queries = new LinkedHashMap();

    // scale variables for the two panels
    private static double horZoom;
    private static double vertZoom;

    public static void resetData() {
        SavedRefData.references = new LinkedHashMap();
        SavedRefData.queries = new LinkedHashMap();
    }

    public static void setData() {
        // set all references
        for (String refId : SummaryViewData.getReferences().keySet()) {
            RefContig ref1 = SummaryViewData.getReferences(refId);
            RefContig ref2 = ref1.copy();
            references.put(refId, ref2);
        }

        // set all queries
        for (String refqryId : SummaryViewData.getQueries().keySet()) {
            QryContig qry1 = SummaryViewData.getQueries(refqryId);
            QryContig qry2 = qry1.copy();
            queries.put(refqryId, qry2);
        }
    }

    public static void saveAllData() {
        for (String refId : references.keySet()) {
            saveOneData(refId);
        }
    }

    public static void saveOneData(String refId) {
        // rescale and set all data to store saved changes from reference view
        RefContig ref1 = UserRefData.getReferences(refId);
        RefContig ref2 = ref1.copy();
        ref2.setRectangle(resizeRect(ref1.getRectangle().getBounds2D()));
        Rectangle2D[] labels = new Rectangle2D[ref1.getLabels().length];
        for (int j = 0; j < ref1.getLabels().length; j++) {
            labels[j] = resizeRect(ref1.getLabels()[j].getBounds2D());
        }
        ref2.setLabels(labels);
        references.put(refId, ref2);

        // rescale all queries for summary view
        for (String qryId : ref1.getConnections()) {
            QryContig qry1 = UserRefData.getQueries(refId + "-" + qryId);
            QryContig qry2 = qry1.copy();
            qry2.setRectangle(resizeRect(qry1.getRectangle().getBounds2D()));
            labels = new Rectangle2D[qry1.getLabels().length];
            for (int j = 0; j < qry1.getLabels().length; j++) {
                labels[j] = resizeRect(qry1.getLabels()[j].getBounds2D());
            }
            qry2.setLabels(labels);
            queries.put(refId + "-" + qryId, qry2);
        }
    }

    private static Rectangle2D resizeRect(Rectangle2D rect) {
        AffineTransform at2 = AffineTransform.getScaleInstance(horZoom, vertZoom);
        rect = at2.createTransformedShape(rect).getBounds2D();
        return rect;
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

    public static void setQueries(LinkedHashMap<String, QryContig> usrRefQueries) {
        SavedRefData.queries = usrRefQueries;
    }

    public static void setHorZoom(double horZoom) {
        SavedRefData.horZoom = horZoom;
    }

    public static void setVertZoom(double vertZoom) {
        SavedRefData.vertZoom = vertZoom;
    }
}
