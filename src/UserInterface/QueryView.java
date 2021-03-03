package UserInterface;

import Algorithms.SortSequence;
import DataTypes.LabelInfo;
import DataTypes.QryContig;
import DataTypes.RefContig;
import Datasets.Default.RawFileData;
import Datasets.UserEdited.SearchRegionData;
import Datasets.UserEdited.UserQryData;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;

/*
 * @author Josie
 */
public class QueryView extends JPanel {

    /**
     * Creates new form SingleAlignment
     */
    public QueryView() {
        initComponents();
    }

    private static String chosenRef = "";
    private static String chosenQry = "";
    private static String chosenLabel = "";
    private static String labelStyle = "match";
    private static boolean confidenceView = false;
    private static boolean regionView = false;
    private static boolean refSequenceView = false;
    private static boolean qrySequenceView = false;
    private static String position = "";
    private static int mouseX = 0;
    private static int mouseY = 0;

    public static void setChosenRef(String chosenRef) {
        QueryView.chosenRef = chosenRef;
    }

    public static void setChosenQry(String chosenQry) {
        QueryView.chosenQry = chosenQry;
    }

    public static void setChosenLabel(String chosenLabel) {
        QueryView.chosenLabel = chosenLabel;
    }

    public static String getChosenQry() {
        return chosenQry;
    }

    public static String getChosenRef() {
        return chosenRef;
    }

    public static void setStyle(String style) {
        QueryView.labelStyle = style;
    }

    public static void setConfidenceView(boolean confidenceView) {
        QueryView.confidenceView = confidenceView;
    }

    public static void setPosition(String position) {
        QueryView.position = position;
    }

    public static void setMouseX(int mouseX) {
        QueryView.mouseX = mouseX;
    }

    public static void setMouseY(int mouseY) {
        QueryView.mouseY = mouseY;
    }

    public static void setRegionView(boolean regionView) {
        QueryView.regionView = regionView;
    }

    public static void setRefSequenceView(boolean refSequenceView) {
        QueryView.refSequenceView = refSequenceView;
    }

    public static void setQrySequenceView(boolean qrySequenceView) {
        QueryView.qrySequenceView = qrySequenceView;
    }

    public static boolean isRegionView() {
        return regionView;
    }

