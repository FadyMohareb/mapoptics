package UserInterface;

import DataTypes.QryContig;
import DataTypes.RefContig;
import Datasets.Default.RawFileData;
import Datasets.UserEdited.SavedRefData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;


/*
 * @author Josie
 */
public class SummaryView extends JPanel {

    private static String chosenRef = "";

    public static void setChosenRef(String chosenRef) {
        SummaryView.chosenRef = chosenRef;
    }

//    public static String getChosenRef() {
//        return chosenRef;
//    }

    public SummaryView() {
        initComponents();
    }

    /*
    This method is currently unused. Commented out in case it becomes useful.
     */
//    public void zoomPanel(double horZoom, double vertZoom) {
//        AffineTransform at2 = AffineTransform.getScaleInstance(horZoom, vertZoom);
//        // move everything
//        // move and resize reference
//        for (String refId : SavedRefData.getReferences().keySet()) {
//            Rectangle2D rect;
//            RefContig ref = SavedRefData.getReferences(refId);
//            rect = at2.createTransformedShape(ref.getRectangle()).getBounds2D();
//            ref.setRectangle(rect);
//            Rectangle2D[] labels = new Rectangle2D[ref.getLabels().length];
//            for (int i = 0; i < ref.getLabels().length; i++) {
//                rect = at2.createTransformedShape(ref.getLabels()[i]).getBounds2D();
//                labels[i] = resize(rect);
//
//            }
//            ref.setLabels(labels);
//            // move all queries
//            for (String qryId : ref.getConnections()) {
//                QryContig qry = SavedRefData.getQueries(refId + "-" + qryId);
//                rect = at2.createTransformedShape(qry.getRectangle()).getBounds2D();
//                qry.setRectangle(rect);
//                labels = new Rectangle2D[qry.getLabels().length];
//                for (int i = 0; i < qry.getLabels().length; i++) {
//                    rect = at2.createTransformedShape(qry.getLabels()[i]).getBounds2D();
//                    labels[i] = resize(rect);
//                }
//                qry.setLabels(labels);
//            }
//        }
//    }
//
//    private Rectangle2D resize(Rectangle2D rect) {
//        rect.setRect(rect.getMinX(), rect.getMinY(), 1, rect.getHeight());
//        return rect;
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw alignment of single reference and all contigs
        try {

            if (!chosenRef.equals("")) {

                RefContig ref = SavedRefData.getReferences(chosenRef);

                Rectangle2D refRect = ref.getRectangle();
                // draw reference contig
                g2d.setColor(new Color(244, 244, 244));
                g2d.fill(refRect);
                g2d.setColor(Color.lightGray);
                g2d.draw(refRect);
                g2d.setColor(new Color(80, 80, 80));
                g2d.drawString("ID: " + chosenRef, (int) refRect.getMaxX() - g2d.getFontMetrics().stringWidth("ID: " + chosenRef), (int) refRect.getMinY() - 2);
                
                double scaleWidth = (refRect.getWidth() / RawFileData.getRefContigs(chosenRef).getContigLen()) * 500000;
                // draw scalebar
                g2d.drawLine(10, 10, 10 + (int) scaleWidth, 10);
                for (int i = 0; i < 6; i++) {
                    g2d.drawLine((int) (10 + (scaleWidth / 5 * i)), 5, (int) (10 + (scaleWidth / 5 * i)), 15);
                }

                g2d.drawString("500 000 bp", 15, 28);

                Rectangle2D qryRect;
                QryContig qry;

                // loop through rest of query contigs and draw one at a time
                for (int i = 0; i < ref.getConnections().length; i++) {
                    String[] connectingContigs = ref.getConnections();
                    String qryId = connectingContigs[i];

                    qry = SavedRefData.getQueries(chosenRef + "-" + qryId);

                    // draw query contigs
                    qryRect = qry.getRectangle();
                    g2d.setColor(new Color(244, 244, 244));
                    g2d.fill(qryRect);
                    g2d.setColor(Color.lightGray);
                    g2d.draw(qryRect);
                    g2d.setColor(new Color(80, 80, 80));

                    int refIndex;

                    for (int j = 0; j < qry.getLabels().length; j++) {
                        // draw the standard label
                        boolean qrymatch = false;
                        for (String[] alignment : qry.getAlignments()) {
                            if (Integer.parseInt(alignment[1]) == (j + 1)) {
                                qrymatch = true;
                                // draw line linking the two
                                refIndex = Integer.parseInt(alignment[0]) - 1;
                                g2d.setColor(Color.black);
                                g2d.drawLine((int) ref.getLabels()[refIndex].getCenterX(), (int) refRect.getMinY() + (int) refRect.getHeight(), (int) qry.getLabels()[j].getCenterX(), (int) qryRect.getMinY());
                                // draw labels in green
                                g2d.setColor(new Color(97, 204, 10));
                                g2d.fill(qry.getLabels()[j]);
                            }
                        }
                        if (!qrymatch) {
                            g2d.setColor(Color.black);
                            g2d.fill(qry.getLabels()[j]);
                        }
                    }
                }

                // loop through reference labels
                for (int i = 0; i < ref.getLabels().length - 1; i++) {
                    boolean refmatch = false;
                    // loop through all matching queries
                    for (String qryId : ref.getConnections()) {
                        String key = chosenRef + "-" + qryId;
                        for (String[] alignment : SavedRefData.getQueries(key).getAlignments()) {
                            if (Integer.parseInt(alignment[0]) == (i + 1)) {
                                refmatch = true;
                                // draw labels in green
                                g2d.setColor(new Color(97, 204, 10));
                                g2d.fill(ref.getLabels()[i]);

                            }
                        }
                    }
                    if (!refmatch) {
                        g2d.setColor(Color.black);
                        g2d.fill(ref.getLabels()[i]);
                    }
                }

            } else {
                Font font = new Font("Tahoma", Font.ITALIC, 12);
                    g2d.setFont(font);
                    g2d.drawString("Choose a reference contig to display SUMMARY VIEW",  this.getWidth() / 2 - 115 , this.getHeight()/2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }
}
