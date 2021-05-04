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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // draw alignment of single reference and all contigs
        try {
            if (!model.getSelectedRefID().isEmpty()) {

                double scaleX = model.getRectangleTotalWidth() / (this.getWidth() * 0.9);
                double scaleY = model.getRectangleTotalHeight() / (this.getHeight() * 0.7);
                Reference ref = model.getSelectedRef();
                Map<Integer, Double> refSites = ref.getSites();
                Set<Integer> refAlignments = ref.getAlignmentSites();

                // Draw scale bar
                g2d.setColor(BLACK);
                int scaleWidth = (int) Math.floor(500000 / scaleX);
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
                        (refRectRaw.getX()  / scaleX ) + this.getWidth() / 20.0,
                        (refRectRaw.getY() / scaleY) + this.getHeight() / 6.66,
                        refRectRaw.getWidth() / scaleX,
                        refRectRaw.getHeight() / scaleY);

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

                    int position = (int) ((refSites.get(site) / scaleX) + refRectScaled.getX());
                    g2d.drawLine(position, refOffSetY, position, refOffSetY + refHeight);
                }

                // For each query, draw rectangle, sites and alignments
                for (Query qry : ref.getQueries()) {
                    // if there are saved deleted query contigs then don't draw them in the summary view
                    Rectangle2D qryRectRaw = qry.getRectangle();
                    Rectangle2D qryRectScaled = new Rectangle2D.Double(
                            (qryRectRaw.getX()  / scaleX ) + this.getWidth() / 20.0,
                            (qryRectRaw.getY() / scaleY) + this.getHeight() / 6.66,
                            qryRectRaw.getWidth() / scaleX,
                            qryRectRaw.getHeight() / scaleY);

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

                        int position = (int) ((qrySites.get(site).get(0) / scaleX) + qryRectScaled.getX());
                        g2d.drawLine(position, qryOffSetY, position, qryOffSetY + qryHeight);

                        g2d.setColor(BLACK);
                        // Draw alignment
                        if (match) {
                            for (int i : qryAlignments.get(site)) {
                                int refPositionX = (int) ((refSites.get(i) / scaleX) + refRectScaled.getX());
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
