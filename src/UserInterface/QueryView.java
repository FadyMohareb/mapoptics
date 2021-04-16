package UserInterface;

import DataTypes.*;
import Datasets.Default.QueryViewData;
import Datasets.Default.RawFileData;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

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
    private static boolean referenceViewSelect = false;
    private static boolean qryViewSelect = false;
    private static String position = "";
    private static int mouseX = 0;
    private static int mouseY = 0;
    private double scale=1000;
    private static String[] regions;


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

    public static void setReferenceViewSelect(boolean referenceViewSelect) {
        QueryView.referenceViewSelect = referenceViewSelect;
    }

    public static void setQryViewSelect(boolean qryViewSelect) {
        QueryView.qryViewSelect = qryViewSelect;
    }

    public static boolean isRegionView() {
        return regionView;
    }

    private final MapOpticsModel model;
    private static final Color LIGHT_GREY = new Color(244, 244, 244);
    private static final Color GREY = new Color(192, 192, 192);
    private static final Color DARK_GREY = new Color(80, 80, 80);
    private static final Color BLACK = new Color(30, 30, 30);
    private static final Color GREEN = new Color(97, 204, 10);
    private double WinoffX;
    private double WinoffX2;
    private int refx;
    private double qryRegionstart;
    private double qryRegionend;
    private boolean RefRect;
    private boolean QryRect;
    private double refOffX;
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

                WinoffX=0;
                WinoffX2=0;
                refx=0;
                qryRegionstart=0;
                qryRegionend=0;
                Reference ref=null ;
                Query qry=null;
                RefRect=true;
                QryRect=true;
                refOffX=this.getWidth() / 20;// the minX of refrectangle

                // draw relative to query alignment
                ref = model.getSelectedRef();
                qry = ref.getQuery(chosenQry);
                if (qry != null) {
                    Rectangle2D refRect = new Rectangle2D.Double(0, 20, ref.getLength(), 50);
                    Rectangle2D qryRect = qry.getRectangle();

                    //find the start and end pos of the alignment in ref contig
                    double Start=0 ;
                    double End=0 ;
                    double AlignStart= ref.getRefAlignPos(chosenQry)[0];
                    double AlignEnd= ref.getRefAlignPos(chosenQry)[1];
                    double alignlen = AlignEnd - AlignStart;
                    int regionOffX=0;
                    if(regionView==false) {
                        scale = qryRect.getWidth() / (this.getWidth() * 0.9);
                        Start=AlignStart;
                        End=AlignEnd;
                    }
                    else if(regions[0].equals("")){
                        scale = qryRect.getWidth() / (this.getWidth() * 0.9);
                        Start=AlignStart;
                        End=AlignEnd;
                    }
                    else{

                        if (referenceViewSelect) {refOffX=0;
                            Double reflen = ref.getLength();
                            if (Integer.parseInt(regions[1]) >= reflen) {
                                scale = (reflen - Integer.parseInt(regions[0])) / (this.getWidth() );
                                Start = Double.parseDouble(regions[0]);
                                End = Double.parseDouble(regions[1]);
                            }else{
                                scale = (Integer.parseInt(regions[1]) - Integer.parseInt(regions[0])) / this.getWidth() ;
                                Start = Double.parseDouble(regions[0]);
                                End = Double.parseDouble(regions[1]);
                            }
                            if(Integer.parseInt(regions[1])<=AlignStart||Integer.parseInt(regions[0])>=AlignEnd ){
                                QryRect=false;
                            }
                        }else if(qryViewSelect){
                            Double qrylen= qry.getLength();
                            Double reflen = ref.getLength();
                            double qryAlignStart= ref.getRefAlignPos(chosenQry)[0];
                            double qryAlignEnd= ref.getRefAlignPos(chosenQry)[1];
                            if (Integer.parseInt(regions[1]) >= qrylen) {
                                scale = (qrylen - Integer.parseInt(regions[0])) / (this.getWidth() );
                                qryRegionstart=Double.parseDouble(regions[0]);
                                qryRegionend=qry.getLength();
                                Start = 0.0;
                                End = reflen;
                            }else{
                                scale = (Integer.parseInt(regions[1]) - Integer.parseInt(regions[0])) / (this.getWidth());
                                qryRegionstart=Double.parseDouble(regions[0]);
                                qryRegionend=Double.parseDouble(regions[1]);
                                Start = 0.0;
                                End = reflen;
                            }
                            if(Integer.parseInt(regions[1])<=qryAlignStart||Integer.parseInt(regions[0])>=qryAlignEnd){
                                RefRect=false;
                            }
                        }
                    }

                    //set scale
                    g2d.setColor(Color.BLACK);
                    Map<Integer, Double> refSites = ref.getSites();
                    Set<Integer> refAlignments = ref.getAlignmentSites();
                    Map<Integer, Double> qrySites = qry.getQryViewSites();
                    Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();
                    List<Integer> refalignments = new ArrayList();
                    //add this scale to QueryViewData
                    QueryViewData.setSelectedRefLen(End - Start);
                    //find the first alignment position on reference contig and query contig
                    //for reference contig
                    for (int site : qry.getSites().keySet()) {
                        if (qryAlignments.containsKey(site)) {
                            refalignments.add(qryAlignments.get(site).get(0));
                        }
                    }
                    for (int site : refSites.keySet()) {
                        if (refalignments.contains(site)) {
                            if (refAlignments.contains(site)) {
                                if (WinoffX == 0) {
                                    WinoffX = refSites.get(site) / scale;//get ref position

                                } else {
                                    if ((refSites.get(site) / scale) < WinoffX) {
                                        WinoffX = refSites.get(site);
                                        break;
                                    }
                                }
                            }
                        }
                    }
                    //for query contig
                    for (int site : qry.getQryViewSites().keySet()) {//get the position of first aligned site
                        if (qryAlignments.containsKey(site)) {

                            if (WinoffX2 == 0) {
                                WinoffX2 = (qrySites.get(site)) / scale;
                                break;
                            } else {
                                if ((qrySites.get(site) / scale) < WinoffX2) {
                                    WinoffX2 = qrySites.get(site) / scale;
                                    break;
                                }
                            }
                        }
                    }
                    //check the reorientation
                    boolean isFlipped = qry.isFlipped();

                    if (isFlipped == false) {
                        if (ref.getLength() > qry.getLength()) {
                            refx = (int) (WinoffX - WinoffX2);
                        } else {
                            refx = -(int) (WinoffX2 - WinoffX);
                        }
                    } else {
                        if (ref.getLength() > qry.getLength()) {
                            refx = (int) (WinoffX - WinoffX2 + alignlen / scale);
                        } else {
                            refx = -(int) ((WinoffX2 - WinoffX) - alignlen / scale);
                        }
                    }
                    //check if it is regionView
                    if(regionView==true& referenceViewSelect ==true){//if search reference

                        regionOffX= refx-(int)(Integer.parseInt(regions[0])/scale);
                        refx=(int) (Integer.parseInt(regions[0])/scale);

                    }
                    else if(regionView==true& qryViewSelect ==true){//if search query
                        regionOffX= -(int)(Integer.parseInt(regions[0])/scale);
                        refx=refx-regionOffX+1;
                    }
                    //check if display rectangles and set rectangles
                    Rectangle2D qryScaled;
                    Rectangle2D refScaled;
                    /*if(QryRect){
                         //set rectangles
                          qryScaled = zoomQryRectangle(qryRect,regionOffX);
                     }else{
                         qryScaled = new Rectangle2D.Double(0, 90, 0, refRect.getHeight());
                     }
                    if(RefRect){
                        //set rectangles
                        refScaled = zoomRectangle(refRect, Start, End);
                    }else{
                        refScaled = new Rectangle2D.Double(0, 90, 0, refRect.getHeight());
                    }*/
                    qryScaled = zoomQryRectangle(qryRect,regionOffX);
                    refScaled = zoomRectangle(refRect, Start, End);
                    QueryViewData.setRefStart(Start);
                    QueryViewData.setQryStart(regionOffX);
                    //QueryViewData.getQryLen();

                    // draw query contig
                    qry.setQryViewRect(qryScaled);
                    drawContig(g2d, qryScaled, chosenQry);

                    // draw reference contig
                    ref.setQryViewRect(refScaled);
                    drawContig(g2d, refScaled, chosenRef);

                    //draw reference labels
                    int WindowWidth = this.getWidth();
                    for (int site : refSites.keySet()) {

                        g2d.setColor(Color.black);
                        if (refalignments.contains(site)) {
                            if (refAlignments.contains(site)) {
                                g2d.setColor(GREEN);
                            } else {
                                g2d.setColor(BLACK);
                            }
                        }
                        if (refSites.get(site) >= Start & refSites.get(site) <= End) {
                            int position = (int) (((refSites.get(site) / scale) + refOffX));
                            int scaledpos=position-refx;
                            if(scaledpos>=0&scaledpos<=WindowWidth){
                                g2d.drawLine(scaledpos, (int) refScaled.getMinY(), scaledpos, (int) refScaled.getMaxY());
                            }}
                    }
                    // For each query, draw sites and alignments

                    int qryOffSetY = (int) qryScaled.getY();
                    int qryHeight = (int) qryScaled.getHeight();


                    for (int site : qry.getQryViewSites().keySet()) {
                        boolean match = false;
                        if (qryAlignments.containsKey(site)) {
                            match = true;
                            g2d.setColor(GREEN);

                        } else {
                            g2d.setColor(BLACK);
                        }

                        int position = (int) ((qrySites.get(site) / scale) + refOffX+ regionOffX);
                        //if(position>=0 &position<=WindowWidth){
                        g2d.drawLine(position, qryOffSetY, position, qryOffSetY + qryHeight);
                        //}

                        g2d.setColor(BLACK);
                        // Draw alignment
                        if (match) {
                            for (int i : qryAlignments.get(site)) {
                                int refPositionX = (int) (((refSites.get(i)) / scale) + refOffX - refx);
                                int refPositionY = (int) (refScaled.getY() + refScaled.getHeight());
                                // if(refPositionX>=0&refPositionX<=this.getWidth()&position>=0 &position<=this.getWidth()){
                                g2d.drawLine(position, qryOffSetY, refPositionX, refPositionY);
                                //}
                            }
                        }
                    }
                    // draw scalebars
                    drawScaleBar(g2d, refScaled, Start, End, true);
                    if(QryRect){
                        drawScaleBar(g2d, zoomQryRectangle(qryRect,regionOffX), Start, End, false);}
                    if (!chosenLabel.equals("")&!regionView) {
                        // draw chosen label
                        Double labelpos = 0.0;
                        if (isFlipped == false) {//if it is reorientated
                            labelpos = qry.getSites().get(Integer.parseInt(chosenLabel)).get(0);//get label position
                        } else {
                            labelpos = qry.getLength() - qry.getSites().get(Integer.parseInt(chosenLabel)).get(0);//get label position
                        }
                        g2d.setColor(Color.red);
                        g2d.drawLine((int) ((int) (labelpos / scale + this.getWidth() / 20)),
                                230,
                                (int) (labelpos / scale + this.getWidth() / 20),
                                290);
                        g2d.drawString(String.format("%.1f", labelpos), (int) (labelpos / scale + this.getWidth() / 20), 290);

                    }
//
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

    private void drawContig(Graphics2D g2d, Rectangle2D rect, String id) {
        g2d.setColor(new Color(244, 244, 244));
        g2d.fill(rect);
        g2d.setColor(Color.lightGray);
        g2d.draw(rect);
        //g2d.drawLine((int) rect.getMinX(), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() - this.getHeight() / 25);
        g2d.setColor(new Color(80, 80, 80));
    }
    private Rectangle2D zoomRectangle(Rectangle2D refRect, Double start, Double end){

        Rectangle2D refRectScaled = new Rectangle2D.Double(
                (this.getWidth() /20+start/scale-refx),
                90,
                (end-start)/scale,
                refRect.getHeight());
        if(refRectScaled.getWidth()>this.getWidth()){
            if(regionView&!referenceViewSelect){
                refRectScaled = new Rectangle2D.Double(
                        (this.getWidth() /20+start/scale-refx),
                        90,
                        refRect.getWidth()/scale,
                        refRect.getHeight());
                return refRectScaled;

            }else{
                refRectScaled= new Rectangle2D.Double(
                        0,
                        90,
                        this.getWidth(),
                        refRect.getHeight());
                return refRectScaled;}
        }
        return refRectScaled;
    }



    private Rectangle2D zoomQryRectangle(Rectangle2D qryRect,int regionOffX){

        Rectangle2D qryRectScaled = new Rectangle2D.Double(
                this.getWidth() /20+regionOffX,
                230,
                qryRect.getWidth()/ scale,
                qryRect.getHeight());
        if(regionView&referenceViewSelect){
            qryRectScaled= new Rectangle2D.Double(
                    regionOffX,
                    230,
                    qryRect.getWidth()/ scale,
                    qryRect.getHeight());

            return qryRectScaled;
        }
        return qryRectScaled;
    }


    private void drawScaleBar(Graphics2D g2d, Rectangle2D rect,double start, double end ,boolean ref) {
        g2d.setColor(Color.black);
        if (ref) {
            g2d.drawLine((int) rect.getMinX(), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() - this.getHeight() / 25);
            int count = (int)start;
            int numScales = (int) rect.getWidth() / 100;
            double length = end-start;
            if (numScales != 0) {
                for (int i = 0; i < numScales + 1; i++) {
                    g2d.drawLine((int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMinY() - this.getHeight() / 20);
                    g2d.drawString(String.format("%.2f", ((double) count) / 1000) + " kb", (int) (rect.getMinX() + ((rect.getWidth() / numScales) * i) - g2d.getFontMetrics().stringWidth(String.format("%.2f", ((double) count) / 1000) + " kb") / 2), (int) rect.getMinY() - this.getHeight() / 20 - 2);
                    count = (int) (count + length / numScales);
                }
            } else {
                g2d.drawLine((int) (rect.getMinX()), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX()), (int) rect.getMinY() - this.getHeight() / 20);
                g2d.drawString(String.format("%.2f", (double) 0.0) + " kb", (int) (rect.getMinX() - g2d.getFontMetrics().stringWidth(String.format("%.2f", (double) 0.0) + " kb") / 2), (int) rect.getMinY() - this.getHeight() / 20 - 2);
                g2d.drawLine((int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() - this.getHeight() / 20);
                g2d.drawString(String.format("%.2f", (double) length / 1000) + " kb", (int) (rect.getMinX() + rect.getWidth() - g2d.getFontMetrics().stringWidth(String.format("%.2f", (double) length / 1000) + " kb") / 2), (int) rect.getMinY() - this.getHeight() / 20 - 2);

            }
        } else {
            int count;
            int numScales;
            double length;
            if(regionView==true& qryViewSelect ==true){
                g2d.drawLine((int) rect.getMinX(), (int) rect.getMaxY() + this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMaxY() + this.getHeight() / 25);
                count = (int)qryRegionstart;
                numScales = (int) rect.getWidth() / 100;
                length =  qryRegionend-qryRegionstart;
            }else{
                g2d.drawLine((int) rect.getMinX(), (int) rect.getMaxY() + this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMaxY() + this.getHeight() / 25);
                count = 0;
                numScales = (int) rect.getWidth() / 100;
                length =  model.getSelectedRef().getQuery(chosenQry).getLength();
            }
            if (numScales != 0) {
                for (int i = 0; i < numScales + 1; i++) {
                    g2d.drawLine((int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMaxY() + this.getHeight() / 20, (int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMaxY() + this.getHeight() / 25);
                    g2d.drawString(String.format("%.2f", ((double) count) / 1000) + " kb", (int) (rect.getMinX() + ((rect.getWidth() / numScales) * i) - g2d.getFontMetrics().stringWidth(String.format("%.2f", ((double) count) / 1000) + " kb") / 2), (int) rect.getMaxY() + this.getHeight() / 20 + 14);
                    count = (int) (count + length / numScales);
                }
            } else {
                g2d.drawLine((int) (rect.getMinX()), (int) rect.getMaxY() + this.getHeight() / 25, (int) (rect.getMinX()), (int) rect.getMaxY() + this.getHeight() / 20);
                g2d.drawString(String.format("%.2f", (double) 0.0) + " kb", (int) (rect.getMinX() - g2d.getFontMetrics().stringWidth(String.format("%.2f", (double) 0.0) + " kb") / 2), (int) rect.getMinY() + +this.getHeight() / 20 + 14);
                g2d.drawLine((int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() + this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() + this.getHeight() / 20);
                g2d.drawString(String.format("%.2f", (double) length / 1000) + " kb", (int) (rect.getMinX() + rect.getWidth() - g2d.getFontMetrics().stringWidth(String.format("%.2f", (double) length / 1000) + " kb") / 2), (int) rect.getMinY() + this.getHeight() / 20 + 14);
            }
        }
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


    public static void setRegionscale(String[] regions){
        QueryView.regions=regions;
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




