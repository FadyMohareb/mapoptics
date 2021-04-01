package UserInterface;

import Algorithms.CalculateOverlaps;
import DataTypes.*;
import Datasets.Default.RawFileData;
import Datasets.Default.RefViewData;
import Datasets.UserEdited.UserRefData;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/*
 * @author Josie
 */
public class ReferenceView extends JPanel {

    private static String draggedShapeId = "";
    private static Query draggedShape = null;
    private static String chosenRef = "";
    private static String refDataset = "";
    private static String qryDataset = "";
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
    private double scaleMultiplier = 0.9;
    private int windowOffX = 0;
    private int windowOffY = 0;
    private final MapOpticsModel model;

    private static final Color LIGHT_GREY = new Color(244, 244, 244);
    private static final Color GREY = new Color(192, 192, 192);
    private static final Color DARK_GREY = new Color(80, 80, 80);
    private static final Color BLACK = new Color(30, 30, 30);
    private static final Color GREEN = new Color(97, 204, 10);

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

    public static void setRefDataset(String refDataset) {
        ReferenceView.refDataset = refDataset;
    }

    public static void setQryDataset(String qryDataset) {
        ReferenceView.qryDataset = qryDataset;
    }

    public ReferenceView(MapOpticsModel model) {

        this.model = model;
        MyMouseAdapter myMouseAdapter = new MyMouseAdapter();
        addMouseListener(myMouseAdapter);
        addMouseMotionListener(myMouseAdapter);
        initComponents();
    }

    public void zoomIn() {
        if (scaleMultiplier < 50) {
            scaleMultiplier *= 1.2;
            windowOffX += scaleMultiplier;
//            windowOffX = (int) ((windowOffX + (this.getWidth() / 2)) * scaleMultiplier);
        }
    }

    public void zoomOut() {
        if (scaleMultiplier > 0.05) {
            scaleMultiplier /= 1.2;
//            windowOffX -= this.getWidth() / 2;
        }
    }

    public void reCenter() {
        scaleMultiplier = 0.9;
        windowOffX = 0;
        windowOffY = 0;
    }


    public void zoomPanel(double horZoom, double vertZoom) {
        AffineTransform at2 = AffineTransform.getScaleInstance(horZoom, vertZoom);
        // move everything
        for (String refId : UserRefData.getReferences().keySet()) {
            // move and resize reference
            Rectangle2D rect;
            RefContig ref = UserRefData.getReferences(refId);
            ref.setConnections(ref.getConnections());
            rect = at2.createTransformedShape(ref.getRectangle()).getBounds2D();
            ref.setRectangle(rect);
            Rectangle2D[] labels = new Rectangle2D[ref.getLabels().length];
            for (int i = 0; i < ref.getLabels().length; i++) {
                rect = at2.createTransformedShape(ref.getLabels()[i]).getBounds2D();
                labels[i] = resize(rect);

            }
            ref.setLabels(labels);
            // move all queries
            for (String qryId : ref.getConnections()) {
                QryContig qry = UserRefData.getQueries(refId + "-" + qryId);
                qry.setQryAlignEnd(qry.getQryAlignEnd() * horZoom);
                qry.setQryAlignStart(qry.getQryAlignStart() * horZoom);
                qry.setRefAlignEnd(qry.getRefAlignEnd() * horZoom);
                qry.setRefAlignStart(qry.getRefAlignStart() * horZoom);
                qry.setRectangle(at2.createTransformedShape(qry.getRectangle()).getBounds2D());

                labels = new Rectangle2D[qry.getLabels().length];
                for (int i = 0; i < qry.getLabels().length; i++) {
                    rect = at2.createTransformedShape(qry.getLabels()[i]).getBounds2D();
                    labels[i] = resize(rect);

                }
                qry.setLabels(labels);
            }
        }

        // do the same for default data
        for (String refId : RefViewData.getReferences().keySet()) {
            // move and resize reference
            Rectangle2D rect;
            RefContig ref = RefViewData.getReferences(refId);
            ref.setConnections(ref.getConnections());
            rect = at2.createTransformedShape(ref.getRectangle()).getBounds2D();
            ref.setRectangle(rect);
            Rectangle2D[] labels = new Rectangle2D[ref.getLabels().length];
            for (int i = 0; i < ref.getLabels().length; i++) {
                rect = at2.createTransformedShape(ref.getLabels()[i]).getBounds2D();
                labels[i] = resize(rect);

            }
            ref.setLabels(labels);
            // move all queries
            for (String qryId : ref.getConnections()) {
                QryContig qry = RefViewData.getQueries(refId + "-" + qryId);
                qry.setQryAlignEnd(qry.getQryAlignEnd() * horZoom);
                qry.setQryAlignStart(qry.getQryAlignStart() * horZoom);
                qry.setRefAlignEnd(qry.getRefAlignEnd() * horZoom);
                qry.setRefAlignStart(qry.getRefAlignStart() * horZoom);
                qry.setRectangle(at2.createTransformedShape(qry.getRectangle()).getBounds2D());

                labels = new Rectangle2D[qry.getLabels().length];
                for (int i = 0; i < qry.getLabels().length; i++) {
                    rect = at2.createTransformedShape(qry.getLabels()[i]).getBounds2D();
                    labels[i] = resize(rect);

                }
                qry.setLabels(labels);
            }
        }
    }

