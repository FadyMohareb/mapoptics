package UserInterface;

import Algorithms.DetectSV;
import DataTypes.*;
import Datasets.Default.QueryViewData;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
 * @author Anisha
 *
 * */

public class SVView extends JPanel {
    public static List<Indel> qryIndels;
    private static String chosenRef = "";
    private static String chosenQry = "";
    private static List<SV> svList;
    private final MapOpticsModel model;
    private static final Color LIGHT_GREY = new Color(244, 244, 244);
    private static final Color GREY = new Color(192, 192, 192);
    private static final Color DARK_GREY = new Color(80, 80, 80);
    private static final Color BLACK = new Color(30, 30, 30);
    private static final Color GREEN = new Color(97, 204, 10);
    private static String chosenLabel = "";
    private static SV chosenSV;
    private static String labelStyle = "match";
    private static boolean confidenceView = false;
    private static boolean regionView = false;
    private static boolean referenceViewSelect = false;
    private static boolean qryViewSelect = false;
    private static String position = "";
    private static int mouseX = 0;
    private static int mouseY = 0;
    private final DetectSV detectSV;
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
    private static boolean cigarDisplay = false;
    private List<Integer> refalignments ;
    private boolean isFlipped;
    private static boolean refSequences = false;
    private static boolean qrySequences = false;
    private static boolean changeOrientation = false;
    private static boolean allIndels = false;

    public SVView(MapOpticsModel model, DetectSV detectSV) {
        this.model = model;
        this.detectSV = detectSV;
        initComponents();

    }

    public static void setChosenQry(String chosenQry) {
        SVView.chosenQry = chosenQry;
    }

    public static String getChosenQry() {
        return chosenQry;
    }

    public static void setChosenRef(String chosenRef) {
        SVView.chosenRef = chosenRef;
    }

    public static void setSVList(List<SV> svList) {
        SVView.svList = svList;
    }

    public static void setStyle(String style) {
        SVView.labelStyle = style;
    }

    public static void setRefDataset(String refDataset) {
    }

    public static void setQryDataset(String qryDataset) {
    }

    public static void setIndels(DetectSV detectSV) {
        List<Indel> indelList;
        List<Indel> indels = detectSV.getIndels();
        indelList = indels.stream().filter(indel -> indel.qryID.equals(chosenQry)).collect(Collectors.toList());
        SVView.qryIndels = indelList;
    }

    public static List<Indel> getIndels() {
        return qryIndels;
    }

    public static void setAllIndels(Boolean allIndels) {
        SVView.allIndels = allIndels;
    }

    public SV getChosenSV() {
        return chosenSV;
    }

    public static void setChosenSV(SV sv) {
        SVView.chosenSV = sv;
    }

    public static void resetChosenSV() {SVView.chosenSV = null; }

