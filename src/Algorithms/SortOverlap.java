package Algorithms;

import DataTypes.*;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.awt.geom.Rectangle2D;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/*
 * @author Josie
 */

/* Glynn's edit starting 09MAR2021 - with adding comments to understand WTF is going on.

 */
public class SortOverlap {
    private static MapOpticsModel model;
    private final List<Query> queries;
    /* GM: My understanding is that this class attempts to move all overlapping contigs (represented as rectangles,
    parallel to the reference map) so that they no longer overlap and can be viewed by the user.
    GM: the scale and movement of these query contigs is performed iteratively with up and down and/or left or right
    movements within a while loop with fixed vector values to move.
    GM: This process is currently very, very slow.
    GM: Look at placing timings to capture slowest parts
     */


    private Rectangle2D rectangle;

    private static Rectangle2D[] qryRects; // Rect2D array
    private static Rectangle2D[][] qryLabels; // Rect2D 2D array
    private static Double[] qryStarts;
    private static Double[] qryEnds;

    /* A linked hashmap is a hashmap where the iteration order is maintained and not random like a normal hashmap

     */
    // New linked hashmap for sortedRects
    private static LinkedHashMap<String, Rectangle2D> sortedRects = new LinkedHashMap();
    // New linked hashmap for sortedLabels
    private static LinkedHashMap<String, Rectangle2D[]> sortedLabels = new LinkedHashMap();
    private static Rectangle2D[] ListofRects;

    public SortOverlap(List<Query> queries) {
        this.queries = queries;
    }


    private ArrayList<Double> accessQueries(Query qry){

        ArrayList<Double> rectInputs = new ArrayList<>();
        // Get the parameters needed to find out where overlaps occur in alignment sites
        // Contig rectangle dimensions and current position
        Rectangle2D contig_outline = qry.getRectangle();
        // First alignment on that contig - based on Treemap so know firstEntry will be the first alignment
        Map.Entry<Integer, List<Double>> first = qry.getSites().firstEntry();
        // Last alignment on that contig - based on Treemap so know lastEntry will be the last alignment
        Map.Entry<Integer, List<Double>> last = qry.getSites().lastEntry();
        // Get values need to find first and last alignment position relevant to contig in relation to reference
        Double firstAlign = first.getValue().get(0);
        Double lastAlign = last.getValue().get(0);

        // Get in relation to contig start X position
        Double alignStart = contig_outline.getMinX()+firstAlign;
        Double alignEnd = contig_outline.getMinX()+lastAlign;

        rectInputs.add(contig_outline.getMinX());
        System.out.println("Query min Rect X = "+contig_outline);
        rectInputs.add(alignStart);
        System.out.println("Query align Start = "+alignStart);
        rectInputs.add(alignEnd);
        System.out.println("Query align End = "+alignEnd);

        return rectInputs;


    }

    public HashMap<Integer, ArrayList<Double>> getAllQueryinfo(List<Query> queries){

        // Create Map to contain contigs and information

        HashMap<Integer, ArrayList<Double>> forOverlaps = new HashMap<>();

        for(int i = 0; i< queries.size(); i++) {
            // For loop to get parameters for sorting overlaps
            Query here = queries.get(i);
            ArrayList<Double> inputs = accessQueries(here);

            forOverlaps.put(i, inputs);

        }
        return forOverlaps;

    }

    // LowestOffSetX
    // HighestOffSetY
    // Create class called Pair to take pairs of Start and End aligns

    // User defined Pair class
    public static class Pair {
        Double x;
        Double y;
        int z;