    public static void zoom(double zoom, double panelWidth) {
        AffineTransform at1 = AffineTransform.getScaleInstance(zoom, 1);
        // Get the shift value
        double newWidth = panelWidth * zoom;
        double difWidth = (panelWidth - newWidth) / 2;

        AffineTransform at2 = AffineTransform.getTranslateInstance(difWidth, 0);

        // move everything
        // move and resize reference
        Rectangle2D rect;
        RefContig ref = UserRefData.getReferences(chosenRef);
        rect = at1.createTransformedShape(ref.getRectangle()).getBounds2D();
        rect = at2.createTransformedShape(rect).getBounds2D();
        ref.setRectangle(rect);
        Rectangle2D[] labels = new Rectangle2D[ref.getLabels().length];
        for (int i = 0; i < ref.getLabels().length; i++) {
            rect = at1.createTransformedShape(ref.getLabels()[i]).getBounds2D();
            rect = at2.createTransformedShape(rect).getBounds2D();
            labels[i] = resize(rect);
        }
        ref.setLabels(labels);
        // move and resize all queries
        for (String qryId : ref.getConnections()) {
            QryContig qry = UserRefData.getQueries(chosenRef + "-" + qryId);
            qry.setQryAlignEnd(qry.getQryAlignEnd() * zoom);
            qry.setQryAlignStart(qry.getQryAlignStart() * zoom);
            qry.setRefAlignEnd(qry.getRefAlignEnd() * zoom);
            qry.setRefAlignStart(qry.getRefAlignStart() * zoom);
            rect = at1.createTransformedShape(qry.getRectangle()).getBounds2D();
            rect = at2.createTransformedShape(rect).getBounds2D();
            qry.setRectangle(rect.getBounds2D());

            labels = new Rectangle2D[qry.getLabels().length];
            for (int i = 0; i < qry.getLabels().length; i++) {
                rect = at1.createTransformedShape(qry.getLabels()[i]).getBounds2D();
                rect = at2.createTransformedShape(rect).getBounds2D();
                labels[i] = resize(rect);
            }
            qry.setLabels(labels);
        }
    }

    private static Rectangle2D resize(Rectangle2D rect) {
        rect.setRect(rect.getMinX(), rect.getMinY(), 1, rect.getHeight());
        return rect;
    }

    // METHODS TO DO WITH REPAINTING THE PAINT COMPONENT

    @Override
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

                double scaleX = model.getRectangleTotalWidth() / (this.getWidth() * scaleMultiplier);
                double scaleY = model.getRectangleTotalHeight() / ((this.getHeight() - yOff) * 0.7);
                Reference ref = model.getSelectedRef();
                Map<Integer, Double> refSites = ref.getSites();
                Set<Integer> refAlignments = ref.getAlignmentSites();


