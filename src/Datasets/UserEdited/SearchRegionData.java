package Datasets.UserEdited;

import DataTypes.*;
import UserInterface.QueryView;
import java.awt.geom.*;

/*
 * @author Josie
   stores data on the contigs when a region is searched, these are rescaled
   to for the view
 */
@Deprecated
public class SearchRegionData {

    // Query alignment view data hashmaps set with those coordinates
    private static QryContig qry = new QryContig();
    private static RefContig ref = new RefContig();

    public static void resetData() {
        SearchRegionData.ref = new RefContig();
        SearchRegionData.qry = new QryContig();
    }
     // To do: can probably delete
    public static void setRegion(double shift, double zoom) {
        AffineTransform at = AffineTransform.getTranslateInstance(-shift, 0);
        AffineTransform at2 = AffineTransform.getScaleInstance(zoom, 1);
        
        String chosenRef = QueryView.getChosenRef();
        String chosenQry = QueryView.getChosenQry();

        QryContig qry1 = UserQryData.getQueries(chosenRef + "-" + chosenQry);
        qry = qry1.copy() ;
        
        // resize query to fit just searched region on query view panel
        Rectangle2D rect = at.createTransformedShape(qry1.getRectangle()).getBounds2D();
        rect = at2.createTransformedShape(rect).getBounds2D();
        qry.setRectangle(rect);
        Rectangle2D[] labels = new Rectangle2D[qry1.getLabels().length];
        for (int i = 0; i < qry1.getLabels().length; i++) {
            rect = at.createTransformedShape(qry1.getLabels()[i]).getBounds2D();
            rect = at2.createTransformedShape(rect).getBounds2D();
            labels[i] = resize(rect);

        }
        qry.setLabels(labels);
        
        // resize reference to fit just searched region on query view panel
        RefContig ref1 = UserQryData.getReferences(chosenRef + "-" + chosenQry);
        ref = ref1.copy();
        rect = at.createTransformedShape(ref1.getRectangle()).getBounds2D();
        rect = at2.createTransformedShape(rect).getBounds2D();
        ref.setRectangle(rect);
        labels = new Rectangle2D[ref1.getLabels().length];
        for (int i = 0; i < ref1.getLabels().length; i++) {
            rect = at.createTransformedShape(ref1.getLabels()[i]).getBounds2D();
            rect = at2.createTransformedShape(rect).getBounds2D();
            labels[i] = resize(rect);
        }
        ref.setLabels(labels);

    }
    
    
    private static Rectangle2D resize(Rectangle2D rect) {
        rect.setRect(rect.getMinX(), rect.getMinY(), 1, rect.getHeight());
        return rect;
    }

    public static QryContig getQry() {
        return qry;
    }

    public static RefContig getRef() {
        return ref;
    }
}
