package Datasets.UserEdited;

import DataTypes.QryContig;
import DataTypes.RefContig;
import Datasets.Default.RefViewData;
import UserInterface.ReferenceView;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/*
 * @author Josie
   stores all user changed data in reference view. This is what is displayed in
   the reference view panel
 */
public class UserRefData {

    // scale variables
    private static double panelLength;
    private static double panelHeight;
    private static double hScale;
    private static double vScale;
    private static double hShift;
    private static double vShift;

    // scale variables for the two panels
    private static double horZoom;
    private static double vertZoom;

    // RefAlignData hashmaps scaled and overlaps sorted
    private static LinkedHashMap<String, RefContig> references = new LinkedHashMap();
    private static LinkedHashMap<String, QryContig> queries = new LinkedHashMap();

    public static void resetData() {
        UserRefData.references = new LinkedHashMap();
        UserRefData.queries = new LinkedHashMap();
    }

    public static void setPanelLength(double panelLength) {
        UserRefData.panelLength = panelLength;
    }

    public static void setPanelHeight(double panelHeight) {
        UserRefData.panelHeight = panelHeight;
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
        UserRefData.queries = usrRefQueries;
    }

    public static void setHorZoom(double horZoom) {
        UserRefData.horZoom = horZoom;
    }

    public static void setVertZoom(double vertZoom) {
        UserRefData.vertZoom = vertZoom;
    }

    public static void setData() {
        // set all references to match default dataset
        RefContig ref1;
        for (String refId : RefViewData.getReferences().keySet()) {
            ref1 = RefViewData.getReferences(refId);
            RefContig ref2 = ref1.copy();
            references.put(refId, ref2);
        }

        // set all queries to match default dataset
        for (String refqryId : RefViewData.getQueries().keySet()) {
            QryContig qry1 = RefViewData.getQueries(refqryId);
            QryContig qry2 = qry1.copy();
            queries.put(refqryId, qry2);
        }
    }

    public static void resetDataToDefault() {
        String chosenRef = ReferenceView.getChosenRef();
        // set all references
        RefContig ref1 = RefViewData.getReferences(chosenRef);
        RefContig ref2 = ref1.copy();
        references.put(chosenRef, ref2);

        // set all queries
        QryContig qry1;
        for (String qryId : ref2.getConnections()) {
            qry1 = RefViewData.getQueries(chosenRef + "-" + qryId);
            QryContig qry2 = qry1.copy();
            queries.put(chosenRef + "-" + qryId, qry2);

        }
    }

    public static void resetDataToLastSaved() {
        String chosenRef = ReferenceView.getChosenRef();
        // set all references
        RefContig ref1 = SavedRefData.getReferences(chosenRef);
        RefContig ref2 = ref1.copy();
        ref2.setRectangle(resizeRect(ref1.getRectangle().getBounds2D()));
        Rectangle2D[] labels = new Rectangle2D[ref1.getLabels().length];
        for (int j = 0; j < ref1.getLabels().length; j++) {
            labels[j] = resizeRect(ref1.getLabels()[j].getBounds2D());
        }
        ref2.setLabels(labels);
        references.put(chosenRef, ref2);

        // set all queries
        for (String qryId : ref2.getConnections()) {
            QryContig qry1 = SavedRefData.getQueries(chosenRef + "-" + qryId);
            QryContig qry2 = qry1.copy();
            qry2.setRectangle(resizeRect(qry1.getRectangle().getBounds2D()));
            labels = new Rectangle2D[qry1.getLabels().length];
            for (int j = 0; j < qry1.getLabels().length; j++) {
                labels[j] = resizeRect(qry1.getLabels()[j].getBounds2D());
            }
            qry2.setLabels(labels);
            queries.put(chosenRef + "-" + qryId, qry2);

        }
    }

    public static void reCentreView(String refId) {
        ArrayList<Rectangle2D> labels = new ArrayList();
        // set scale variables again and recentre screen to left and rightmost contig
        setScaleVariables(refId);

        // rescale reference
        RefContig ref = references.get(refId);
        for (Rectangle2D label : ref.getLabels()) {
            labels.add(scaleRectangle(label.getBounds2D(), true));
        }
        ref.setRectangle(scaleRectangle(ref.getRectangle().getBounds2D(), false));
        ref.setLabels(labels.toArray(new Rectangle2D[labels.size()]));
        labels.clear();

        // loop through connecting queries and rescale those
        for (String qryId : ref.getConnections()) {
            QryContig qry = queries.get(refId + "-" + qryId).copy(hScale);
            for (Rectangle2D label : qry.getLabels()) {
                labels.add(scaleRectangle(label.getBounds2D(), true));
            }
            qry.setLabels(labels.toArray(new Rectangle2D[labels.size()]));
            qry.setRectangle(scaleRectangle(qry.getRectangle().getBounds2D(), false));
            queries.put(refId + "-" + qryId,qry);
            labels.clear();
        }

    }