                // Draw reference rectangle and sites
                Rectangle2D refRectScaled;
                Rectangle2D refRectRaw = ref.getRectangle();
                refRectScaled = new Rectangle2D.Double(
                        (refRectRaw.getX() / scaleX) + (this.getWidth() / 20.0) + windowOffX,
                        (refRectRaw.getY() / scaleY) + (this.getHeight() / 6.66) + yOff + windowOffY,
                        refRectRaw.getWidth() / scaleX,
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

                for (int site : refSites.keySet()) {
                    if (refAlignments.contains(site)) {
                        g2d.setColor(GREEN);
                    } else {
                        g2d.setColor(BLACK);
                    }

                    int position = (int) ((refSites.get(site) / scaleX) + refRectScaled.getX());
                    g2d.drawLine(position, refOffSetY, position, refOffSetY + refHeight);
                }

                drawScaleBar(g2d, refRectScaled);

                // For each query, draw rectangle, sites and alignments
                for (Query qry : ref.getQueries()) {
                    Rectangle2D qryRectScaled;
                    Rectangle2D qryRectRaw = qry.getRectangle();
                    qryRectScaled = new Rectangle2D.Double(
                            (qryRectRaw.getX() / scaleX) + (this.getWidth() / 20.0) + windowOffX + qry.getRefViewOffsetX(),
                            (qryRectRaw.getY() / scaleY) + (this.getHeight() / 6.66) + yOff + windowOffY + qry.getRefViewOffsetY(),
                            qryRectRaw.getWidth() / scaleX,
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
                            g2d.setColor(GREEN);
                        } else {
                            g2d.setColor(BLACK);
                        }

                        int position = (int) ((qrySites.get(site) / scaleX) + qryRectScaled.getX());
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
//    @Override
//    public void paintComponent(Graphics g) {
//        super.paintComponent(g);
//        this.setBackground(Color.white);
//        Graphics2D g2d = (Graphics2D) g;
//        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
//        Stroke defaultStroke = g2d.getStroke();
//
//        // draw alignment of single reference and all contigs
//        try {
//
//            if (!chosenRef.equals("")) {
//
//                Font defaultFont = g2d.getFont();
//                Font fontB = new Font("Tahoma", Font.BOLD, 12);
//                g2d.setFont(fontB);
//                g2d.drawString("Reference Dataset:  ", 20, 20);
//                g2d.drawString("Query Dataset:  ", 20, 40);
//                int refStringLen = g2d.getFontMetrics().stringWidth("Reference Dataset:  ");
//                int qryStringLen = g2d.getFontMetrics().stringWidth("Query Dataset:  ");
//                g2d.setFont(defaultFont);
//                g2d.drawString(refDataset, refStringLen + 20, 20);
//                g2d.drawString(qryDataset, qryStringLen + 20, 40);
//                //String refqryId = chosenRef + "-" + chosenQry;
//
//                RefContig ref = UserRefData.getReferences(chosenRef);
//
//                // draw reference contig
//                Rectangle2D refRect = ref.getRectangle();
//                drawContig(g2d, refRect, chosenRef, true);
//
//                // draw scalebar
//                drawScaleBar(g2d, refRect);
//
//                if ("coverage".equals(labelStyle)) {
//                    // draw labels coloured by coverage values
//                    LabelInfo[] labelInfo = RawFileData.getRefContigs(chosenRef).getLabelInfo();
//                    drawCoverageLabels(g2d, labelInfo, ref.getLabels());
//                }
//
//                if ("chimQual".equals(labelStyle)) {
//                    // draw labels coloured by chimeric quality values
//                    LabelInfo[] labelInfo = RawFileData.getRefContigs(chosenRef).getLabelInfo();
//                    drawQualityLabels(g2d, labelInfo, ref.getLabels());
//                }
//
//                QryContig qry;
//                Rectangle2D qryRect;
//
//                // loop through rest of query contigs and draw one at a time
//                for (int i = 0; i < ref.getConnections().length; i++) {
//                    String[] connectingContigs = ref.getConnections();
//                    String qryId = connectingContigs[i];
//
//                    qry = UserRefData.getQueries(chosenRef + "-" + qryId);
//
//                    // draw query contigs
//                    qryRect = qry.getRectangle();
//                    drawContig(g2d, qryRect, qryId, false);
//
//                    if ("coverage".equals(labelStyle)) {
//                        // draw all labels relative to confidence scores
//                        LabelInfo[] labelInfo = RawFileData.getQryContigs(qryId).getLabelInfo();
//                        drawCoverageLabels(g2d, labelInfo, qry.getLabels());
//                    }
//
//                    if ("chimQual".equals(labelStyle)) {
//                        // draw all labels coloured by label channel
//                        LabelInfo[] labelInfo = RawFileData.getQryContigs(qryId).getLabelInfo();
//                        drawQualityLabels(g2d, labelInfo, qry.getLabels());
//                    }
//
//                    if ("chimQual".equals(labelStyle) || "coverage".equals(labelStyle)) {
//                        // draw all connections
//                        int refIndex;
//                        for (int j = 0; j < qry.getLabels().length; j++) {
//                            for (String[] alignment : qry.getAlignments()) {
//                                if (Integer.parseInt(alignment[1]) == (j + 1)) {
//                                    refIndex = Integer.parseInt(alignment[0]) - 1;
//                                    if (confidenceView) {
//                                        drawConfidence(g2d, qryId, defaultStroke);
//
//                                    } else {
//                                        g2d.setStroke(defaultStroke);
//                                        g2d.setColor(Color.black);
//                                    }
//                                    g2d.drawLine((int) ref.getLabels()[refIndex].getCenterX(), (int) refRect.getMinY() + (int) refRect.getHeight(), (int) qry.getLabels()[j].getCenterX(), (int) qryRect.getMinY());
//                                    g2d.setStroke(defaultStroke);
//                                }
//                            }
//                        }
//                    }
//
//                    // draw labels in style set
//                    if ("match".equals(labelStyle)) {
//                        int refIndex;
//                        for (int j = 0; j < qry.getLabels().length; j++) {
//                            // draw the standard label
//                            boolean qrymatch = false;
//                            for (String[] alignment : qry.getAlignments()) {
//                                if (Integer.parseInt(alignment[1]) == (j + 1)) {
//                                    qrymatch = true;
//                                    // draw line linking the two
//                                    refIndex = Integer.parseInt(alignment[0]) - 1;
//                                    if (confidenceView) {
//                                        drawConfidence(g2d, qryId, defaultStroke);
//                                    } else {
//                                        g2d.setStroke(defaultStroke);
//                                        g2d.setColor(Color.black);
//                                    }
//                                    g2d.drawLine((int) ref.getLabels()[refIndex].getCenterX(), (int) refRect.getMinY() + (int) refRect.getHeight(), (int) qry.getLabels()[j].getCenterX(), (int) qryRect.getMinY());
//                                    // draw labels in green
//                                    g2d.setStroke(defaultStroke);
//                                    g2d.setColor(new Color(97, 204, 10));
//                                    g2d.fill(qry.getLabels()[j]);
//                                }
//                            }
//                            if (!qrymatch) {
//                                g2d.setColor(Color.black);
//                                g2d.fill(qry.getLabels()[j]);
//                            }
//                        }
//                    }
//                }
//
//                if ("match".equals(labelStyle)) {
//                    // loop through reference labels
//                    for (int i = 0; i < ref.getLabels().length - 1; i++) {
//                        boolean refmatch = false;
//                        // loop through all matching queries
//                        for (String qryId : ref.getConnections()) {
//                            String key = chosenRef + "-" + qryId;
//                            for (String[] alignment : UserRefData.getQueries(key).getAlignments()) {
//                                if (Integer.parseInt(alignment[0]) == (i + 1)) {
//                                    refmatch = true;
//                                    // draw labels in green
//                                    g2d.setColor(new Color(97, 204, 10));
//                                    g2d.fill(ref.getLabels()[i]);
//                                }
//                            }
//                        }
//                        if (!refmatch) {
//                            g2d.setColor(Color.black);
//                            g2d.fill(ref.getLabels()[i]);
//                        }
//                    }
//                }
//
//                if (!"".equals(chosenQry)) {
//                    g2d.setColor(Color.orange);
//                    g2d.setStroke(new BasicStroke(3));
//                    qry = UserRefData.getQueries(chosenRef + "-" + chosenQry);
//                    QryContig qry2 = RawFileData.getQueries(chosenRef + "-" + chosenQry);
//                    if (qry != null) {
//                        g2d.draw(qry.getRectangle());
//                    } else if (qry2 != null) {
//                        Font fontI = new Font("Tahoma", Font.ITALIC, 12);
//                        g2d.setFont(fontI);
//                        g2d.setColor(new Color(204, 0, 0));
//                        g2d.drawString("Chosen contig was deleted", 30, 70);
//                    }
//                }
//
//                if (overlapView) {
//                    drawOverlaps(g2d, ref, UserRefData.getQueries());
//                }
//
//                if (!"".equals(position)) {
//
//                    g2d.setStroke(defaultStroke);
//                    g2d.setColor(Color.white);
//                    int stringLen = g2d.getFontMetrics().stringWidth(position);
//                    g2d.fillRect(mouseX, mouseY, stringLen + 16, 14);
//                    g2d.setColor(Color.black);
//                    g2d.drawRect(mouseX, mouseY, stringLen + 16, 14);
//                    g2d.drawString(position, mouseX + 12, mouseY + 12);
//                }
//
//            } else {
//                Font font = new Font("Tahoma", Font.ITALIC, 12);
//                g2d.setFont(font);
//                g2d.drawString("Choose a reference contig from SUMMARY VIEW", this.getWidth() / 2 - 115, this.getHeight() / 2);
//            }
//
//        } catch (Exception e) {
//            super.paintComponent(g);
//            this.setBackground(Color.white);
//            Font font = new Font("Tahoma", Font.ITALIC, 12);
//            g2d.setColor(Color.red);
//            g2d.setFont(font);
//            g2d.drawString("ERROR DRAWING ALIGNMENT", this.getWidth() / 2 - 115, this.getHeight() / 2);
//        }
//    }

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

    private void drawContig(Graphics2D g2d, Rectangle2D rect, String id, boolean ref) {
        g2d.setColor(new Color(244, 244, 244));
        g2d.fill(rect);
        g2d.setColor(Color.lightGray);
        g2d.draw(rect);
        g2d.setColor(new Color(80, 80, 80));
        if (ref) {
            g2d.drawString("ID: " + id, (int) rect.getMaxX() - g2d.getFontMetrics().stringWidth("ID: " + id), (int) rect.getMinY() - 2);
        } else {
            g2d.drawString("ID: " + id, (int) rect.getMaxX() - g2d.getFontMetrics().stringWidth("ID: " + id), (int) rect.getMaxY() + 14);
        }
    }

    private void drawCoverageLabels(Graphics2D g2d, LabelInfo[] labelInfo, Rectangle2D[] labels) {
        for (int i = 0; i < labelInfo.length - 1; i++) {
            double coverage = Double.parseDouble(labelInfo[i].getCoverage());
            Rectangle2D label = labels[i];
            if (coverage < lowCov) {
                g2d.setColor(new Color(204, 0, 0));
                g2d.fill(label);
            } else if (coverage >= lowCov && coverage < highCov) {
                g2d.setColor(new Color(255, 204, 0));
                g2d.fill(label);
            } else if (coverage > highCov) {
                g2d.setColor(new Color(0, 153, 0));
                g2d.fill(label);
            }
        }
    }

    private void drawQualityLabels(Graphics2D g2d, LabelInfo[] labelInfo, Rectangle2D[] labels) {
        for (int i = 0; i < labelInfo.length - 1; i++) {
            Rectangle2D label = labels[i];
            if (labelInfo[i].getChimQuality() != null) {
                double chimQuality = Double.parseDouble(labelInfo[i].getChimQuality());
                if (chimQuality < lowQual) {
                    g2d.setColor(new Color(204, 0, 0));
                    g2d.fill(label);
                } else if (chimQuality >= lowQual && chimQuality < highQual) {
                    g2d.setColor(new Color(255, 204, 0));
                    g2d.fill(label);
                } else {
                    g2d.setColor(new Color(0, 153, 0));
                    g2d.fill(label);
                }
            } else {
                g2d.setColor(Color.black);
                g2d.fill(label);
            }

        }
    }

    private void drawConfidence(Graphics2D g2d, String qryId, Stroke defaultStroke) {
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6, 2}, 2);
        Stroke dotted = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{1, 2}, 2);
        double confidence = Double.parseDouble(RawFileData.getAlignmentInfo(chosenRef + "-" + qryId).getConfidence());
        g2d.setColor(Color.black);
        if (confidence < lowConf) {
            g2d.setStroke(dotted);
        } else if (confidence >= lowConf && confidence < highConf) {
            g2d.setStroke(dashed);
        } else {
            g2d.setStroke(defaultStroke);
        }
    }

    private void drawOverlaps(Graphics2D g2d, RefContig ref, LinkedHashMap<String, QryContig> queries) {
        LinkedHashMap<String, Rectangle2D[]> overlapRegions = CalculateOverlaps.calculateRefOverlap(chosenRef, ref, queries);
        for (String refqryIds : overlapRegions.keySet()) {
            g2d.setColor(new Color(255, 255, 0, 80));
            Rectangle2D[] regions = overlapRegions.get(refqryIds);

            for (Rectangle2D region : regions) {
                g2d.fill(region);
            }

            for (int i = 1; i < 3; i++) {
                QryContig qry = UserRefData.getQueries(refqryIds.split("-")[0] + "-" + refqryIds.split("-")[i]);
                Path2D.Double alignment = new Path2D.Double();
                alignment.moveTo(regions[0].getMinX(), regions[0].getMaxY());
                alignment.lineTo(regions[0].getMaxX(), regions[0].getMaxY());
                if (qry.getOrientation().equals("+")) {
                    alignment.lineTo(regions[i].getMaxX(), regions[i].getMinY());
                    alignment.lineTo(regions[i].getMinX(), regions[i].getMinY());
                } else {
                    alignment.lineTo(regions[i].getMinX(), regions[i].getMinY());
                    alignment.lineTo(regions[i].getMaxX(), regions[i].getMinY());
                }
                alignment.closePath();
                g2d.fill(alignment);
            }
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
            draggedShapeId = "";
            draggedShape = null;
            this.point = e.getPoint();
            Rectangle2D rect;
            if (!"".equals(chosenRef)) {
                RefContig ref = UserRefData.getReferences().get(chosenRef);
                // loop through draggable queries and set dragged shape
//                for (String qryId : ref.getConnections()) {
                for (Query qry : model.getSelectedRef().getQueries()) {
//                    rect = UserRefData.getQueries(chosenRef + "-" + qryId).getRectangle();
                    rect = qry.getRefViewRect();
                    if (rect.contains(e.getPoint())) {
//                        draggedShapeId = chosenRef + "-" + qryId;
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
//                System.out.println(deltaX +","+deltaY);
                AffineTransform at = AffineTransform.getTranslateInstance(deltaX, deltaY);

                if (draggedShape != null) {

//                    Query qry = draggedShape;

//                    Rectangle2D[] draggedShapes = new Rectangle2D[qry.getLabels().length];
//                    for (int i = 0; i < qry.getLabels().length; i++) {
//                        draggedShapes[i] = at.createTransformedShape(qry.getLabels()[i]).getBounds2D();
//                    }
//                    qry.setLabels(draggedShapes);
                    for (Query qry : model.getSelectedRef().getQueries()) {
                        if (qry.equals(draggedShape)) {
//                            Rectangle2D oldRect = qry.getRefViewRect();
//                            Rectangle2D newRect = new Rectangle2D.Double(oldRect.getX() + deltaX, oldRect.getY() + deltaY, oldRect.getWidth(), oldRect.getHeight());
//                            qry.setRefViewRect(newRect);
                            qry.setRefViewOffsetX(deltaX);
                            qry.setRefViewOffsetY(deltaY);
                        }
                    }

//                    qry.setAlignments(UserRefData.getQueries(draggedShapeId).getAlignments());

//                    UserRefData.getQueries().put(draggedShapeId, qry);
                } else if (!chosenRef.isEmpty()){
                    windowOffX += deltaX;
                    windowOffY += deltaY;
                    // move everything
                    // move reference
                    //Reference ref = model.getSelectedRef();
                    //ref.setRefViewRect(at.createTransformedShape(ref.getRefViewRect()).getBounds2D());
//                    Rectangle2D[] labels = new Rectangle2D[ref.getLabels().length];
//                    for (int i = 0; i < ref.getLabels().length; i++) {
//                        labels[i] = at.createTransformedShape(ref.getLabels()[i]).getBounds2D();
//                    }
//                    ref.setLabels(labels);
                    // move all queries
                    //for (Query qry : ref.getQueries()) {
                        //qry.setRefViewRect(at.createTransformedShape(qry.getRefViewRect()).getBounds2D());
//                        labels = new Rectangle2D[qry.getLabels().length];
//                        for (int i = 0; i < qry.getLabels().length; i++) {
//                            labels[i] = at.createTransformedShape(qry.getLabels()[i]).getBounds2D();
//                        }
//                        qry.setLabels(labels);
                    //}
                }

                point = e.getPoint();
                repaint();
            }
        }

//        @Override
//        public void mouseDragged(MouseEvent e) {
//            if (pressed) {
//                setCursor(new Cursor(Cursor.MOVE_CURSOR));
//                int deltaX = e.getX() - point.x;
//                int deltaY = e.getY() - point.y;
//                AffineTransform at = AffineTransform.getTranslateInstance(deltaX, deltaY);
//
//                if (!draggedShapeId.equals("")) {
//
//                    QryContig qry = UserRefData.getQueries(draggedShapeId);
//
//                    Rectangle2D[] draggedShapes = new Rectangle2D[qry.getLabels().length];
//                    for (int i = 0; i < qry.getLabels().length; i++) {
//                        draggedShapes[i] = at.createTransformedShape(qry.getLabels()[i]).getBounds2D();
//                    }
//                    qry.setLabels(draggedShapes);
//                    qry.setRectangle(at.createTransformedShape(qry.getRectangle()).getBounds2D());
//                    qry.setAlignments(UserRefData.getQueries(draggedShapeId).getAlignments());
//
//                    UserRefData.getQueries().put(draggedShapeId, qry);
//                } else if (!chosenRef.isEmpty()){
//                    // move everything
//                    // move reference
//                    RefContig ref = UserRefData.getReferences(chosenRef);
//                    ref.setRectangle(at.createTransformedShape(ref.getRectangle()).getBounds2D());
//                    Rectangle2D[] labels = new Rectangle2D[ref.getLabels().length];
//                    for (int i = 0; i < ref.getLabels().length; i++) {
//                        labels[i] = at.createTransformedShape(ref.getLabels()[i]).getBounds2D();
//                    }
//                    ref.setLabels(labels);
//                    // move all queries
//                    for (String qryId : ref.getConnections()) {
//                        QryContig qry = UserRefData.getQueries(chosenRef + "-" + qryId);
//                        qry.setRectangle(at.createTransformedShape(qry.getRectangle()).getBounds2D());
//                        labels = new Rectangle2D[qry.getLabels().length];
//                        for (int i = 0; i < qry.getLabels().length; i++) {
//                            labels[i] = at.createTransformedShape(qry.getLabels()[i]).getBounds2D();
//                        }
//                        qry.setLabels(labels);
//                    }
//                }
//
//                point = e.getPoint();
//                repaint();
//            }
//        }

        @Override
        public void mouseReleased(MouseEvent e) {
            pressed = false;
            draggedShapeId = "";
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }
    }
}