    /*
    This method is currently unused. Commented out in case it becomes useful.
     */
//    public void zoomPanel(double horZoom, double vertZoom) {
//        AffineTransform at = AffineTransform.getScaleInstance(horZoom, vertZoom);
//        // move everything
//        // move and resize user data
//        for (String refqryId : UserQryData.getQueries().keySet()) {
//            Rectangle2D rect;
//            QryContig qry = UserQryData.getQueries(refqryId);
//            rect = at.createTransformedShape(qry.getRectangle()).getBounds2D();
//            qry.setRectangle(rect);
//
//            Rectangle2D[] labels = new Rectangle2D[qry.getLabels().length];
//            for (int i = 0; i < qry.getLabels().length; i++) {
//                rect = at.createTransformedShape(qry.getLabels()[i]).getBounds2D();
//                labels[i] = resize(rect);
//
//            }
//            qry.setLabels(labels);
//        }
//
//        for (String refqryId : UserQryData.getReferences().keySet()) {
//            Rectangle2D rect;
//            RefContig ref = UserQryData.getReferences(refqryId);
//            rect = at.createTransformedShape(ref.getRectangle()).getBounds2D();
//            ref.setRectangle(rect);
//            Rectangle2D[] labels = new Rectangle2D[ref.getLabels().length];
//            for (int i = 0; i < ref.getLabels().length; i++) {
//                rect = at.createTransformedShape(ref.getLabels()[i]).getBounds2D();
//                labels[i] = resize(rect);
//
//            }
//            ref.setLabels(labels);
//        }
//
//        // move and resize default data
//        for (String refqryId : QueryViewData.getQueries().keySet()) {
//            Rectangle2D rect;
//            QryContig qry = QueryViewData.getQueries(refqryId);
//            rect = at.createTransformedShape(qry.getRectangle()).getBounds2D();
//            qry.setRectangle(rect);
//            Rectangle2D[] labels = new Rectangle2D[qry.getLabels().length];
//            for (int i = 0; i < qry.getLabels().length; i++) {
//                rect = at.createTransformedShape(qry.getLabels()[i]).getBounds2D();
//                labels[i] = resize(rect);
//            }
//            qry.setLabels(labels);
//        }
//        for (String refqryId : QueryViewData.getReferences().keySet()) {
//            Rectangle2D rect;
//            RefContig ref = QueryViewData.getReferences(refqryId);
//            rect = at.createTransformedShape(ref.getRectangle()).getBounds2D();
//            ref.setRectangle(rect);
//            Rectangle2D[] labels = new Rectangle2D[ref.getLabels().length];
//            for (int i = 0; i < ref.getLabels().length; i++) {
//                rect = at.createTransformedShape(ref.getLabels()[i]).getBounds2D();
//                labels[i] = resize(rect);
//            }
//            ref.setLabels(labels);
//        }
//        // move and resize search region data if present
//        Rectangle2D rect;
//        QryContig qry = SearchRegionData.getQry();
//        if (qry.getRectangle() != null) {
//            rect = at.createTransformedShape(qry.getRectangle()).getBounds2D();
//            qry.setRectangle(rect);
//            Rectangle2D[] labels = new Rectangle2D[qry.getLabels().length];
//            for (int i = 0; i < qry.getLabels().length; i++) {
//                rect = at.createTransformedShape(qry.getLabels()[i]).getBounds2D();
//                labels[i] = resize(rect);
//            }
//            qry.setLabels(labels);
//        }
//        RefContig ref = SearchRegionData.getRef();
//        if (ref.getRectangle() != null) {
//            rect = at.createTransformedShape(ref.getRectangle()).getBounds2D();
//            ref.setRectangle(rect);
//            Rectangle2D[] labels = new Rectangle2D[ref.getLabels().length];
//            for (int i = 0; i < ref.getLabels().length; i++) {
//                rect = at.createTransformedShape(ref.getLabels()[i]).getBounds2D();
//                labels[i] = resize(rect);
//            }
//            ref.setLabels(labels);
//        }
//    }
//
//    private static Rectangle2D resize(Rectangle2D rect) {
//        rect.setRect(rect.getMinX(), rect.getMinY(), 1, rect.getHeight());
//        return rect;
//    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke defaultStroke = g2d.getStroke();

