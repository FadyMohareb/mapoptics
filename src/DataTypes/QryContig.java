package DataTypes;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/*
 * @author Josie
 */
@Deprecated
public class QryContig {

    private Rectangle2D rectangle;
    private Rectangle2D[] labels;
    private String[][] alignments;
    private double qryAlignStart;
    private double qryAlignEnd;
    private double refAlignStart;
    private double refAlignEnd;
    private String orientation;
    private boolean reOrientated = false;
    private String sequence;
    private String name;

    public QryContig(Rectangle2D rectangle, Rectangle2D[] labels, String[][] alignments, double qryAlignStart, double qryAlignEnd, double refAlignStart, double refAlignEnd, String orientation) {
        this.rectangle = rectangle;
        this.labels = labels;
        this.alignments = alignments;
        this.qryAlignStart = qryAlignStart;
        this.qryAlignEnd = qryAlignEnd;
        this.refAlignStart = refAlignStart;
        this.refAlignEnd = refAlignEnd;
        this.orientation = orientation;
    }

    public QryContig() {
    }

    public QryContig copy() {
        QryContig qry1 = this ;
        QryContig qry2 = new QryContig();
        ArrayList<Rectangle2D> labelRects = new ArrayList();

        // add variables that won't change
        qry2.setAlignments(qry1.getAlignments());
        qry2.setOrientation(qry1.getOrientation());
        qry2.setReOrientated(qry1.isReOrientated());
        qry2.setQryAlignStart(qry1.getQryAlignStart());
        qry2.setQryAlignEnd(qry1.getQryAlignEnd());
        qry2.setRefAlignStart(qry1.getRefAlignStart());
        qry2.setRefAlignEnd(qry1.getRefAlignEnd());
        // scale query rectangle
        qry2.setRectangle(qry1.getRectangle().getBounds2D());

        // scale all query rectangle labels
        for (Rectangle2D label : qry1.getLabels()) {
            labelRects.add(label.getBounds2D());
        }
        qry2.setLabels(labelRects.toArray(new Rectangle2D[labelRects.size()]));
        labelRects.clear();

        return qry2;
    }

    public QryContig copy(double hScale) {
        QryContig qry1 = this ;
        QryContig qry2 = new QryContig();
        ArrayList<Rectangle2D> labelRects = new ArrayList();

        // add variables that won't change
        qry2.setAlignments(qry1.getAlignments());
        qry2.setOrientation(qry1.getOrientation());
        qry2.setReOrientated(qry1.isReOrientated());
        qry2.setQryAlignStart(qry1.getQryAlignStart() * hScale);
        qry2.setQryAlignEnd(qry1.getQryAlignEnd() * hScale);
        qry2.setRefAlignStart(qry1.getRefAlignStart()* hScale);
        qry2.setRefAlignEnd(qry1.getRefAlignEnd() * hScale);
        // scale query rectangle
        qry2.setRectangle(qry1.getRectangle().getBounds2D());

        // scale all query rectangle labels
        for (Rectangle2D label : qry1.getLabels()) {
            labelRects.add(label.getBounds2D());
        }
        qry2.setLabels(labelRects.toArray(new Rectangle2D[labelRects.size()]));
        labelRects.clear();

        return qry2;
    }

    public Rectangle2D getRectangle() {
        return rectangle;
    }

    public void setRectangle(Rectangle2D rectangle) {
        this.rectangle = rectangle;
    }

    public Rectangle2D[] getLabels() {
        return labels;
    }

    public void setLabels(Rectangle2D[] labels) {
        this.labels = labels;
    }

    public String[][] getAlignments() {
        return alignments;
    }

    public void setAlignments(String[][] alignments) {
        this.alignments = alignments;
    }

    public double getQryAlignStart() {
        return qryAlignStart;
    }

    public void setQryAlignStart(double qryAlignStart) {
        this.qryAlignStart = qryAlignStart;
    }

    public double getQryAlignEnd() {
        return qryAlignEnd;
    }

    public void setQryAlignEnd(double qryAlignEnd) {
        this.qryAlignEnd = qryAlignEnd;
    }

    public double getRefAlignStart() {
        return refAlignStart;
    }

    public void setRefAlignStart(double refAlignStart) {
        this.refAlignStart = refAlignStart;
    }

    public double getRefAlignEnd() {
        return refAlignEnd;
    }

    public void setRefAlignEnd(double refAlignEnd) {
        this.refAlignEnd = refAlignEnd;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public boolean isReOrientated() {
        return reOrientated;
    }

    public void setReOrientated(boolean reOrientated) {
        this.reOrientated = reOrientated;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public void setName(String name) {
        this.name = name;
    }

    /*
     public String getName() {
        return name;
     }
     */


}
