<<<<<<< HEAD
package DataTypes;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/*
 * @author Josie
 */
@Deprecated
public class RefContig {

    private Rectangle2D rectangle;
    private Rectangle2D[] labels;
    private String[] connections;
    private String sequence;
    private String name;

    public RefContig(Rectangle2D rectangle, Rectangle2D[] labels, String[] connections) {
        this.rectangle = rectangle;
        this.labels = labels;
        this.connections = connections;
    }

    public RefContig() {
    }

    public RefContig copy() {
        RefContig ref1 = this;
        RefContig ref2 = new RefContig();
        ArrayList<Rectangle2D> labelRects = new ArrayList();

        // add variables that won't change
        ref2.setConnections(ref1.getConnections());
        ref2.setName(ref1.getName());
        ref2.setSequence(ref1.getSequence());

        // scale query rectangle
        ref2.setRectangle(ref1.getRectangle().getBounds2D());

        // scale all query rectangle labels
        for (Rectangle2D label : ref1.getLabels()) {
            labelRects.add(label.getBounds2D());
        }
        ref2.setLabels(labelRects.toArray(new Rectangle2D[labelRects.size()]));
        labelRects.clear();

        return ref2;
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

    public String[] getConnections() {
        return connections;
    }

    public void setConnections(String[] connections) {
        this.connections = connections;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
=======
package DataTypes;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/*
 * @author Josie
 */
@Deprecated
public class RefContig {

    private Rectangle2D rectangle;
    private Rectangle2D[] labels;
    private String[] connections;
    private String sequence;
    private String name;

    public RefContig(Rectangle2D rectangle, Rectangle2D[] labels, String[] connections) {
        this.rectangle = rectangle;
        this.labels = labels;
        this.connections = connections;
    }

    public RefContig() {
    }

    public RefContig copy() {
        RefContig ref1 = this;
        RefContig ref2 = new RefContig();
        ArrayList<Rectangle2D> labelRects = new ArrayList();

        // add variables that won't change
        ref2.setConnections(ref1.getConnections());
        ref2.setName(ref1.getName());
        ref2.setSequence(ref1.getSequence());

        // scale query rectangle
        ref2.setRectangle(ref1.getRectangle().getBounds2D());

        // scale all query rectangle labels
        for (Rectangle2D label : ref1.getLabels()) {
            labelRects.add(label.getBounds2D());
        }
        ref2.setLabels(labelRects.toArray(new Rectangle2D[labelRects.size()]));
        labelRects.clear();

        return ref2;
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

    public String[] getConnections() {
        return connections;
    }

    public void setConnections(String[] connections) {
        this.connections = connections;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
>>>>>>> mapOpticsv2/master
