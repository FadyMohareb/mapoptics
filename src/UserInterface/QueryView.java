package UserInterface;

import DataTypes.Query;
import DataTypes.Reference;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/*
 * @author Josie
 */
public class QueryView extends JPanel {

    /**
     * Creates new form SingleAlignment
     */
    public QueryView(MapOpticsModel model) {
        this.model = model;
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
    private final MapOpticsModel model;
    private static final Color LIGHT_GREY = new Color(244, 244, 244);
    private static final Color GREY = new Color(192, 192, 192);
    private static final Color DARK_GREY = new Color(80, 80, 80);
    private static final Color BLACK = new Color(30, 30, 30);
    private static final Color GREEN = new Color(97, 204, 10);
    private int  WinoffX =0;
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke defaultStroke = g2d.getStroke();

        // draw alignment of single reference and contig
        try {
            Reference ref = model.getSelectedRef();
            if (!"".equals(chosenQry)) {
                if (ref.getDelQryIDs().contains(Integer.parseInt(chosenQry))) {
                    Font font = new Font("Tahoma", Font.ITALIC, 12);
                    g2d.setFont(font);
                    g2d.setColor(Color.red);
                    g2d.drawString("Selected query contig has been deleted.",
                            this.getWidth() / 2 - 115 , this.getHeight()/2);
                } else {
                    Font defaultFont = g2d.getFont();
                    Font fontB = new Font("Tahoma", Font.BOLD, 12);
                    g2d.setFont(fontB);
                    g2d.drawString("Reference ID:  ", 20, 20);
                    g2d.drawString("Query ID:  ", 20, 40);
                    int refStringLen = g2d.getFontMetrics().stringWidth("Reference ID:  ");
                    int qryStringLen = g2d.getFontMetrics().stringWidth("Query ID:  ");
                    g2d.setFont(defaultFont);
                    g2d.drawString(model.getSelectedRef().getRefID(), refStringLen + 20, 20);
                    g2d.drawString(chosenQry, qryStringLen + 20, 40);

                    //draw blank reference rectangle

                    double scale = model.getRectangleTotalWidth() / (this.getWidth() * 0.9);

                    //Reference ref = model.getSelectedRef(); // already declared at start of try statement
                    Query qry = ref.getQuery(chosenQry);
                    Map<Integer, Double> refSites = ref.getSites();
                    //double scale = qry.getLength()/(this.getWidth()* 0.9 );
                    // WinoffX= (int) ((int)Double.parseDouble(qry.getFirstAlignment()[1]) /scale);


                    Set<Integer> refAlignments = ref.getAlignmentSites();

                    Map<Integer, List<Double>> qrySites = qry.getSites();
                    Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();


                    List <Integer> refalignments = new ArrayList<>();
                    for (int site : qry.getSites().keySet()) {
                        if (qryAlignments.containsKey(site)) {
                            refalignments.add(qryAlignments.get(site).get(0));
                            //System.out.println(Integer.toString(qryAlignments.get(site).get(0)));
                        }}
                    // Set<Integer> refAlignments = new HashSet<>(refalignments);;


/*
                // Draw scale bar
                g2d.setColor(BLACK);
                int scaleWidth = (int) Math.floor(500000 / scale);
                g2d.drawLine(10, 63, 10 + scaleWidth, 63);
                for (int i = 0; i < 5; i++) {
                    int interval = (int) (10 + (Math.ceil(scaleWidth / 5.0) * i));
                    g2d.drawLine(interval, 60, interval, 65);
                }

                g2d.drawLine(10 + scaleWidth, 60, 10 + scaleWidth, 66);
                g2d.drawString("500 000 bp", 15, 74);
*/

                    // Draw reference rectangle and sites
                    Rectangle2D refRect = new Rectangle2D.Double(0, 10, ref.getLength(), 50);
                    // Rectangle2D refRectRaw = new Rectangle2D.Double(0, 30, ref.getAlignmentLen(), 50) ;
                    Rectangle2D refRectScaled = new Rectangle2D.Double(
                            this.getWidth() /20-WinoffX,
                            80,
                            refRect.getWidth()/ scale,
                            60);
                    ref.setQryViewRect(refRectScaled);
                    g2d.setColor(LIGHT_GREY);
                    g2d.fill(refRectScaled);
                    g2d.setColor(GREY);
                    g2d.draw(refRectScaled);
                    g2d.setColor(DARK_GREY);
                    drawScaleBar(g2d, refRectScaled);

                    int refOffSetY = (int) refRectScaled.getY();
                    int refHeight = (int) refRectScaled.getHeight();

                    //int firstRefAlignment=  Integer. valueOf(qry.getFirstAlignment()[1]);
                    //Double startpos = refSites.get(firstRefAlignment);
                    //System.out.println(qry.getFirstAlignment()[1]);
                    //System.out.println(qry.getFirstAlignment()[0]);

                    //draw reference sites
                    for (int site : refSites.keySet()) {
                        if (refalignments.contains(site)) {

                            if (refAlignments.contains(site)) {
                                g2d.setColor(GREEN);
                            }} else {
                            g2d.setColor(BLACK);
                        }
                        //System.out.println(Double.toString(refSites.get(site)));
                        int position = (int) ((refSites.get(site)/ scale) + this.getWidth() /20-WinoffX);
                        g2d.drawLine(position, refOffSetY, position, refOffSetY + refHeight);
                    }

                    // For each query, draw rectangle, sites and alignments

                    Rectangle2D qryRectRaw = qry.getRectangle();
                    Rectangle2D qryRectScaled = new Rectangle2D.Double(
                            (qryRectRaw.getX()  / scale ) + this.getWidth() / 20.0 -WinoffX,
                            qryRectRaw.getY() + 50,
                            qryRectRaw.getWidth() / scale,
                            qryRectRaw.getHeight());
                    qry.setQryViewRect(qryRectScaled);
                    g2d.setColor(LIGHT_GREY);
                    g2d.fill(qryRectScaled);
                    g2d.setColor(GREY);
                    g2d.draw(qryRectScaled);

                    int qryOffSetY = (int) qryRectScaled.getY();
                    int qryHeight = (int) qryRectScaled.getHeight();
                    //Map<Integer, List<Double>> qrySites = qry.getSites();
                    //Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();

                    for (int site : qry.getSites().keySet()) {
                        boolean match = false;
                        if (qryAlignments.containsKey(site)) {
                            match = true;
                            g2d.setColor(GREEN);

                        } else {
                            g2d.setColor(BLACK);
                        }

                        int position = (int) ((qrySites.get(site).get(0) / scale) + qryRectScaled.getX()-WinoffX);
                        g2d.drawLine(position, qryOffSetY, position, qryOffSetY + qryHeight);

                        g2d.setColor(BLACK);
                        // Draw alignment
                        if (match) {
                            for (int i : qryAlignments.get(site)) {
                                int refPositionX = (int) ((refSites.get(i) / scale) + refRectScaled.getX()-WinoffX);
                                int refPositionY = (int) (refRectScaled.getY() + refRectScaled.getHeight());
                                g2d.drawLine(position, qryOffSetY, refPositionX, refPositionY);
                            }
                        }
                    }
                    if (!position.isEmpty()) {

                        g2d.setStroke(g2d.getStroke());
                        g2d.setColor(Color.white);
                        int stringLen = g2d.getFontMetrics().stringWidth(position);
                        g2d.fillRect(mouseX, mouseY, stringLen + 16, 14);
                        g2d.setColor(Color.black);
                        g2d.drawRect(mouseX, mouseY, stringLen + 16, 14);
                        g2d.drawString(position, mouseX + 12, mouseY + 12);}
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

        g2d.drawLine((int) refRect.getMinX(), (int) refRect.getMinY() - 15, (int) (refRect.getMinX() + refRect.getWidth()), (int) refRect.getMinY() - 15);
        int count = 0;
        int numScales = (int) refRect.getWidth() / 100;
//        double length = RawFileData.getRefContigs(chosenRef).getContigLen();
        double length = model.getSelectedRef().getLength();
        if (numScales != 0) {
            for (int i = 0; i < numScales + 1; i++) {
                g2d.drawLine((int) (refRect.getMinX() + (refRect.getWidth() / numScales) * i), (int) refRect.getMinY() - 25, (int) (refRect.getMinX() + (refRect.getWidth() / numScales) * i), (int) refRect.getMinY() - 15);
                g2d.drawString(String.format("%.2f", ((double) count) / 1000) + " kb", (int) (refRect.getMinX() + ((refRect.getWidth() / numScales) * i) - g2d.getFontMetrics().stringWidth(String.format("%.2f", ((double) count) / 1000) + " kb") / 2), (int) refRect.getMinY() - 27);
                count = (int) (count + length / numScales);
            }
        } else {
            g2d.drawLine((int) (refRect.getMinX()), (int) refRect.getMinY() - 25, (int) (refRect.getMinX()), (int) refRect.getMinY() - 15);
            g2d.drawString(String.format("%.2f", 0.0) + " kb", (int) (refRect.getMinX() - g2d.getFontMetrics().stringWidth(String.format("%.2f", 0.0) + " kb") / 2), (int) refRect.getMinY() - 27);
            g2d.drawLine((int) (refRect.getMinX() + refRect.getWidth()), (int) refRect.getMinY() - 25, (int) (refRect.getMinX() + refRect.getWidth()), (int) refRect.getMinY() - 15);
            g2d.drawString(String.format("%.2f", length / 1000) + " kb", (int) (refRect.getMinX() + refRect.getWidth() - g2d.getFontMetrics().stringWidth(String.format("%.2f", length / 1000) + " kb") / 2), (int) refRect.getMinY() - 27);

        }
    }



/*
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
*/
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