        // Constructor
        public Pair(Double x, Double y, int z)
        {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    // class to define user defined conparator
    static class Compare {

        void compare(Pair arr[], int n)
        {
            // Comparator to sort the pair according to first element
            Arrays.sort(arr, new Comparator<Pair>() {
                @Override public int compare(Pair p1, Pair p2)
                {
                    return (int) (p1.x - p2.x);
                }
            });

            for (int i = 0; i < n; i++) {
                System.out.print("AlignStart: "+ arr[i].x + " AlignEnd: " + arr[i].y + " Original Rect i: "+ arr[i].z + " ");
            }
            System.out.println();

        }
    }

    public HashMap<Integer, ArrayList<Pair>> sortingOverlaps() {

        HashMap<Integer, ArrayList<Double>> contigInfo = getAllQueryinfo(queries);
        // length of array
        int n = contigInfo.size();
        System.out.println("Number of queries is ="+n);

        // Array of Pair
        Pair rects[] = new Pair[n];

        // Add start and end of alignment values to array.
        for (Map.Entry<Integer, ArrayList<Double>> entry : contigInfo.entrySet()) {
            Integer key = entry.getKey();
            ArrayList value = entry.getValue();
            rects[key] = new Pair((Double) (value.get(1)), (Double) (value.get(2)), key);

        }
        // Initialise compare class
        Compare obj = new Compare();
        // Sort Alignments by start position (relevant to the reference)
        obj.compare(rects, n);

        HashMap<Integer, ArrayList<Pair>> overlapping = new HashMap<>();

        // Compare align end (rect1) to align start (rect 2 to n) and if align end is larger than align start
        for (int i = 0; i < rects.length; i++) {
            ArrayList<Pair> Overlaps = new ArrayList<>();

            // Add X-1 (i-1) rectangle to ArrayList in position 0 and X (i in position 1)
            // These are used for number of rectangles in an overlap "block" and to calculate space either side for
            // moving the rectangles left or rigth and down.

            if(i>0) {
                Overlaps.add(rects[i - 1]);
                Overlaps.add(rects[i]);
            }else{
                // Create a pair that represents with X, Y and Z, that is 10% left of reference contigs end
                //Pair LeftSide = new Pair(Double x, Double y, int Z) ;
                //Overlaps.add(Leftside); // Set based on model.totalRectangleWidth((highestOffSetX - lowestOffsetX));
                // And model.ref.getLength()
                // model.getSelectedRef().getLength();
                //Overlaps.add(rects[i]);
            }

            // Start working way through each pairwise comparison
            for (int j = 0; j < rects.length; j++) {
                //Compare ith rect to jth rectangle

                if (i < j) {
                    // Don't compare the rectangle to itself

                    if (rects[i].y >= rects[j].x && j < rects.length - 1) {
                        // Want to add rectangles that overlap sequentially to list
                        Overlaps.add(rects[j]);
                        System.out.println("Overlaps between rectangle i="+i+ " original rect number = "+rects[i].z+
                                " and j= "+j+ " original rect number " +rects[j].z+
                                " total overlaps: "+ Overlaps.size());

                        } else{
                        // Add surrounding rectangles for how far overlaps can move
                        if(j < rects.length-1){
                            Overlaps.add(rects[j + 1]);
                            System.out.println("Overlaps between rectangle i="+i+ " original rect number = "+rects[i].z+
                                    " and j= "+j+ " original rect number " +rects[j].z+
                                    " total overlaps: "+ Overlaps.size());
                            overlapping.put(i, Overlaps);


                          // Add max offset to the right max X offset
                        }else if(j==rects.length) {
                            // Add max offset to the right max X offset
                            overlapping.put(i, Overlaps);
                            //movingRects(Overlaps,queries);



                        }
                        movingRects(Overlaps,queries);
                        break;

                    }
                }

            }
        }
        return overlapping;
    }

// Count number of overlaps per entry in Overlapping to work out left and right moves

    public void movingRects (ArrayList<Pair> overlappingRects, List<Query> queries) {
        int numOfRects = overlappingRects.size();
        double median;
        double gapsLeft = 0;
        double gapsRight = 0;
        int numberOfQueries = queries.size();
        System.out.println("Number of queries: "+numberOfQueries);

        // If queries fewer than 20 do one type of sorting greater than 20 default sorting below

        // Find median depending on number of rects in the overlap set
        if(numOfRects%2 == 0){
             median = numOfRects+1 / 2;
        }else{
            median = numOfRects / 2;
        }


        for(int k =1; k<overlappingRects.size()-1; k++){

             if(k<median){
               // Move rectangle set left
                 // get pair class of first overlap rectangle and the one to the left
               Pair RectInterest = overlappingRects.get(k);
               Pair RectLeft = overlappingRects.get(k-1);

               // Get the Query of the two rectangles to calculate overlap move
               Query interest_qry = queries.get(RectInterest.z);
               Query left_qry = queries.get(RectLeft.z);


               // Get rectangle dimensions
               Rectangle2D interest_rect = interest_qry.getRectangle();
               Rectangle2D left_rect = left_qry.getRectangle();

               // Find gap between alignments of Pair of interest and left
                 double overlapOrGap = RectInterest.x - RectLeft.y;

                 // if overlapOrGap is positive move RectInterest to RectLeft +10
                 // Need to check if overlap is solved after move
                 if(overlapOrGap > 0) {

                     double moveLeft = overlapOrGap-10;

                     Rectangle2D new_interest_rect = new Rectangle2D.Double(interest_rect.getX()-moveLeft, interest_rect.getY() + 52,
                             interest_rect.getWidth(), interest_rect.getHeight());
                    // System.out.println("New Rectangle dimensions " + new_interest_rect);
                     interest_qry.setRectangle(new_interest_rect);
                 }else{
                     Rectangle2D new_interest_rect = new Rectangle2D.Double(interest_rect.getX(), interest_rect.getY() + 52,
                             interest_rect.getWidth(), interest_rect.getHeight());
                     //System.out.println("New Rectangle dimensions " + new_interest_rect);
                     interest_qry.setRectangle(new_interest_rect);

                 }


           }else if(k> median){
                // Move rectangle set right
                 // get pair class of first overlap rectangle and the one to the left
                 Pair RectInterest = overlappingRects.get(k);
                 Pair RectRight = overlappingRects.get(k+1);

                 // Get the Query of the two rectangles to calculate overlap move
                 Query interest_qry = queries.get(RectInterest.z);
                 Query right_qry = queries.get(RectRight.z);

                 // Get rectangle dimensions
                 Rectangle2D interest_rect = interest_qry.getRectangle();
                 Rectangle2D right_rect = right_qry.getRectangle();

                 // Find gap between alignments of Pair of interest and left
                 double overlapOrGap =  RectRight.x - RectInterest.y;

                 // if overlapOrGap is positive move RectInterest to RectLeft +10
                 // Need to check if overlap is solved after move
                 if(overlapOrGap > 0) {

                     double moveRight = overlapOrGap-10;

                     Rectangle2D new_interest_rect = new Rectangle2D.Double(interest_rect.getX()+moveRight, interest_rect.getY() + 52,
                             interest_rect.getWidth(), interest_rect.getHeight());
                     //System.out.println("New Rectangle dimensions " + new_interest_rect);
                     interest_qry.setRectangle(new_interest_rect);
                 }else{
                     Rectangle2D new_interest_rect = new Rectangle2D.Double(interest_rect.getX(), interest_rect.getY() + 52,
                             interest_rect.getWidth(), interest_rect.getHeight());
                     //System.out.println("New Rectangle dimensions " + new_interest_rect);
                     interest_qry.setRectangle(new_interest_rect);

                 }
           }else{
               // Leave rectangle as is
                 Pair RectInterest = overlappingRects.get(k);
                 Query original_qry = queries.get(RectInterest.z);
                 Rectangle2D original_rect = original_qry.getRectangle();
                // System.out.println("Original Rectangle dimensions "+original_rect);
                 Rectangle2D new_rect = new Rectangle2D.Double(original_rect.getX(), original_rect.getY()+52,
                         original_rect.getWidth(), original_rect.getHeight());
                // System.out.println("New Rectangle dimensions "+new_rect);
                 original_qry.setRectangle(new_rect);
           }

        }


    }




    /*// Getters for LinkedHashMaps
    public static LinkedHashMap<String, Rectangle2D> getSortedRects() {
        return sortedRects;
    }

    public static LinkedHashMap<String, Rectangle2D[]> getSortedLabels() {
        return sortedLabels;
    }

    //Getters for Rectangles
    public static Rectangle2D getSortedRects(String refqryId) {
        return sortedRects.get(refqryId);
    }

    public static Rectangle2D[] getSortedLabels(String refqryId) {
        return sortedLabels.get(refqryId);
    }
*/
/*    public static void sortOverlaps(LinkedHashMap<String, RefContig> references, LinkedHashMap<String, QryContig> queries, int scale)  {
    *//* sortOverlaps receives input of two LinkedHashMaps: references and queries alongside an integer for the scale
    GM: was is the impact of scale on time to load rectangles, currently hard coded.
     *//*
    // Clears current LinkedHashMaps
        sortedRects.clear();
        sortedLabels.clear();
        String[] qryIds; // Create string array for Query IDs

        for (String refId : references.keySet()) {
            // A for loop that takes a refID and gets value matching refID from references LinkedHashMap
            //.getConnections() ? -Gets a connection to a database - connections of XMAP, ref CMAP and qry CMAP?
            qryIds = references.get(refId).getConnections();
            // MoveOverlappingContigs method qryIDs related to refID, queries HashMap and scale parameter
            //moveOverlappingContigs(qryIds, refId, queries, scale);
        }
    }*/

   /* private static void moveOverlappingContigs(String[] qryIds, String refId, LinkedHashMap<String, QryContig> queries, int scale) throws IOException {

        HashMap<Integer, ArrayList<Rectangle2D>> new_Hash = setArrays(qryIds, refId, queries);

        // move left and right and up and down until they no longer overlap
        sortContigs(new_Hash, qryStarts, qryEnds);
        //moveLeftandRight(scale);
        //moveUpandDown(scale);

        // now that all rectangles no longer overlap, put them in the new hashmap of queries and labels
        String refqryId;
        for (int i = 0; i < qryIds.length; i++) {
            refqryId = refId + "-" + qryIds[i];
            sortedRects.put(refqryId, qryRects[i]);
            sortedLabels.put(refqryId, qryLabels[i]);
        }
    }*/

    private static HashMap<Integer, ArrayList<Rectangle2D>> setArrays(String[] qryIds, String refId, LinkedHashMap<String, QryContig> queries) {
        /* Creating a number of arrays and making an array of rectangles that matches the given reference

         */

        qryRects = new Rectangle2D[qryIds.length];
        System.out.println("Query Rects = "+qryRects);

        qryStarts = new Double[qryIds.length];
        qryEnds = new Double[qryIds.length];
        qryLabels = new Rectangle2D[qryIds.length][];
        HashMap<Integer, ArrayList<Rectangle2D>> queryMap = new HashMap<>();
        String refqryId;

        // Make array of rectangles that match to this reference
        for (int i = 0; i < qryIds.length; i++) {
            ArrayList<Rectangle2D> qryRectLabels = new ArrayList<>();
            refqryId = refId + "-" + qryIds[i];
            qryRects[i] = queries.get(refqryId).getRectangle().getBounds2D();
            qryRectLabels.add(qryRects[i]);
            System.out.println("Contig Rectangle = " + i+ " "+qryRectLabels);

            qryStarts[i] = queries.get(refqryId).getQryAlignStart();
            qryEnds[i] = queries.get(refqryId).getQryAlignEnd();
            Rectangle2D[] labels = new Rectangle2D[queries.get(refqryId).getLabels().length];
            for (int j = 0; j < queries.get(refqryId).getLabels().length; j++) {
                qryRectLabels.add(queries.get(refqryId).getLabels()[j].getBounds2D());
                labels[j] =queries.get(refqryId).getLabels()[j].getBounds2D();
            }


            System.out.println("Contig Rectangle = " + i+ " "+qryRectLabels);

            qryLabels[i] = labels;
            queryMap.put(i, qryRectLabels);


        }
       return queryMap;
    }

    /* GM: Adding new method with novel approach to sorting overlapping alignment areas

     */


    private static Pair[] sortContigs(HashMap<Integer, ArrayList<Rectangle2D>> QueryRectangles, Double[] QueryAlignStart, Double[] QueryAlignEnd) {
        // Take in Rectangle dimensions for contigs for a single reference and
        // add alignment start and end position as their reference positions

        // Set parameters


        int alignStart;
        int alignEnd;

        Rectangle2D rect;

       // length of array
        int n = QueryRectangles.size();

        // Array of Pair
        Pair rects[] = new Pair[n];

        // Re-write for HashMap containing at index 0 the contig rectangle and then all the labels

        for (Map.Entry<Integer, ArrayList<Rectangle2D>> entry : QueryRectangles.entrySet()) {
            Integer key = entry.getKey();
            ArrayList value = entry.getValue();
//Go through HashMap a reorder based on qryRects (legacy)
// Use contig boundary rectangle to sort any align overlaps
            Rectangle2D rect2 = (Rectangle2D) value.get(0);

            alignStart = (int) (rect2.getMinX() + qryStarts[key]);
            alignEnd = (int) (rect2.getMinX() + qryEnds[key]);

            // Add start and end of alignment values to array.
            //rects[key] = new Pair(alignStart, alignEnd, key);


        }


// Initialise compare class
        Compare obj = new Compare();
// Sort Alignments by start position (relevant to the reference)
        obj.compare(rects, n);

        // List
        HashMap<Integer, ArrayList<ArrayList<Rectangle2D>>> RectLayoutMap = new HashMap<>();
        //ArrayList<Rectangle2D> Overlaps = new ArrayList<>();
        // Compare align end (rect1) to align start (rect 2 to n) and if align end is larger than align start
        for(int i =0; i<rects.length; i++) {
            ArrayList<ArrayList<Rectangle2D>> Overlaps = new ArrayList<>();
            // Start working way through each pairwise comparison
            for(int j = 0; j< rects.length; j++) {
                //Compare ith rect to jth rectangle
                // Clear array list from last set of comparisons


                if(i<j) {
                    // Don't compare the rectangle to itself

                    if (rects[i].y >= rects[j].x && j< rects.length-1) {

                        // Can contig move right?
                       // int spaceRight = rects[j+1].y -rects[j].x;

                      /*  if(spaceRight>0){
                            // This means space to move the contig right
                            // How much does contig need to move to unoverlap?
                            int unOverlap = rects[i].y - rects[j].x;

                            if(spaceRight>unOverlap){
                                MoveDownandRight(QueryRectangles.get(rects[j].z), unOverlap);
                            }else if(spaceRight<unOverlap){
                                MoveDownandRight(QueryRectangles.get(rects[j].z), spaceRight);
                            }
                        }*/

                        Rectangle2D[] newQueryLabels = new Rectangle2D[QueryRectangles.get(rects[j].z).size()-1];

                        for(int ka = 1; ka< QueryRectangles.get(rects[j].z).size(); ka++) {
                            newQueryLabels[ka-1]= QueryRectangles.get(rects[j].z).get(ka);
                        }

                        qryRects[rects[j].z]= QueryRectangles.get(rects[j].z).get(0);

                        qryLabels[rects[j].z] = newQueryLabels;

                       // System.out.println("Before Mod"+QueryRectangles.get(rects[j].z).get(0));

                       // System.out.println("After Mod"+QueryRectangles.get(rects[j].z).get(0));



                    } else {
                        break;
                        }

                    }
                }
            }














                // Find matching rectangle based  on x value in the orginal qryRects
                /*for (int z = 0; z < qryRects.length; z++) {

                    // Loop through qryRects to find matching rectangle and replace
                    Double qryX = qryRects[z].getMinX();
                    Double changeX = newtwo.getMinX();
                    if (qryX == changeX) {
                        qryRects[z] = newtwo;
                        // Need to capture Query Labels (green and black lines) and move these too.
                        // Find queryLabels that relate to qryRect
                        System.out.println("Check Labels BEFORE changed dimension " + qryLabels[z][0]);
                        Rectangle2D[] Mod_labels = qryLabels[z];
                        for (int lab = 0; lab<Mod_labels.length; lab++) {
                            Mod_labels[lab].setRect(Mod_labels[lab].getMinX(),
                                    qryRects[z].getY(), Mod_labels[lab].getWidth(), Mod_labels[lab].getHeight());

                        }
                        qryLabels[z] = Mod_labels;
                        System.out.println("Check Rectangle has changed dimension " + qryRects[z]);
                        System.out.println("Check Labels has changed dimension " + qryLabels[z][0]);

                        System.out.println("Check Rectangle has changed dimension " + qryRects[z]);

                    }
                }*/





        return rects;

    }

    // Method for changing the bounds of rectangles to move them away from overlaps

    private static void MoveDownandRight(ArrayList<Rectangle2D> ListofRects, int farRight){

        // Only moves down at the moment
        for(int rectangle = 0; rectangle<ListofRects.size(); rectangle++){
            // Iterate through each array of Rectangle 2D (query contig and labels and move)
            ListofRects.get(rectangle).setRect(ListofRects.get(rectangle).getMinX()+farRight,
                    ListofRects.get(rectangle).getY() + 10, ListofRects.get(rectangle).getWidth(),
                    ListofRects.get(rectangle).getHeight());
        }




    }

    private static void moveLeftandRight(int scale) throws IOException {//
        // Does this need to be static?

        FileWriter fileWriter = new FileWriter("C:/git/Mastership/Group_project/SortOverlaps_log/GM_scale_chilenese.log");
        PrintWriter printWriter = new PrintWriter(fileWriter);

        long Start = System.currentTimeMillis();
        // Check for overlaps in alignment sections and move rectangles apart if they overlap
        boolean[][] overlap = new boolean[qryRects.length][qryRects.length];
        for (int i = 0; i < overlap.length; i++) {
            Arrays.fill(overlap[i], true);
        }

        // Print to file test
        printWriter.println("Glynn's log");
        printWriter.println("Length of query rects array = "+ qryRects.length);



        boolean isLeftOf;
        boolean isOverLapping;
        double alignS1;
        double alignE1;
        double alignS2;
        double alignE2;
        Rectangle2D rect1;
        Rectangle2D rect2;

        while (Arrays.deepToString(overlap).contains("true")) {
            for (int i = 0; i < qryRects.length; i++) {
                for (int j = 0; j < qryRects.length; j++) {
                    // If rectangles are not the same, check if they overlap in regions
                    if (i < j) {
                        rect1 = qryRects[i];
                        rect2 = qryRects[j];

                        printWriter.println("Rectangle Number " +i+ " vs Rectangle Number "+j);

                        // set variables for areas where there is alignment
                        // Test what happens when removing scale component
                        alignS1 = rect1.getMinX() + qryStarts[i];
                        //- (3 * scale);


                        alignE1 = rect1.getMinX() + qryEnds[i];
                        //+ (3 * scale);
                        // Print line check to understand what is happening here.
                        printWriter.println("Rect1 minX ="+rect1.getMinX());
                        printWriter.println("Query Start pos: "+ qryStarts[i]);
                        printWriter.println("Query End pos: "+ qryEnds[i]);
                        printWriter.println("alignS1 = " +alignS1);
                        printWriter.println("alignE1 = " +alignE1);
                        printWriter.println("-------------");

                        alignS2 = rect2.getMinX() + qryStarts[j];
                        //- (3 * scale);

                        printWriter.println("-------------");


                        alignE2 = rect2.getMinX() + qryEnds[j];
                                //+ (3 * scale);
                        // Print line check to understand what is happening here.
                        // Print line check to understand what is happening here.
                        printWriter.println("Rect2 minX ="+rect2.getMinX());
                        printWriter.println("Query Start pos: "+ qryStarts[j]);
                        printWriter.println("alignS2 = " +alignS2 + " iteration i =" + i+ " iteration j = "+j);
                        printWriter.println("Query End pos: "+ qryEnds[j]);
                        printWriter.println("alignE2 = " +alignE2 + " iteration i =" + i+ " iteration j = "+j);


                        isOverLapping = isOverLappingX(alignS1, alignE1, alignS2, alignE2);
                        printWriter.println("Is there an overlap between Rect #"+i+ " and Rect #"+j+ " "+isOverLapping);
                        printWriter.println("-------------");
                        if (isOverLapping) {
                            overlap[i][j] = true;
                            isLeftOf = isLeftOf(rect1.getMinX() + qryStarts[i], rect2.getMinX() + qryStarts[j]);
                            if (isLeftOf) {
                                // move 1 left and 2 right
                                moveAllLeft(rect1, i, scale);
                                moveAllRight(rect2, j, scale);
                            } else {
                                // move 2 right and 1 left
                                moveAllLeft(rect2, j, scale);
                                moveAllRight(rect1, i, scale);
                            }
                        } else {
                            overlap[i][j] = false;
                        }
                    } else {
                        overlap[i][j] = false;
                    }
                }
            }
        }
        long end = System.currentTimeMillis();
        long timeTaken = end - Start;
        printWriter.println("Time for MoveLeftRight = "+timeTaken + " with Scale: "+scale);
        printWriter.close();
    }

    private static void moveUpandDown(int scale) {
        // Check for overlaps in overall contig size and move rectangles apart if they overlap
        boolean[][] overlap = new boolean[qryRects.length][qryRects.length];
        for (int i = 0; i < overlap.length; i++) {
            Arrays.fill(overlap[i], true);
        }

        boolean isLeftOf;
        boolean isOverLapping;
        boolean overlapsAlignment1;
        boolean overlapsAlignment2;
        double alignS1;
        double alignE1;
        double alignS2;
        double alignE2;
        double start1;
        double end1;
        double start2;
        double end2;
        Rectangle2D rect1;
        Rectangle2D rect2;

        while (Arrays.deepToString(overlap).contains("true")) {
            for (int i = 0; i < qryRects.length; i++) {
                for (int j = 0; j < qryRects.length; j++) {
                    // If rectangles are not the same, check if they overlap in regions
                    if (i < j) {
                        rect1 = qryRects[i];
                        rect2 = qryRects[j];

                        // set variables for start and end of query contigs
                        start1 = rect1.getMinX() - (5 * scale);
                        end1 = rect1.getMaxX() + (5 * scale);
                        start2 = rect2.getMinX() - (5 * scale);
                        end2 = rect2.getMaxX() + (5 * scale);

                        // set variables for areas where there is alignment
                        alignS1 = rect1.getMinX() + qryStarts[i] - (5 * scale);
                        alignE1 = rect1.getMinX() + qryEnds[i] + (5 * scale);
                        alignS2 = rect2.getMinX() + qryStarts[j] - (5 * scale);
                        alignE2 = rect2.getMinX() + qryEnds[j] + (5 * scale);

                        // check if contig overlaps alignment areas
                        overlapsAlignment1 = isOverLappingX(start1, end1, alignS2, alignE2);
                        overlapsAlignment2 = isOverLappingX(start2, end2, alignS1, alignE1);

                        if (overlapsAlignment1 && overlapsAlignment2) {
                            overlap[i][j] = true;
                            // if they both overlap alignment regions, then move them further away
                            isLeftOf = isLeftOf(rect1.getMinX() + qryStarts[i], rect2.getMinX() + qryStarts[j]);
                            if (isLeftOf) {
                                // move 1 left and 2 right
                                moveAllLeft(rect1, i, 0.1 * scale);
                                moveAllRight(rect2, j, 0.1 * scale);
                            } else {
                                // move 2 right and 1 left
                                moveAllLeft(rect2, j, 0.1 * scale);
                                moveAllRight(rect1, i, 0.1 * scale);
                            }

                        } else if (overlapsAlignment1) {
                            // check if the contigs overlap at all
                            isOverLapping = isOverLappingX(start1, end1, start2, end2) && isOverLappingY(rect1.getMinY(), rect2.getMinY());
                            if (isOverLapping) {
                                overlap[i][j] = true;
                                // if they only overlap one move the overlapping one below
                                moveAllDown(rect1, i, rect1.getHeight() * 2);
                            } else {
                                overlap[i][j] = false;
                            }

                        } else if (overlapsAlignment2) {
                            // check if the contigs overlap at all
                            isOverLapping = isOverLappingX(start1, end1, start2, end2) && isOverLappingY(rect1.getMinY(), rect2.getMinY());
                            if (isOverLapping) {
                                overlap[i][j] = true;
                                // if they only overlap one move the overlapping one below
                                moveAllDown(rect2, j, rect2.getHeight() * 2);
                            } else {
                                overlap[i][j] = false;
                            }
                        } else {
                            isOverLapping = isOverLappingX(start1, end1, start2, end2) && isOverLappingY(rect1.getMinY(), rect2.getMinY());
                            if (isOverLapping) {
                                overlap[i][j] = true;
                                isLeftOf = isLeftOf(rect1.getMinX() + qryStarts[i], rect2.getMinX() + qryStarts[j]);
                                if (isLeftOf) {
                                    // move 1 left and 2 right
                                    moveAllLeft(rect1, i, 0.1 * scale);
                                    moveAllRight(rect2, j, 0.1 * scale);
                                } else {
                                    // move 2 right and 1 left
                                    moveAllLeft(rect2, j, 0.1 * scale);
                                    moveAllRight(rect1, i, 0.1 * scale);
                                }
                            } else {
                                overlap[i][j] = false;
                            }
                        }
                    } else {
                        overlap[i][j] = false;
                    }
                }
            }
        }
    }

    public static boolean isOverLappingX(double rect1start, double rect1end, double rect2start, double rect2end) {
        return (((rect1start <= rect2start) && (rect2end <= rect1end)) || ((rect2start <= rect1start) && (rect1end <= rect2end))
                || ((rect1start <= rect2start) && (rect1end >= rect2start) && (rect2end >= rect1end)) || ((rect2start <= rect1start) && (rect2end >= rect1start) && (rect1end >= rect2end)));
    }

    private static boolean isOverLappingY(double rect1top, double rect2top) {
        return (rect1top == rect2top);
    }

    private static boolean isLeftOf(double firstMinX, double otherMinX) {
        return firstMinX <= otherMinX;
    }

    private static Rectangle2D moveLeft(Rectangle2D rect, double amount) {
        rect.setRect(rect.getMinX() - amount, rect.getMinY(), rect.getWidth(), rect.getHeight());
        return rect;
    }

    private static Rectangle2D moveRight(Rectangle2D rect, double amount) {
        rect.setRect(rect.getMinX() + amount, rect.getMinY(), rect.getWidth(), rect.getHeight());
        return rect;
    }

    private static Rectangle2D moveDown(Rectangle2D rect, double amount) {
        rect.setRect(rect.getMinX(), rect.getMinY() + amount, rect.getWidth(), rect.getHeight());
        return rect;
    }

    private static void moveAllDown(Rectangle2D rect, int i, double amount) {
        Rectangle2D[] labels = qryLabels[i];
        qryRects[i] = moveDown(rect, amount);
        for (int k = 0; k < labels.length; k++) {
            labels[k] = moveDown(labels[k], amount);
            qryLabels[i] = labels;
        }
    }

    private static void moveAllLeft(Rectangle2D rect, int i, double amount) {
        Rectangle2D[] labels = qryLabels[i];
        qryRects[i] = moveLeft(rect, amount);
        for (int k = 0; k < labels.length; k++) {
            labels[k] = moveLeft(labels[k], amount);
            qryLabels[i] = labels;
        }
    }

    private static void moveAllRight(Rectangle2D rect, int i, double amount) {
        Rectangle2D[] labels = qryLabels[i];
        qryRects[i] = moveRight(rect, amount);
        for (int k = 0; k < labels.length; k++) {
            labels[k] = moveRight(labels[k], amount);
            qryLabels[i] = labels;
        }
    }

}
