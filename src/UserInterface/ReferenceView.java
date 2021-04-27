package UserInterface;

import Algorithms.CalculateOverlaps;
import DataTypes.Query;
import DataTypes.Reference;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
 * @author Josie
 */
public class ReferenceView extends JPanel {

    private static Query draggedShape = null;
    private static String chosenRef = "";
    private static String chosenQry = "";
    private String movedQry = "";
    private static String labelStyle = "match";
    private static boolean confidenceView = false;
    private static boolean overlapView = false;
    private static String position = "";
    private static int mouseX = 0;
    private static int mouseY = 0;
    private static int lowConf = 20;
    private static int highConf = 40;
    private static int lowCov = 20;
    private static int highCov = 50;
    private static int lowQual = 20;
    private static int highQual = 90;
    private double scaleMultiplier = 1.0d;
    private int windowOffX = 0;
    private int windowOffY = 0;
    private final MapOpticsModel model;

    private static final Color LIGHT_GREY = new Color(244, 244, 244);
    private static final Color GREY = new Color(192, 192, 192);
    private static final Color DARK_GREY = new Color(80, 80, 80);
    private static final Color BLACK = new Color(30, 30, 30);
    private static final Color GREEN = new Color(97, 204, 10);
    private static final Color AMBER = new Color(255, 204, 0);
    private static final Color RED = new Color(204, 0, 0);
    private static final Color YELLOW = new Color(255, 255, 0, 80);
    private static final Stroke DASHED = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_BEVEL, 0, new float[]{6, 2}, 2);
    private static final Stroke DOTTED = new BasicStroke(1, BasicStroke.CAP_BUTT,
            BasicStroke.JOIN_MITER, 1, new float[]{1, 2}, 2);

    public static void setChosenRef(String chosenRef) {
        ReferenceView.chosenRef = chosenRef;
    }

    public static void setChosenQry(String chosenQry) {
        ReferenceView.chosenQry = chosenQry;
    }

    private void setMovedQry(String movedQry) {
        this.movedQry = movedQry;
    }

    public static String getChosenRef() {
        return chosenRef;
    }

    public static String getChosenQry() {
        return chosenQry;
    }

    public static void setStyle(String style) {
        ReferenceView.labelStyle = style;
    }

    public static void setConfidenceView(boolean confidenceView) {
        ReferenceView.confidenceView = confidenceView;
    }

    public static void setOverlapView(boolean overlapView) {
        ReferenceView.overlapView = overlapView;
    }

    public static void setPosition(String position) {
        ReferenceView.position = position;
    }

    public static void setMouseX(int mouseX) {
        ReferenceView.mouseX = mouseX;
    }

    public static void setMouseY(int mouseY) {
        ReferenceView.mouseY = mouseY;
    }

    public static void setLowConf(int lowConf) {
        ReferenceView.lowConf = lowConf;
    }

    public static void setHighConf(int highConf) {
        ReferenceView.highConf = highConf;
    }

    public static void setLowCov(int lowCov) {
        ReferenceView.lowCov = lowCov;
    }

    public static void setHighCov(int highCov) {
        ReferenceView.highCov = highCov;
    }

    public static void setLowQual(int lowQual) {
        ReferenceView.lowQual = lowQual;
    }

    public static void setHighQual(int highQual) {
        ReferenceView.highQual = highQual;
    }

    public ReferenceView(MapOpticsModel model) {

        this.model = model;
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        addMouseListener(myMouseAdapter);
        addMouseMotionListener(myMouseAdapter);
        initComponents();
    }

    public void zoomIn() {
        if (scaleMultiplier < 35) {
            scaleMultiplier *= 1.2;
        }
    }

    public void zoomOut() {
        if (scaleMultiplier > 0.05) {
            scaleMultiplier /= 1.2;
        }
    }

    public void reCenter() {
        scaleMultiplier = 1.0;
        windowOffX = 0;
        windowOffY = 0;
    }

    // METHODS TO DO WITH REPAINTING THE PAINT COMPONENT

    @Override
    // Can be refactored into smaller methods
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        int yOff = this.getHeight() / 5;
        Stroke defaultStroke = g2d.getStroke();

        // draw alignment of single reference and all contigs
        try {
            if (!model.getSelectedRefID().isEmpty()) {

                Font defaultFont = g2d.getFont();
                Font fontB = new Font("Tahoma", Font.BOLD, 12);
                g2d.setFont(fontB);
                g2d.drawString("Reference Dataset:  ", 20, 20);
                g2d.drawString("Query Dataset:  ", 20, 40);
                int refStringLen = g2d.getFontMetrics().stringWidth("Reference Dataset:  ");
                int qryStringLen = g2d.getFontMetrics().stringWidth("Query Dataset:  ");
                g2d.setFont(defaultFont);
                g2d.drawString(model.getRefFile().getName(), refStringLen + 20, 20);
                g2d.drawString(model.getQryFile().getName(), qryStringLen + 20, 40);

                double defaultScale = model.getRectangleTotalWidth() / (this.getWidth() * 0.9);
                double scaleX = defaultScale / scaleMultiplier;
                double scaleY = model.getRectangleTotalHeight() / ((this.getHeight() - yOff) * 0.7);
                Reference ref = model.getSelectedRef();
                Map<Integer, Double> refSites = ref.getSites();
                Set<Integer> refAlignments = ref.getAlignmentSites();
                List<Double> overlapRegions = CalculateOverlaps.getOverlapRegions(ref);


                // Draw reference rectangle and sites
                Rectangle2D refRectScaled;
                Rectangle2D refRectRaw = ref.getRectangle();

                double refOff = (((refRectRaw.getX() / defaultScale)
                        + windowOffX - (this.getWidth() / 2.0))
                        * scaleMultiplier) + (this.getWidth() / 2.0);

                double refWidth = refRectRaw.getWidth() / scaleX;

                refRectScaled = new Rectangle2D.Double(
                        refOff + (this.getWidth() / 20.0),
                        (refRectRaw.getY() / scaleY) + (this.getHeight() / 6.66) + yOff + windowOffY,
                        refWidth,
                        refRectRaw.getHeight() / scaleY);

                ref.setRefViewRect(refRectScaled);

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

                if (overlapView) {
                    for (int i = 0; i < overlapRegions.size(); i += 2) {
                        g2d.setColor(YELLOW);
                        int position = (int) ((int) (overlapRegions.get(i) / scaleX) + refRectScaled.getX());
                        Rectangle2D overlap = new Rectangle2D.Double(position, refRectScaled.getY(), (overlapRegions.get(i+1) - overlapRegions.get(i)) / scaleX, refRectScaled.getHeight());
                        g2d.fill(overlap);
                    }
                }

                for (int site : refSites.keySet()) {
                    if (labelStyle.equals("match")) {
                        if (refAlignments.contains(site)) {
                            g2d.setColor(GREEN);
                        } else {
                            g2d.setColor(BLACK);
                        }
                    } else {
                        g2d.setColor(BLACK);
                    }


                    int position = (int) ((refSites.get(site) / scaleX) + refRectScaled.getX());
                    g2d.drawLine(position, refOffSetY, position, refOffSetY + refHeight);
                }

                g2d.setColor(BLACK);
                drawScaleBar(g2d, refRectScaled);

                // For each query, draw rectangle, sites and alignments
                for (Query qry : ref.getQueries()) {
                    if (ref.getDelQryIDs().contains(Integer.parseInt(qry.getID()))) {
                        continue;
                    }

                    Rectangle2D qryRectScaled;
                    Rectangle2D qryRectRaw = qry.getRectangle();

                    double qryOff = (((qryRectRaw.getX() / defaultScale)
                            + windowOffX - (this.getWidth() / 2.0))
                            * scaleMultiplier) + (this.getWidth() / 2.0);

                    double qryWidth = qryRectRaw.getWidth() / scaleX;

                    qryRectScaled = new Rectangle2D.Double(

                            qryOff + (this.getWidth() / 20.0) + qry.getRefViewOffsetX(),
                            (qryRectRaw.getY() / scaleY) + (this.getHeight() / 6.66) + yOff + windowOffY + qry.getRefViewOffsetY(),
                            qryWidth,
                            qryRectRaw.getHeight() / scaleY);

                        qry.setRefViewRect(qryRectScaled);

                        g2d.setColor(LIGHT_GREY);
                        g2d.fill(qryRectScaled);
                        g2d.setColor(GREY);
                        g2d.draw(qryRectScaled);

                        g2d.setColor(DARK_GREY);
                        g2d.drawString("ID: " + qry.getID(),
                                (int) qryRectScaled.getMaxX() - g2d.getFontMetrics().stringWidth("ID: " + qry.getID()),
                                (int) qryRectScaled.getMaxY() + 14);

                        int qryOffSetY = (int) qryRectScaled.getY();
                        int qryHeight = (int) qryRectScaled.getHeight();
                        Map<Integer, Double> qrySites = qry.getRefViewSites();
                        Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();

                    for (int site : qry.getRefViewSites().keySet()) {
                        boolean match = false;
                        if (qryAlignments.containsKey(site)) {
                            match = true;
                            if (labelStyle.equals("match")) {
                                g2d.setColor(GREEN);
                            }
                        } else {
                            g2d.setColor(BLACK);
                        }

                        if (labelStyle.equals("coverage")) {
                            Double coverage = qry.getSites().get(site).get(2);
                            if (coverage < lowCov) {
                                g2d.setColor(RED);
                            } else if (lowCov <= coverage && coverage <= highCov) {
                                g2d.setColor(AMBER);
                            } else if (coverage > highCov){
                                g2d.setColor(GREEN);
                            }

                        } else if (labelStyle.equals("chimQual")) {
                            Double chimQual = qry.getSites().get(site).get(4);
                            if (chimQual < lowQual) {
                                g2d.setColor(RED);
                            } else if (lowQual <= chimQual && chimQual <= highQual) {
                                g2d.setColor(AMBER);
                            } else if (chimQual > highQual){
                                g2d.setColor(GREEN);
                            }
                        }

                        int position = (int) ((qrySites.get(site) / scaleX) + qryRectScaled.getX());
                        g2d.drawLine(position, qryOffSetY, position, qryOffSetY + qryHeight);

                        g2d.setColor(BLACK);
                        // Draw alignment
                        if (match) {
                            if (confidenceView) {
                                double confidence = qry.getConfidence();
                                if (confidence < lowConf) {
                                    g2d.setStroke(DOTTED);
                                } else if (lowConf <= confidence && confidence <= highConf) {
                                    g2d.setStroke(DASHED);
                                } else {
                                    g2d.setStroke(defaultStroke);
                                }
                            }
                            for (int i : qryAlignments.get(site)) {
                                int refPositionX = (int) ((refSites.get(i) / scaleX) + refRectScaled.getX());
                                int refPositionY = (int) (refRectScaled.getY() + refRectScaled.getHeight());
                                g2d.drawLine(position, qryOffSetY, refPositionX, refPositionY);
                            }
                            g2d.setStroke(defaultStroke);
                        }
                    }

                    if (chosenQry.equals(qry.getID())) {
                        g2d.setStroke(new BasicStroke(3));
                        g2d.setColor(Color.ORANGE);
                        g2d.draw(qry.getRefViewRect());
                        g2d.setStroke(defaultStroke);
                    } else if (movedQry.equals(qry.getID())) {
                        g2d.setStroke(new BasicStroke(2));
                        g2d.setColor(Color.CYAN);
                        g2d.draw(qry.getRefViewRect());
                        g2d.setStroke(defaultStroke);
                    }
                }

                if (!position.isEmpty()) {

                    g2d.setStroke(g2d.getStroke());
                    g2d.setColor(Color.white);
                    int stringLen = g2d.getFontMetrics().stringWidth(position);
                    g2d.fillRect(mouseX, mouseY, stringLen + 16, 14);
                    g2d.setColor(Color.black);
                    g2d.drawRect(mouseX, mouseY, stringLen + 16, 14);
                    g2d.drawString(position, mouseX + 12, mouseY + 12);
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

    private void drawScaleBar(Graphics2D g2d, Rectangle2D refRect) {

        g2d.drawLine((int) refRect.getMinX(), (int) refRect.getMinY() - 40, (int) (refRect.getMinX() + refRect.getWidth()), (int) refRect.getMinY() - 40);
        int count = 0;
        int numScales = (int) refRect.getWidth() / 100;
//        double length = RawFileData.getRefContigs(chosenRef).getContigLen();
        double length = model.getSelectedRef().getLength();
        if (numScales != 0) {
            for (int i = 0; i < numScales + 1; i++) {
                g2d.drawLine((int) (refRect.getMinX() + (refRect.getWidth() / numScales) * i), (int) refRect.getMinY() - 50, (int) (refRect.getMinX() + (refRect.getWidth() / numScales) * i), (int) refRect.getMinY() - 40);
                g2d.drawString(String.format("%.2f", ((double) count) / 1000) + " kb", (int) (refRect.getMinX() + ((refRect.getWidth() / numScales) * i) - g2d.getFontMetrics().stringWidth(String.format("%.2f", ((double) count) / 1000) + " kb") / 2), (int) refRect.getMinY() - 55);
                count = (int) (count + length / numScales);
            }
        } else {
            g2d.drawLine((int) (refRect.getMinX()), (int) refRect.getMinY() - 50, (int) (refRect.getMinX()), (int) refRect.getMinY() - 40);
            g2d.drawString(String.format("%.2f", 0.0) + " kb", (int) (refRect.getMinX() - g2d.getFontMetrics().stringWidth(String.format("%.2f", 0.0) + " kb") / 2), (int) refRect.getMinY() - 55);
            g2d.drawLine((int) (refRect.getMinX() + refRect.getWidth()), (int) refRect.getMinY() - 50, (int) (refRect.getMinX() + refRect.getWidth()), (int) refRect.getMinY() - 40);
            g2d.drawString(String.format("%.2f", length / 1000) + " kb", (int) (refRect.getMinX() + refRect.getWidth() - g2d.getFontMetrics().stringWidth(String.format("%.2f", length / 1000) + " kb") / 2), (int) refRect.getMinY() - 55);

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

    class MyMouseAdapter extends MouseAdapter {

        private boolean pressed = false;
        private Point point;

        @Override
        public void mousePressed(MouseEvent e) {
            if (e.getButton() != MouseEvent.BUTTON1) {
                return;
            }
            position = "";
            pressed = true;
            draggedShape = null;
            this.point = e.getPoint();
            Rectangle2D rect;
            if (!"".equals(chosenRef)) {
                // loop through draggable queries and set dragged shape
                System.out.println(model.getSelectedRef().getRefID());
                for (Query qry : model.getSelectedRef().getQueries()) {

                    rect = qry.getRefViewRect();
                    System.out.println(qry.getID());
                    System.out.println(rect);
                    if (rect != null && rect.contains(e.getPoint())) {
                        setMovedQry(qry.getID());
                        draggedShape = qry;
                    }
                }
            }
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (pressed) {
                setCursor(new Cursor(Cursor.MOVE_CURSOR));
                int deltaX = (e.getX() - point.x);
                int deltaY = (e.getY() - point.y);

                if (draggedShape != null) {
                    for (Query qry : model.getSelectedRef().getQueries()) {
                        if (qry.equals(draggedShape)) {
                            qry.setRefViewOffsetX(deltaX);
                            qry.setRefViewOffsetY(deltaY);
                        }
                    }
                } else if (!chosenRef.isEmpty()){
                    windowOffX += deltaX / (scaleMultiplier * 0.9);
                    windowOffY += deltaY;
                }

                point = e.getPoint();
                repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pressed = false;
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
