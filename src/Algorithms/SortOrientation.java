package Algorithms;

import DataTypes.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/*
 * @author Josie
 */
public class SortOrientation {

    public static LinkedHashMap<String, QryContig> sortAllOrientation(LinkedHashMap<String, RefContig> references,
                                                                      LinkedHashMap<String, QryContig> queries) {

        boolean orientated;
        QryContig orientedQuery;

        for (String refId : references.keySet()) {
            for (String qryId : references.get(refId).getConnections()) {

                QryContig qry = queries.get(refId + "-" + qryId);
                RefContig ref = references.get(refId);
                orientated = qry.getOrientation().equals("+") ;

                if (!orientated) {
                    // sort orientation
                    orientedQuery = sortOneOrientation(qry, ref);
                    queries.put(refId + "-" + qryId, orientedQuery);
                }
            }
        }

        // sort overlaps 
        SortOverlap.sortOverlaps(references, queries, 5);
        Rectangle2D[] labels;
        for (String refqryId : queries.keySet()) {
            labels = new Rectangle2D[SortOverlap.getSortedLabels(refqryId).length];
            queries.get(refqryId).setRectangle(SortOverlap.getSortedRects(refqryId).getBounds2D());
            for (int i = 0; i < labels.length; i++) {
                labels[i] = SortOverlap.getSortedLabels(refqryId)[i].getBounds2D();
            }
            queries.get(refqryId).setLabels(labels);
        }

        return queries;
    }

    public static QryContig sortOneOrientation(QryContig qry, RefContig ref) {
        // swap start and end alignments
        double end = qry.getQryAlignEnd();
        double start = qry.getQryAlignStart();
        qry.setQryAlignStart(qry.getRectangle().getWidth() - end);
        qry.setQryAlignEnd(qry.getRectangle().getWidth() - start);

        // reorder labels
        double centre = qry.getRectangle().getMinX() + qry.getRectangle().getWidth() / 2;
        Rectangle2D[] labels = qry.getLabels();
        double newMinX;
        for (Rectangle2D rectangle2D : labels) {
            if (rectangle2D.getMinX() < centre) {
                newMinX = centre + (centre - rectangle2D.getMinX());
            } else {
                newMinX = centre - (rectangle2D.getMinX() - centre);
            }
            rectangle2D.setRect(newMinX, rectangle2D.getMinY(), 1, rectangle2D.getHeight());
        }
        qry.setLabels(labels);
        
        String orientation = qry.getOrientation() ;

        if (orientation.equals("-")) {
            qry.setOrientation("+");
        } else if (orientation.equals("+")) {
            qry.setOrientation("-");
        }

        qry.setReOrientated(!qry.isReOrientated());
        
        double qryShift = qry.getRectangle().getMinX() + qry.getQryAlignStart();
        double refShift = ref.getRectangle().getMinX() + qry.getRefAlignStart();
            
        double shift = refShift - qryShift;

        qry.setRectangle(moveRectangle(qry.getRectangle().getBounds2D(), shift));
        // scale query labels
        ArrayList<Rectangle2D> labelsA = new ArrayList<>();
        for (Rectangle2D label : qry.getLabels()) {
            labelsA.add(moveRectangle(label.getBounds2D(), shift));
        }
        qry.setLabels(labelsA.toArray(new Rectangle2D[0]));
        
        
        return qry;
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
