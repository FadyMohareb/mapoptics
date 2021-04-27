package UserInterface;

import DataTypes.Query;
import DataTypes.Reference;
import Datasets.Default.QueryViewData;
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
    private final MapOpticsModel model;
    private static final Color LIGHT_GREY = new Color(244, 244, 244);
    private static final Color GREY = new Color(192, 192, 192);
    private static final Color DARK_GREY = new Color(80, 80, 80);
    private static final Color BLACK = new Color(30, 30, 30);
    private static final Color GREEN = new Color(97, 204, 10);
    private static String chosenRef = "";
    private static String chosenQry = "";
    private static String chosenLabel = "";
    private static String labelStyle = "match";
    private static boolean confidenceView = false;
    private static boolean regionView = false;
    private static boolean referenceViewSelect = false;
    private static boolean qryViewSelect = false;
    private static boolean refSequences = false;
    private static boolean qrySequences = false;
    private static String position = "";
    private static int mouseX = 0;
    private static int mouseY = 0;
    private double scale=1000.0;
    private double regionOffX;
    private static String[] regions;
    private double WinoffX;
    private double WinoffX2;
    private double refx;
    private double qryRegionstart;
    private double qryRegionend;
    private double refOffX;
    private double alignlen;
    private double Start;
    private double End;
    private List<Integer> refalignments ;
    private static boolean reorientation=false;


    public QueryView(MapOpticsModel model) {
        this.model = model;
        initComponents();
    }
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
    public static void setRefSequences(boolean refSequences){
        QueryView.refSequences=refSequences;
    }
    public static void setQrySequences(boolean qrySequences){
        QueryView.qrySequences=qrySequences;
    }
    private boolean isFlipped;


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
                refx=0.0;
                alignlen=0;
                Start=0 ;
                End=0 ;
                qryRegionstart=0;
                qryRegionend=0;
                Reference ref=null ;
                Query qry=null;
                refalignments = new ArrayList();
               // refOffX=this.getWidth() / 20;// the minX of refrectangle
                refOffX=0;

                // draw relative to query alignment
                ref = model.getSelectedRef();
                qry = ref.getQuery(chosenQry);
                //check the reorientation
                isFlipped = qry.isFlipped();
                if(regionView&qry.getOrientation().equals("-")){
                    if (isFlipped==false){
                        qry.reOrientate();
                        isFlipped = qry.isFlipped();
                       // System.out.println("after flip "+isFlipped);
                      //  repaint();
                    }
                }
                if (qry != null) {
                    Rectangle2D refRect = new Rectangle2D.Double(0, 20, ref.getLength(), 50);
                    Rectangle2D qryRect = qry.getRectangle();
                    //set scale parameters
                    setScaleParameters(ref,qry,refRect,qryRect);
                    setZoomParameters(ref, qry);
                    //add this scale to QueryViewData
                    QueryViewData.setSelectedRefLen(End - Start);
                    //get sites information
                    Map<Integer, Double> refSites = ref.getSites();
                    Set<Integer> refAlignments = ref.getAlignmentSites();
                    Map<Integer, Double> qrySites = qry.getQryViewSites();
                    Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();

                    // set rectangles
                    Rectangle2D qryScaled;
                    Rectangle2D refScaled;
                    // add to QueryViewData
                    qryScaled = zoomQryRectangle(qryRect,regionOffX);
                    refScaled = zoomRectangle(refRect);
                    QueryViewData.setRefStart(Start);
                    QueryViewData.setQryStart(regionOffX);

                    // draw query contig
                    qry.setQryViewRect(new Rectangle2D.Double(refOffX+regionOffX, 230, qryRect.getWidth()/ scale, qryRect.getHeight()));
                    drawContig(g2d, qryScaled, chosenQry);

                    // draw reference contig
                    if(Start!=0){
                        ref.setQryViewRect(new Rectangle2D.Double(refOffX+Start/scale-refx, 90, (End-Start)/scale, refRect.getHeight()));
                    }else{
                        ref.setQryViewRect(new Rectangle2D.Double(refOffX-refx, 90, (End-Start)/scale, refRect.getHeight()));
                    }
                    drawContig(g2d, refScaled, chosenRef);

                    // view gap
                    if(refSequences){
                        ArrayList<Integer> gaps= new ArrayList<>(QueryViewData.setSequences().get(chosenRef));
                        for(int i=0;i < gaps.size() ;i=i+2){
                            Rectangle2D gap;
                            if(gaps.get(i)!=0) {
                                 gap = new Rectangle2D.Double(gaps.get(i) / scale - refx, 90, (gaps.get(i + 1) - gaps.get(i))/scale, refRect.getHeight());
                            }else{
                                 gap = new Rectangle2D.Double(- refx, 90, (gaps.get(i + 1) - gaps.get(i))/scale, refRect.getHeight());
                            }
                            g2d.setColor(new Color(0, 153, 204));
                            g2d.fill(gap);

                        }
                        }else if(qrySequences){
                        ArrayList<Integer> gaps= new ArrayList<>(QueryViewData.setSequences().get(chosenQry));
                        for(int i=0;i < gaps.size();i=i+2){
                            Rectangle2D gap;
                            if(gaps.get(i)!=0) {
                                gap = new Rectangle2D.Double(gaps.get(i) / scale +regionOffX,90, (gaps.get(i + 1) - gaps.get(i))/scale, refRect.getHeight());
                            }else{
                                gap = new Rectangle2D.Double( regionOffX, 90, (gaps.get(i + 1) - gaps.get(i))/ scale, refRect.getHeight());
                            }

                            g2d.setColor(new Color(0, 153, 204));
                            g2d.fill(gap);
                        }

                    }
                    g2d.setColor(new Color(80, 80, 80));

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
                            int position = (int) ((refSites.get(site) / scale) + refOffX-refx);
                            if(position>=0&position<=WindowWidth){
                                g2d.drawLine(position, (int) refScaled.getMinY(), position, (int) refScaled.getMaxY());
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
                        if(position>=0 &position<=WindowWidth){
                            g2d.drawLine(position, qryOffSetY, position, qryOffSetY + qryHeight);
                        }

                        g2d.setColor(BLACK);
                        // Draw alignment
                        if (match) {
                            for (int i : qryAlignments.get(site)) {
                                int refPositionX = (int) (((refSites.get(i)) / scale) + refOffX - refx);
                                int refPositionY = (int) (refScaled.getY() + refScaled.getHeight());
                                if(refPositionX>=0&refPositionX<=this.getWidth()&position>=0 &position<=this.getWidth()){
                                    g2d.drawLine(position, qryOffSetY, refPositionX, refPositionY);
                                }
                            }
                        }
                    }
                    // draw scalebars
                    double qrylength=qry.getLength();
                    drawScaleBar(g2d, refScaled,qrylength, true);
                    drawScaleBar(g2d, zoomQryRectangle(qryRect,regionOffX),qrylength, false);
                    //display chosen label
                    drawChosenLabel(g2d,qry,isFlipped);

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
            //display mouse position
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
    private void setZoomParameters(Reference ref,Query qry){
        //get sites information
        Map<Integer, Double> refSites = ref.getSites();
        Set<Integer> refAlignments = ref.getAlignmentSites();
        Map<Integer, Double> qrySites = qry.getQryViewSites();
        Map<Integer, List<Integer>> qryAlignments = qry.getAlignmentSites();
        //-------find the first align position ----
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

        if (isFlipped == false) {
            if (ref.getLength() > qry.getLength()) {
                refx = WinoffX - WinoffX2;
            } else {
                refx =  -(WinoffX2 - WinoffX);
            }
        } else {
            if (ref.getLength() > qry.getLength()) {
                refx = WinoffX - WinoffX2 + alignlen / scale;
            } else {
                refx = -((WinoffX2 - WinoffX) - alignlen / scale);
            }
        }
        //check if it is regionView
        if(regionView==true& referenceViewSelect ==true){//if search reference
            qryRegionstart=Start-refx*scale;
            qryRegionend=End-refx*scale;

            if(Integer.parseInt(regions[0])!=0){
                regionOffX=refx-Integer.parseInt(regions[0])/scale;
                refx=Integer.parseInt(regions[0])/scale;
            }else{
                regionOffX=refx;
                refx=0.0;}


            if(qryRegionstart<=0){
                qryRegionstart=0;
            }
            if(qryRegionend<=0){
                qryRegionend=0;
            }
            if(qryRegionend>=qry.getLength()){
                qryRegionend=qry.getLength();
            }

        }
        else if(regionView==true& qryViewSelect ==true){//if search query

            Start=qryRegionstart+refx*scale;
            End=qryRegionend+refx*scale;
            //System.out.println("refx "+refx+" scale "+scale);
           // System.out.println("qryStart "+qryRegionstart+" qryEnd "+qryRegionend);
           // System.out.println("Start "+Start+" End "+End);

            if(isFlipped){
                if(qryRegionstart!=qry.getLength()){
                    regionOffX= -qryRegionstart/scale;
                    refx=refx-regionOffX;
                }else{
                    regionOffX=qry.getLength();
                    refx=refx-regionOffX;
                }
            }else{
                if(Integer.parseInt(regions[0])!=0){
                    regionOffX= -Integer.parseInt(regions[0])/scale;
                    refx=refx-regionOffX;
                }else{
                    regionOffX=0.0;
                    refx=refx-regionOffX;
                }


            }
        }else{
            Start=qryRegionstart+refx*scale;
            End=qryRegionend+refx*scale;
            regionOffX=0.0;
            refx=refx-regionOffX;

        }
    }
    private void setScaleParameters(Reference ref,Query qry,Rectangle2D refRect,Rectangle2D qryRect){
        //find the start and end pos of the alignment in ref contig
        double AlignStart= ref.getRefAlignPos(chosenQry)[0];
        double AlignEnd= ref.getRefAlignPos(chosenQry)[1];
        alignlen = AlignEnd - AlignStart;
        regionOffX=0.0;
        Double reflen = ref.getLength();
        if(regionView==false) {
            scale = qryRect.getWidth() / (this.getWidth() );
            //Start=AlignStart;
            //End=AlignEnd;
            Start = 0.0;
            End = reflen;
            qryRegionstart=0.0;
            qryRegionend=qry.getLength();
        }
        else if(regions[0].equals("")){
            scale = qryRect.getWidth() / (this.getWidth() );
            //Start=AlignStart;
            //End=AlignEnd;
            Start = 0.0;
            End = reflen;
            qryRegionstart=0.0;
            qryRegionend=qry.getLength();
        }
        else{
            refOffX=0;
            if (referenceViewSelect) {
                scale = (Double.parseDouble(regions[1]) - Double.parseDouble(regions[0])) / this.getWidth() ;
                Start = Double.parseDouble(regions[0]);
                End = Double.parseDouble(regions[1]);
            }else if(qryViewSelect){
                qryRegionstart= Double.parseDouble(regions[0]);
                qryRegionend=Double.parseDouble(regions[1]);
                scale = (qryRegionend-qryRegionstart) / this.getWidth();
                Start = 0.0;
                End = reflen;
            }
           // System.out.println("check flip in qrysearch "+isFlipped);
            if(isFlipped){
                double flippedstart = qry.getLength()-qryRegionend;
                double flippedend = qry.getLength()-qryRegionstart;
                qryRegionstart= flippedstart;
                qryRegionend=flippedend;
            }
        }


    }
    private void drawContig(Graphics2D g2d, Rectangle2D rect, String id) {
        g2d.setColor(new Color(244, 244, 244));
        g2d.fill(rect);
        g2d.setColor(Color.lightGray);
        g2d.draw(rect);
        g2d.setColor(new Color(80, 80, 80));
    }
    private Rectangle2D zoomRectangle(Rectangle2D refRect){

        double x = 0.0;
        double w=(End-Start)/scale;
        double x1;
        double w1;

            if(referenceViewSelect){
                Rectangle2D refRectScaled = new Rectangle2D.Double(0, 90,this.getWidth(), refRect.getHeight());
                return refRectScaled;
            }else{
                if(Start!=0){ x=Start/scale-refx;
                }else{x=-refx;}

            }
        if(x<0){
            x1=0;
        }else{
            x1=x;}
        if((x+w)>this.getWidth()){
            w1=this.getWidth()-x1;
        }else{
            w1=w+x-x1;
        }
        Rectangle2D refRectScaled = new Rectangle2D.Double(x1, 90, w1, refRect.getHeight());
        return refRectScaled;
    }

    private Rectangle2D zoomQryRectangle(Rectangle2D qryRect,double regionOffX){
        double x;
        double w=qryRect.getWidth()/ scale;
        double x1;
        double w1;
        x=regionOffX;

        if(x<0){
            x1=0;
        }else{
            x1=x;}
        if((x+w)>this.getWidth()){
            w1=this.getWidth()-x1;
        }else{
            w1=w+x-x1;
        }
        Rectangle2D qryRectScaled= new Rectangle2D.Double(x1, 230, w1, qryRect.getHeight());
        return qryRectScaled;
    }

    private void drawScaleBar(Graphics2D g2d, Rectangle2D rect,double qrylength,boolean ref) {
        g2d.setColor(Color.black);
        if (ref) {
            g2d.drawLine((int) rect.getMinX(), (int) rect.getMinY() - this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMinY() - this.getHeight() / 25);
            int count = (int)Start;
            int numScales = (int) rect.getWidth() / 100;
            double length = End-Start;
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
            g2d.drawLine((int) rect.getMinX(), (int) rect.getMaxY() + this.getHeight() / 25, (int) (rect.getMinX() + rect.getWidth()), (int) rect.getMaxY() + this.getHeight() / 25);
            count = (int)qryRegionstart;
            numScales = (int) rect.getWidth() / 100;
            length =  qryRegionend-qryRegionstart;
            if (numScales != 0) {
                if(isFlipped){
                    count=(int)(qrylength-qryRegionstart);
                   // System.out.println("count "+count+" region start "+qryRegionstart+" region end"+qryRegionend+ " length "+length);
                    for (int i = 0; i < numScales + 1; i++) {
                        g2d.drawLine((int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMaxY() + this.getHeight() / 20, (int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMaxY() + this.getHeight() / 25);
                        g2d.drawString(String.format("%.2f", ((double) count) / 1000) + " kb", (int) (rect.getMinX() + ((rect.getWidth() / numScales) * i) - g2d.getFontMetrics().stringWidth(String.format("%.2f", ((double) count) / 1000) + " kb") / 2), (int) rect.getMaxY() + this.getHeight() / 20 + 14);
                        count = (int) (count - length / numScales);
                    }
                }else{
                for (int i = 0; i < numScales + 1; i++) {
                    g2d.drawLine((int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMaxY() + this.getHeight() / 20, (int) (rect.getMinX() + (rect.getWidth() / numScales) * i), (int) rect.getMaxY() + this.getHeight() / 25);
                    g2d.drawString(String.format("%.2f", ((double) count) / 1000) + " kb", (int) (rect.getMinX() + ((rect.getWidth() / numScales) * i) - g2d.getFontMetrics().stringWidth(String.format("%.2f", ((double) count) / 1000) + " kb") / 2), (int) rect.getMaxY() + this.getHeight() / 20 + 14);
                    count = (int) (count + length / numScales);
                }}
            } else {
               }
        }
    }

    private void drawChosenLabel(Graphics2D g2d,Query qry,Boolean isFlipped) {
        if (!chosenLabel.equals("")&!regionView) {
            // draw chosen label
            Double labelpos = 0.0;
            if (isFlipped == false) {//if it is reorientated
                labelpos = qry.getSites().get(Integer.parseInt(chosenLabel)).get(0);//get label position
                g2d.setColor(Color.red);
                g2d.drawLine((int) ((int) (labelpos / scale )),
                        230,
                        (int) (labelpos / scale ),
                        290);
                g2d.drawString(String.format("%.1f", labelpos), (int) (labelpos / scale ), 290);
            } else {
                labelpos = qry.getLength() - qry.getSites().get(Integer.parseInt(chosenLabel)).get(0);//get label position
                g2d.setColor(Color.red);
                g2d.drawLine((int) ((int) (labelpos / scale )),
                        230,
                        (int) (labelpos / scale ),
                        290);
                g2d.drawString(String.format("%.1f", qry.getLength() -labelpos), (int) (labelpos / scale ), 290);
            }


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