        // draw alignment of single reference and contig
        try {
            if (!"".equals(chosenQry)) {

                Font defaultFont = g2d.getFont();
                Font fontB = new Font("Tahoma", Font.BOLD, 12);
                g2d.setFont(fontB);
                g2d.drawString("Reference ID:  ", 20, 20);
                g2d.drawString("Query ID:  ", 20, 40);
                int refStringLen = g2d.getFontMetrics().stringWidth("Reference ID:  ");
                int qryStringLen = g2d.getFontMetrics().stringWidth("Query ID:  ");
                g2d.setFont(defaultFont);
                g2d.drawString(chosenRef, refStringLen + 20, 20);
                g2d.drawString(chosenQry, qryStringLen + 20, 40);
                String refqryId = chosenRef.concat("-").concat(chosenQry);

                RefContig ref;
                QryContig qry;

                if (regionView) {
                    // draw only region searched
                    ref = SearchRegionData.getRef();
                    qry = SearchRegionData.getQry();
                } else {
                    // draw relative to query alignment
                    ref = UserQryData.getReferences(refqryId);
                    qry = UserQryData.getQueries(refqryId);
                }

                if (qry != null) {
                    Rectangle2D refRect = ref.getRectangle();
                    Rectangle2D qryRect = qry.getRectangle();

                    if (refSequenceView && !qrySequenceView) {
                        // draw contigs with gaps visible
                        drawSequence(g2d, ref.getRectangle(), ref.getSequence(), RawFileData.getRefContigs(chosenRef).getContigLen());
                        // draw query contig
                        drawContig(g2d, qryRect);

                    } else if (!refSequenceView && qrySequenceView) {
                        // draw ref contig
                        drawContig(g2d, refRect);
                        // draw contigs with gaps visible
                        drawSequence(g2d, qry.getRectangle(), qry.getSequence(), RawFileData.getQryContigs(chosenQry).getContigLen());

                    } else if (refSequenceView && qrySequenceView) {
                        // draw both contigs with gaps visible
                        drawSequence(g2d, ref.getRectangle(), ref.getSequence(), RawFileData.getRefContigs(chosenRef).getContigLen());
                        drawSequence(g2d, qry.getRectangle(), qry.getSequence(), RawFileData.getQryContigs(chosenQry).getContigLen());

                    } else {
                        // draw reference contig
                        drawContig(g2d, refRect);
                        // draw query contig
                        drawContig(g2d, qryRect);

                    }

                    // draw scalebars
                    drawScaleBar(g2d, refRect, true);
                    drawScaleBar(g2d, qryRect, false);

                    if (!chosenLabel.equals("")) {
                        // draw chosen label
                        LabelInfo labelInfo = RawFileData.getQryContigs(chosenQry).getLabelInfo()[Integer.parseInt(chosenLabel)];
                        Rectangle2D label = qry.getLabels()[Integer.parseInt(chosenLabel)];
                        boolean orientated = RawFileData.getQueries(chosenRef + "-" + chosenQry).getOrientation().equals("+");
                        drawChosenLabel(g2d, labelInfo, label, orientated);
                    }

                    if ("coverage".equals(labelStyle)) {
                        // draw labels coloured by coverage values
                        LabelInfo[] labelInfo = RawFileData.getRefContigs(chosenRef).getLabelInfo();
                        drawCoverageLabels(g2d, labelInfo, ref.getLabels());
                        labelInfo = RawFileData.getQryContigs(chosenQry).getLabelInfo();
                        drawCoverageLabels(g2d, labelInfo, qry.getLabels());
                    }

                    if ("chimQual".equals(labelStyle)) {
                        // draw labels coloured by chimeric quality values
                        LabelInfo[] labelInfo = RawFileData.getRefContigs(chosenRef).getLabelInfo();
                        drawQualityLabels(g2d, labelInfo, ref.getLabels());
                        labelInfo = RawFileData.getQryContigs(chosenQry).getLabelInfo();
                        drawQualityLabels(g2d, labelInfo, qry.getLabels());
                    }

                    if ("chimQual".equals(labelStyle) || "coverage".equals(labelStyle)) {
                        // draw all connections
                        int refIndex;
                        for (int j = 0; j < qry.getLabels().length; j++) {
                            for (String[] alignment : qry.getAlignments()) {
                                if (Integer.parseInt(alignment[1]) == (j + 1)) {
                                    refIndex = Integer.parseInt(alignment[0]) - 1;
                                    if (confidenceView) {
                                        drawConfidence(g2d, defaultStroke);
                                    } else {
                                        g2d.setStroke(defaultStroke);
                                        g2d.setColor(Color.black);
                                    }
                                    g2d.drawLine((int) ref.getLabels()[refIndex].getCenterX(), (int) refRect.getMinY() + (int) refRect.getHeight(), (int) qry.getLabels()[j].getCenterX(), (int) qryRect.getMinY());
                                    g2d.setStroke(defaultStroke);
                                }
                            }
                        }
                    }

                    // draw labels in style set 
                    if ("match".equals(labelStyle)) {
                        int refIndex;
                        for (int j = 0; j < qry.getLabels().length; j++) {
                            // draw the standard label
                            boolean qrymatch = false;
                            for (String[] alignment : qry.getAlignments()) {
                                if (Integer.parseInt(alignment[1]) == (j + 1)) {
                                    qrymatch = true;
                                    // draw line linking the two
                                    refIndex = Integer.parseInt(alignment[0]) - 1;
                                    if (confidenceView) {
                                        drawConfidence(g2d, defaultStroke);
                                    } else {
                                        g2d.setStroke(defaultStroke);
                                        g2d.setColor(Color.black);
                                    }
                                    g2d.drawLine((int) ref.getLabels()[refIndex].getCenterX(), (int) refRect.getMinY() + (int) refRect.getHeight(), (int) qry.getLabels()[j].getCenterX(), (int) qryRect.getMinY());
                                    // draw labels in green
                                    g2d.setStroke(defaultStroke);
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

                    if ("match".equals(labelStyle)) {
                        // loop through reference labels
                        for (int i = 0; i < ref.getLabels().length - 1; i++) {
                            boolean refmatch = false;
                            // loop through all matching queries
                            for (String[] alignment : UserQryData.getQueries(chosenRef + "-" + chosenQry).getAlignments()) {
                                if (Integer.parseInt(alignment[0]) == (i + 1)) {
                                    refmatch = true;
                                    // draw labels in green
                                    g2d.setColor(new Color(97, 204, 10));
                                    g2d.fill(ref.getLabels()[i]);

                                }
                            }
                            if (!refmatch) {
                                g2d.setColor(Color.black);
                                g2d.fill(ref.getLabels()[i]);
                            }

                        }
                    }
                } else {
                    Font font = new Font("Tahoma", Font.ITALIC, 12);
                    g2d.setFont(font);
                    g2d.drawString("No match between chosen query and reference", this.getWidth() / 2 - 115, this.getHeight() / 2 - 10);
                    g2d.drawString("(Query contig may have been deleted)", this.getWidth() / 2 - 100, this.getHeight() / 2 + 10);
                }
            } else {
                Font font = new Font("Tahoma", Font.ITALIC, 12);
                g2d.setFont(font);
                g2d.drawString("Choose a query contig from REFERENCE VIEW", this.getWidth() / 2 - 115, this.getHeight() / 2);
            }

            if (!"".equals(position)) {

                g2d.setColor(Color.white);
                int stringLen = g2d.getFontMetrics().stringWidth(position);
                g2d.fillRect(mouseX, mouseY, stringLen + 16, 14);
                g2d.setColor(Color.black);
                g2d.drawRect(mouseX, mouseY, stringLen + 16, 14);
                g2d.drawString(position, mouseX + 12, mouseY + 12);
            }

        } catch (Exception e) {
            super.paintComponent(g);
            this.setBackground(Color.white);
            Font font = new Font("Tahoma", Font.ITALIC, 12);
            g2d.setColor(Color.red);
            g2d.setFont(font);
            
            g2d.drawString("ERROR DRAWING ALIGNMENT".concat(e.toString()), this.getWidth() / 2 - 115, this.getHeight() / 2);
        }

    }

    private void drawScaleBar(Graphics2D g2d, Rectangle2D rect, boolean ref) {

        if (ref) {
            g2d.drawLine((int) rect.getMinX(), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() - this.getHeight() / 25);
            int count = 0;
            int numScales = (int) rect.getWidth() / 100;
            double length = RawFileData.getRefContigs(chosenRef).getContigLen();
            if (numScales != 0) {
                for (int i = 0; i < numScales + 1; i++) {
                    g2d.drawLine((int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMinY() - this.getHeight() / 20);
                    g2d.drawString(String.format("%.2f", ((double) count) / 1000) + " kb", (int) (rect.getMinX() + ((rect.getWidth() / numScales) * i) - g2d.getFontMetrics().stringWidth(String.format("%.2f", ((double) count) / 1000) + " kb") / 2), (int) rect.getMinY() - this.getHeight() / 20 - 2);
                    count = (int) (count + length / numScales);
                }
            } else {
                g2d.drawLine((int) (rect.getMinX()), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX()), (int) rect.getMinY() - this.getHeight() / 20);
                g2d.drawString(String.format("%.2f", 0.0) + " kb", (int) (rect.getMinX() - g2d.getFontMetrics().stringWidth(String.format("%.2f", 0.0) + " kb") / 2), (int) rect.getMinY() - this.getHeight() / 20 - 2);
                g2d.drawLine((int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() - this.getHeight() / 20);
                g2d.drawString(String.format("%.2f", length / 1000) + " kb", (int) (rect.getMinX() + rect.getWidth() - g2d.getFontMetrics().stringWidth(String.format("%.2f", length / 1000) + " kb") / 2), (int) rect.getMinY() - this.getHeight() / 20 - 2);

            }
        } else {
            g2d.drawLine((int) rect.getMinX(), (int) rect.getMaxY() + this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMaxY() + this.getHeight() / 25);
            int count = 0;
            int numScales = (int) rect.getWidth() / 100;
            double length = RawFileData.getQryContigs(chosenQry).getContigLen();
            if (numScales != 0) {
                for (int i = 0; i < numScales + 1; i++) {
                    g2d.drawLine((int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMaxY() + this.getHeight() / 20, (int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMaxY() + this.getHeight() / 25);
                    g2d.drawString(String.format("%.2f", ((double) count) / 1000) + " kb", (int) (rect.getMinX() + ((rect.getWidth() / numScales) * i) - g2d.getFontMetrics().stringWidth(String.format("%.2f", ((double) count) / 1000) + " kb") / 2), (int) rect.getMaxY() + this.getHeight() / 20 + 14);
                    count = (int) (count + length / numScales);
                }
            } else {
                g2d.drawLine((int) (rect.getMinX()), (int) rect.getMinY() + this.getHeight() / 25, (int) (rect.getMinX()), (int) rect.getMinY() + this.getHeight() / 20);
                g2d.drawString(String.format("%.2f", 0.0) + " kb", (int) (rect.getMinX() - g2d.getFontMetrics().stringWidth(String.format("%.2f", 0.0) + " kb") / 2), (int) rect.getMinY() + +this.getHeight() / 20 + 14);
                g2d.drawLine((int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() + this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() + this.getHeight() / 20);
                g2d.drawString(String.format("%.2f", length / 1000) + " kb", (int) (rect.getMinX() + rect.getWidth() - g2d.getFontMetrics().stringWidth(String.format("%.2f", length / 1000) + " kb") / 2), (int) rect.getMinY() + this.getHeight() / 20 + 14);
            }
        }
    }

    private void drawContig(Graphics2D g2d, Rectangle2D rect) {
        g2d.setColor(new Color(244, 244, 244));
        g2d.fill(rect);
        g2d.setColor(Color.lightGray);
        g2d.draw(rect);
        g2d.setColor(new Color(80, 80, 80));
    }

    private void drawSequence(Graphics2D g2d, Rectangle2D rect, String sequence, double length) {
        g2d.setColor(new Color(244, 244, 244));
        g2d.fill(rect);
        Rectangle2D[] gaps = SortSequence.findGaps(sequence, rect.getWidth() / length, rect);
        for (Rectangle2D gap : gaps) {
            g2d.setColor(new Color(0, 153, 204));
            g2d.fill(gap);
        }
        g2d.setColor(Color.lightGray);
        g2d.draw(rect);
        g2d.setColor(new Color(80, 80, 80));
    }

    private void drawChosenLabel(Graphics2D g2d, LabelInfo labelInfo, Rectangle2D label, boolean orientated) {
        g2d.setColor(Color.red);
        g2d.drawLine((int) label.getCenterX(), (int) label.getMaxY(), (int) label.getCenterX(), (int) label.getMaxY() + this.getHeight() / 10);
        if (orientated) {
            g2d.drawString(labelInfo.getLabelPos(), (int) label.getCenterX() - g2d.getFontMetrics().stringWidth(labelInfo.getLabelPos()) / 2, (int) label.getMaxY() + this.getHeight() / 10 + 14);
        } else {
            double position = RawFileData.getQryContigs(chosenQry).getContigLen() - Double.parseDouble(labelInfo.getLabelPos());
            g2d.drawString(String.format("%.1f", position), (int) label.getCenterX() - g2d.getFontMetrics().stringWidth(labelInfo.getLabelPos()) / 2, (int) label.getMaxY() + this.getHeight() / 10 + 14);
        }
    }

    private void drawCoverageLabels(Graphics2D g2d, LabelInfo[] labelInfo, Rectangle2D[] labels) {
        for (int i = 0; i < labelInfo.length - 1; i++) {
            double coverage = Double.parseDouble(labelInfo[i].getCoverage());
            Rectangle2D label = labels[i];
            int lowCov = 20;
            int highCov = 50;
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
                int lowQual = 20;
                int highQual = 90;
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

    private void drawConfidence(Graphics2D g2d, Stroke defaultStroke) {
        Stroke dashed = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{6, 2}, 2);
        Stroke dotted = new BasicStroke(1, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 1, new float[]{1, 2}, 2);
        double confidence = Double.parseDouble(RawFileData.getAlignmentInfo(chosenRef + "-" + chosenQry).getConfidence());
        g2d.setColor(Color.black);
        int lowConf = 20;
        int highConf = 40;
        if (confidence < 20) {
            g2d.setStroke(dotted);
        } else if (confidence >= lowConf && confidence < highConf) {
            g2d.setStroke(dashed);
        } else {
            g2d.setStroke(defaultStroke);
        }
    }

    private void initComponents() {

        setPreferredSize(new java.awt.Dimension(0, 0));

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
