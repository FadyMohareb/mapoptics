package UserInterface;

import DataTypes.Query;
import DataTypes.Reference;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
 * @author Josie
 */
public class SummaryView extends JPanel {

    private final MapOpticsModel model;

    private static final Color LIGHT_GREY = new Color(244, 244, 244);
    private static final Color GREY = new Color(192, 192, 192);
    private static final Color DARK_GREY = new Color(80, 80, 80);
    private static final Color BLACK = new Color(30, 30, 30);
    private static final Color GREEN = new Color(97, 204, 10);

    public SummaryView(MapOpticsModel model) {
        this.model = model;
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
            if (!model.getSelectedRefID().isEmpty()) {

                double scale = model.getRectangleTotalWidth() / (this.getWidth() * 0.9);
                Reference ref = model.getSelectedRef();
                Map<Integer, Double> refSites = ref.getSites();
                Set<Integer> refAlignments = ref.getAlignmentSites();

                // Draw scale bar
                g2d.setColor(BLACK);
                int scaleWidth = (int) Math.floor(500000 / scale);
                g2d.drawLine(10, 10, 10 + scaleWidth, 10);
                for (int i = 0; i < 5; i++) {
                    int interval = (int) (10 + (Math.ceil(scaleWidth / 5.0) * i));
                    g2d.drawLine(interval, 5, interval, 15);
                }

                g2d.drawLine(10 + scaleWidth, 5, 10 + scaleWidth, 15);
                g2d.drawString("500 000 bp", 15, 28);

                // Draw reference rectangle and sites
                Rectangle2D refRectRaw = ref.getRectangle();
                Rectangle2D refRectScaled = new Rectangle2D.Double(
                        (refRectRaw.getX()  / scale ) + this.getWidth() / 20.0,
                        refRectRaw.getY() + 50,
                        refRectRaw.getWidth() / scale,
                        refRectRaw.getHeight());

                g2d.setColor(LIGHT_GREY);
                g2d.fill(refRectScaled);
                g2d.setColor(GREY);
                g2d.draw(refRectScaled);
                g2d.setColor(DARK_GREY);
                g2d.drawString("ID: " + ref.getRefID(),
                        (int) refRectScaled.getMaxX() - g2d.getFontMetrics().stringWidth("ID: " + ref.getRefID()),
                        (int) refRectScaled.getMinY() - 2);

                int refOffSetY = (int) refRectScaled.getY();
                int refHeight = (int) refRectScaled.getHeight();

                for (int site : refSites.keySet()) {
                    if (refAlignments.contains(site)) {
                        g2d.setColor(GREEN);
                    } else {
                        g2d.setColor(BLACK);
                    }

                    int position = (int) ((refSites.get(site) / scale) + refRectScaled.getX());
                    g2d.drawLine(position, refOffSetY, position, refOffSetY + refHeight);
                }

                // For each query, draw rectangle, sites and alignments
                for (Query qry : ref.getQueries()) {
                    Rectangle2D qryRectRaw = qry.getRectangle();
                    Rectangle2D qryRectScaled = new Rectangle2D.Double(
                            (qryRectRaw.getX()  / scale ) + this.getWidth() / 20.0,
                            qryRectRaw.getY() + 50,
                            qryRectRaw.getWidth() / scale,
                            qryRectRaw.getHeight());

                    g2d.setColor(LIGHT_GREY);
                    g2d.fill(qryRectScaled);
                    g2d.setColor(GREY);
                    g2d.draw(qryRectScaled);

                    int qryOffSetY = (int) qryRectScaled.getY();
                    int qryHeight = (int) qryRectScaled.getHeight();
                    Map<Integer, List<Double>> qrySites = qry.getSites();
                    Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();

                    for (int site : qry.getSites().keySet()) {
                        boolean match = false;
                        if (qryAlignments.containsKey(site)) {
                            match = true;
                            g2d.setColor(GREEN);
                        } else {
                            g2d.setColor(BLACK);
                        }

                        int position = (int) ((qrySites.get(site).get(0) / scale) + qryRectScaled.getX());
                        g2d.drawLine(position, qryOffSetY, position, qryOffSetY + qryHeight);

                        g2d.setColor(BLACK);
                        // Draw alignment
                        if (match) {
                            for (int i : qryAlignments.get(site)) {
                                int refPositionX = (int) ((refSites.get(i) / scale) + refRectScaled.getX());
                                int refPositionY = (int) (refRectScaled.getY() + refRectScaled.getHeight());
                                g2d.drawLine(position, qryOffSetY, refPositionX, refPositionY);
                            }
                        }
                    }
                }
            } else {
                Font font = new Font("Tahoma", Font.ITALIC, 12);
                    g2d.setFont(font);
                    g2d.drawString("Choose a reference contig to display SUMMARY VIEW",
                            this.getWidth() / 2 - 115 , this.getHeight()/2);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

// DEPRECATED
//
//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        this.setBackground(Color.white);
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//
//        // draw alignment of single reference and all contigs
//        try {
//
//            if (!chosenRef.equals("")) {
//
//                RefContig ref = SavedRefData.getReferences(chosenRef);
//
//                Rectangle2D refRect = ref.getRectangle();
//                // draw reference contig
//                g2d.setColor(new Color(244, 244, 244));
//                g2d.fill(refRect);
//                g2d.setColor(Color.lightGray);
//                g2d.draw(refRect);
//                g2d.setColor(new Color(80, 80, 80));
//                g2d.drawString("ID: " + chosenRef, (int) refRect.getMaxX() - g2d.getFontMetrics().stringWidth("ID: " + chosenRef), (int) refRect.getMinY() - 2);
//
//                double scaleWidth = (refRect.getWidth() / RawFileData.getRefContigs(chosenRef).getContigLen()) * 500000;
//                // draw scalebar
//                g2d.drawLine(10, 10, 10 + (int) scaleWidth, 10);
//                for (int i = 0; i < 6; i++) {
//                    g2d.drawLine((int) (10 + (scaleWidth / 5 * i)), 5, (int) (10 + (scaleWidth / 5 * i)), 15);
//                }
//
//                g2d.drawString("500 000 bp", 15, 28);
//
//                Rectangle2D qryRect;
//                QryContig qry;
//
//                // loop through rest of query contigs and draw one at a time
//                for (int i = 0; i < ref.getConnections().length; i++) {
//                    String[] connectingContigs = ref.getConnections();
//                    String qryId = connectingContigs[i];
//
//                    qry = SavedRefData.getQueries(chosenRef + "-" + qryId);
//
//                    // draw query contigs
//                    qryRect = qry.getRectangle();
//                    g2d.setColor(new Color(244, 244, 244));
//                    g2d.fill(qryRect);
//                    g2d.setColor(Color.lightGray);
//                    g2d.draw(qryRect);
//                    g2d.setColor(new Color(80, 80, 80));
//
//                    int refIndex;
//
//                    for (int j = 0; j < qry.getLabels().length; j++) {
//                        // draw the standard label
//                        boolean qrymatch = false;
//                        for (String[] alignment : qry.getAlignments()) {
//                            if (Integer.parseInt(alignment[1]) == (j + 1)) {
//                                qrymatch = true;
//                                // draw line linking the two
//                                refIndex = Integer.parseInt(alignment[0]) - 1;
//                                g2d.setColor(Color.black);
//                                g2d.drawLine((int) ref.getLabels()[refIndex].getCenterX(), (int) refRect.getMinY() + (int) refRect.getHeight(), (int) qry.getLabels()[j].getCenterX(), (int) qryRect.getMinY());
//                                // draw labels in green
//                                g2d.setColor(new Color(97, 204, 10));
//                                g2d.fill(qry.getLabels()[j]);
//                            }
//                        }
//                        if (!qrymatch) {
//                            g2d.setColor(Color.black);
//                            g2d.fill(qry.getLabels()[j]);
//                        }
//                    }
//                }
//
//                // loop through reference labels
//                for (int i = 0; i < ref.getLabels().length - 1; i++) {
//                    boolean refmatch = false;
//                    // loop through all matching queries
//                    for (String qryId : ref.getConnections()) {
//                        String key = chosenRef + "-" + qryId;
//                        for (String[] alignment : SavedRefData.getQueries(key).getAlignments()) {
//                            if (Integer.parseInt(alignment[0]) == (i + 1)) {
//                                refmatch = true;
//                                // draw labels in green
//                                g2d.setColor(new Color(97, 204, 10));
//                                g2d.fill(ref.getLabels()[i]);
//
//                            }
//                        }
//                    }
//                    if (!refmatch) {
//                        g2d.setColor(Color.black);
//                        g2d.fill(ref.getLabels()[i]);
//                    }
//                }
//
//            } else {
//                Font font = new Font("Tahoma", Font.ITALIC, 12);
//                    g2d.setFont(font);
//                    g2d.drawString("Choose a reference contig to display SUMMARY VIEW",  this.getWidth() / 2 - 115 , this.getHeight()/2);
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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