    private static void setScaleVariables(String refId) {
        double minX = Double.POSITIVE_INFINITY;
        double maxX = Double.NEGATIVE_INFINITY;
        double rectMinX;
        double rectMaxX;

        // loop through all reference contigs and find the max and min
        rectMinX = references.get(refId).getRectangle().getMinX();
        if (rectMinX < minX) {
            minX = rectMinX;
        }
        rectMaxX = references.get(refId).getRectangle().getMaxX();
        if (rectMaxX > maxX) {
            maxX = rectMaxX;
        }

        // loop through all query contigs and find the max and min
        for (String qryId : references.get(refId).getConnections()) {
            rectMinX = queries.get(refId + "-" + qryId).getRectangle().getMinX();
            if (rectMinX < minX) {
                minX = rectMinX;
            }
            rectMaxX = queries.get(refId + "-" + qryId).getRectangle().getMaxX();
            if (rectMaxX > maxX) {
                maxX = rectMaxX;
            }
        }

        double minY = Double.POSITIVE_INFINITY;
        double maxY = Double.NEGATIVE_INFINITY;
        double rectMinY;
        double rectMaxY;

        // loop through all reference contigs and find the max and min
        rectMinY = references.get(refId).getRectangle().getMinY();
        if (rectMinY < minY) {
            minY = rectMinY;
        }
        rectMaxY = references.get(refId).getRectangle().getMaxY();
        if (rectMaxY > maxY) {
            maxY = rectMaxY;
        }

        // loop through all query contigs and find the max and min
        for (String qryId : references.get(refId).getConnections()) {
            rectMinY = queries.get(refId + "-" + qryId).getRectangle().getMinY();
            if (rectMinY < minY) {
                minY = rectMinY;
            }
            rectMaxY = queries.get(refId + "-" + qryId).getRectangle().getMaxY();
            if (rectMaxY > maxY) {
                maxY = rectMaxY;
            }
        }

        vScale = (panelHeight - panelHeight / 5 * 2.5) / (maxY - minY);
        hScale = (panelLength - panelLength / 12) / (maxX - minX);
        vShift = panelHeight / 5 * 1.5 - minY * vScale;
        hShift = panelLength / 24 - minX * hScale;

    }

    private static Rectangle2D scaleRectangle(Rectangle2D rect, boolean label) {
        double newX = rect.getMinX() * hScale + hShift;
        double newY = rect.getMinY() * vScale + vShift;
        double newW;
        if (label) {
            newW = rect.getWidth();
        } else {
            newW = rect.getWidth() * hScale;
        }
        double newH = rect.getHeight();
        rect.setRect(newX, newY, newW, newH);
        return rect;
    }

    private static Rectangle2D resizeRect(Rectangle2D rect) {
        AffineTransform at2 = AffineTransform.getScaleInstance(horZoom, vertZoom);
        // move everything
        rect = at2.createTransformedShape(rect).getBounds2D();
        return rect;
    }

    public static void align(String refId, String qryId, boolean left) {
        RefContig ref = references.get(refId);
        QryContig qry = queries.get(refId + "-" + qryId);

        double qryShift;
        double refShift;

        if (left) {
            qryShift = qry.getRectangle().getMinX() + qry.getQryAlignStart();
            refShift = ref.getRectangle().getMinX() + qry.getRefAlignStart();
        } else {
            qryShift = qry.getRectangle().getMinX() + qry.getQryAlignEnd();
            refShift = ref.getRectangle().getMinX() + qry.getRefAlignEnd();
        }
        double shift = refShift - qryShift;

        qry.setRectangle(moveRectangle(qry.getRectangle().getBounds2D(), shift));
        // scale query labels
        ArrayList<Rectangle2D> labels = new ArrayList();
        for (Rectangle2D label : qry.getLabels()) {
            labels.add(moveRectangle(label.getBounds2D(), shift));
        }
        qry.setLabels(labels.toArray(new Rectangle2D[labels.size()]));
    }

    private static Rectangle2D moveRectangle(Rectangle2D rect, double shift) {
        double newX = rect.getMinX() + shift;
        double newY = rect.getMinY();
        double newW = rect.getWidth();
        double newH = rect.getHeight();
        rect.setRect(newX, newY, newW, newH);
        return rect;
    }

}