    public static String getStyle() {
        return labelStyle;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.white);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Stroke defaultStroke = g2d.getStroke();
        try {
            if (!"".equals(chosenQry) && !model.getSelectedRef().getDelQryIDs().contains(chosenQry)) {
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
                Reference ref = null ;
                Query qry = null;
                refalignments = new ArrayList();
                // refOffX=this.getWidth() / 20;// the minX of refrectangle
                refOffX=0;

                // draw relative to query alignment
                ref = model.getSelectedRef();
                qry = ref.getQuery(chosenQry);



                if (qry != null) {
                    //check the reorientation
                    isFlipped = qry.isFlipped();//to see whether the button is clicked
                    if(qry.getOrientation().equals("-")){
                        if (!isFlipped){
                            qry.reOrientate();
                            isFlipped = qry.isFlipped();
                            repaint();
                        }
                    }
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
                    //cigar
                    String hitEnum = qry.getHitEnum();
                    Cigar cigar = new Cigar(hitEnum);
                    cigar.parseHitEnum();
                    // Extract aligned ref sites with selected qry
                    List<Integer> qryRefSites = qryAlignments.values().stream().flatMapToInt(
                            refSite -> refSite.stream().mapToInt(i -> i)).boxed().sorted().collect(Collectors.toList());
                    cigar.mapCigSites(refSites, qrySites, qryAlignments, qry.getOrientation());
                    Map<Integer, String> refCig = cigar.getCigRefSites();
                    Map<Integer, String> qryCig = cigar.getCigQrySites();
                    // For each query, draw sites and alignments

                    int qryOffSetY = (int) qryScaled.getY();
                    int qryHeight = (int) qryScaled.getHeight();

                    // draw SV if selected
                    if (chosenSV != null && chosenSV instanceof Indel) {
                        // Set Polygon for indel region
                        Polygon polygon;
                        if (allIndels) {
                            for (Indel indel : SVView.getIndels()) {
                                if (isFlipped) {
                                    int qryPos1X = (int) (((qry.getLength() - indel.qryEndPos) / scale) + refOffX + regionOffX);
                                    int qryPos1Y = (int) qryOffSetY + qryHeight;
                                    int qryPos2X = (int) (((qry.getLength() - indel.qryStartPos) / scale) + refOffX + regionOffX);
                                    int qryPos2Y = (int) qryOffSetY + qryHeight;
                                    int refPos1X = (int) ((indel.refStartPos / scale) + refOffX - refx);
                                    int refPos1Y = (int) (refScaled.getMinY());
                                    int refPos2X = (int) ((indel.refEndPos / scale) + refOffX - refx);
                                    int refPos2Y = (int) (refScaled.getMinY());
                                    int[] xVals = {qryPos1X, qryPos2X, refPos1X, refPos2X};
                                    int[] yVals = {(int) qryScaled.getMinY(), (int) qryScaled.getMinY(),
                                            (int) refScaled.getMaxY(), (int) refScaled.getMaxY()};
                                    polygon = new Polygon(xVals, yVals, 4);
                                    drawSVRegion(g2d, polygon, indel);
                                } else {
                                    int qryPos1X = (int) (((indel.qryStartPos) / scale) + refOffX + regionOffX);
                                    int qryPos1Y = (int) qryOffSetY + qryHeight;
                                    int qryPos2X = (int) (((indel.qryEndPos) / scale) + refOffX + regionOffX);
                                    int qryPos2Y = (int) qryOffSetY + qryHeight;
                                    int refPos1X = (int) ((indel.refStartPos / scale) + refOffX - refx);
                                    int refPos1Y = (int) (refScaled.getMinY());
                                    int refPos2X = (int) ((indel.refEndPos / scale) + refOffX - refx);
                                    int refPos2Y = (int) (refScaled.getMinY());
                                    int[] xVals = {qryPos1X, qryPos2X, refPos2X, refPos1X};
                                    int[] yVals = {(int) qryScaled.getMinY(), (int) qryScaled.getMinY(),
                                            (int) refScaled.getMaxY(), (int) refScaled.getMaxY()};
                                    polygon = new Polygon(xVals, yVals, 4);
                                    drawSVRegion(g2d, polygon, indel);
                                }
                            }
                        } else {
                            if (chosenSV instanceof Indel) {
                                if (isFlipped) {
                                    int qryPos1X = (int) (((qry.getLength() - chosenSV.qryEndPos) / scale) + refOffX + regionOffX);
                                    int qryPos1Y = (int) qryOffSetY + qryHeight;
                                    int qryPos2X = (int) (((qry.getLength() - chosenSV.qryStartPos) / scale) + refOffX + regionOffX);
                                    int qryPos2Y = (int) qryOffSetY + qryHeight;
                                    int refPos1X = (int) ((chosenSV.refStartPos / scale) + refOffX - refx);
                                    int refPos1Y = (int) (refScaled.getMinY());
                                    int refPos2X = (int) ((chosenSV.refEndPos / scale) + refOffX - refx);
                                    int refPos2Y = (int) (refScaled.getMinY());
                                    int[] xVals = {qryPos1X, qryPos2X, refPos1X, refPos2X};
                                    int[] yVals = {(int) qryScaled.getMinY(), (int) qryScaled.getMinY(),
                                            (int) refScaled.getMaxY(), (int) refScaled.getMaxY()};
                                    polygon = new Polygon(xVals, yVals, 4);
                                    drawSVRegion(g2d, polygon, chosenSV);
                                } else {
                                    int qryPos1X = (int) (((chosenSV.qryStartPos) / scale) + refOffX + regionOffX);
                                    int qryPos1Y = (int) qryOffSetY + qryHeight;
                                    int qryPos2X = (int) (((chosenSV.qryEndPos) / scale) + refOffX + regionOffX);
                                    int qryPos2Y = (int) qryOffSetY + qryHeight;
                                    int refPos1X = (int) ((chosenSV.refStartPos / scale) + refOffX - refx);
                                    int refPos1Y = (int) (refScaled.getMinY());
                                    int refPos2X = (int) ((chosenSV.refEndPos / scale) + refOffX - refx);
                                    int refPos2Y = (int) (refScaled.getMinY());
                                    int[] xVals = {qryPos1X, qryPos2X, refPos2X, refPos1X};
                                    int[] yVals = {(int) qryScaled.getMinY(), (int) qryScaled.getMinY(),
                                            (int) refScaled.getMaxY(), (int) refScaled.getMaxY()};
                                    polygon = new Polygon(xVals, yVals, 4);
                                    drawSVRegion(g2d, polygon, chosenSV);
                                }
                            }
                        }
                        }


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
                        // in cigar mode color ref labels blue if its a deletion or green if its a match
                        if (getStyle().equals("cigar")) {

                            if (refCig.containsKey(site)) {
                                if (refCig.get(site).equals("D")) {
                                    g2d.setColor(Color.BLUE);
                                } else if (refCig.get(site).equals("M")) {
                                    g2d.setColor(GREEN);
                                } else {
                                    g2d.setColor(BLACK);
                                }
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


                    // draw query contig
                    qry.setQryViewRect(new Rectangle2D.Double(refOffX+regionOffX, 230, qryRect.getWidth()/ scale, qryRect.getHeight()));
                    drawContig(g2d, qryScaled, chosenQry);
                            // draw qry labels


                    for (int site : qry.getQryViewSites().keySet()) {
                        boolean match = false;
                        if (qryAlignments.containsKey(site)) {
                            match = true;
                            g2d.setColor(GREEN);
                        } else {
                            g2d.setColor(BLACK);
                        }
                        // if cigar style selected color insertion labels red and matches green
                        if (getStyle().equals("cigar")) {
                            if (qryCig.containsKey(site)) {
                                if (qryCig.get(site).equals("I")) {
                                    g2d.setColor(Color.RED);
                                } else if (qryCig.get(site).equals("M")) {
                                    g2d.setColor(GREEN);
                                } else {
                                    g2d.setColor(BLACK);
                                }
                            } else {
                                g2d.setColor(BLACK);
                            }
                        }
                        int position;
                       position = (int) ((qrySites.get(site) / scale) + refOffX+ regionOffX);


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
                    //display the icon
                    if(isFlipped) {
                        int[] xval = {this.getWidth()-130,this.getWidth()-110,this.getWidth()-130,this.getWidth()-110};
                        int[] yval = {10,10,35,35};
                        g2d.setColor(Color.GREEN.darker());
                        g2d.fillPolygon(xval,yval,4);
                        Font font = new Font("Tahoma", Font.ITALIC, 12);
                        g2d.setFont(font);
                        g2d.drawString("NEG", this.getWidth() - 155, 25);

                    }


                } else {
                    if (svList.isEmpty()) {
                        Font font = new Font("Tahoma", Font.ITALIC, 12);
                        g2d.setFont(font);
                        g2d.drawString("No indels found", this.getWidth() / 2 - 115, this.getHeight() / 2);
                    } else {
                        Font font = new Font("Tahoma", Font.ITALIC, 12);
                        g2d.setFont(font);
                        g2d.drawString("Choose a SV from STRUCTURAL VARIANTS table", this.getWidth() / 2 - 115, this.getHeight() / 2 - 10);
                    }
                }
            } else if (!"".equals(chosenRef)) {
                if (svList.isEmpty()) {
                    Font font = new Font("Tahoma", Font.ITALIC, 12);
                    g2d.setFont(font);
                    g2d.drawString("No indels found", this.getWidth() / 2 - 115, this.getHeight() / 2);
                } else {
                    Font font = new Font("Tahoma", Font.ITALIC, 12);
                    g2d.setFont(font);
                    g2d.drawString("Choose a SV from STRUCTURAL VARIANTS table", this.getWidth() / 2 - 115, this.getHeight() / 2);
                }

            } else {
                // if both query and ref are empty
                Font font = new Font("Tahoma", Font.ITALIC, 12);
                g2d.setFont(font);
                g2d.drawString("Choose a Reference contig from SUMMARY VIEW", this.getWidth() / 2 - 115, this.getHeight() / 2);
            }

        } catch (Exception e) {
            super.paintComponent(g);
            this.setBackground(Color.white);
            Font font = new Font("Tahoma", Font.ITALIC, 12);
            g2d.setColor(Color.red);
            g2d.setFont(font);
            e.printStackTrace(); // test for errors
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

        }else{
            Start=0;
            End=ref.getLength();
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
            if(Start!=0.0){ x=Start/scale-refx;
            }else{
                x=-refx;
            }
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



    public static void setRegionscale(String[] regions){
        SVView.regions=regions;
    }

    public static void drawSVRegion(Graphics2D g2d, Polygon polygon, SV chosenSV) {
        // create polygon from boundary positions of SV
        if (chosenSV instanceof Indel) {
            g2d.draw(polygon);
            if (chosenSV.type.equals("insertion")) {
                g2d.setColor(new Color(250, 128, 114));
                for (int i = 1; i <= 10; i++) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i * 0.1f));
                    g2d.fill(polygon);
                }
            } else {
                g2d.setColor(new Color(176, 224, 230));
                for (int i = 1; i <= 10; i++) {
                    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, i * 0.1f));
                    g2d.fill(polygon);
                }

            }

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
