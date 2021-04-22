package Algorithms;

import DataTypes.*;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.awt.geom.Rectangle2D;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;
import java.lang.Math.*;

import static java.lang.Math.ceil;
import static java.lang.Math.floor;

/*
 * @author Glynn Martin
 * SortOverlap class is designed to move query contigs in the MapOptics GUI - in Summary view and Reference View so that the user can see all the contigs and any overlaps are minimised
 * This class has been optimised to work with a range of datasets and prioritises memory usage and speed over the perfect layout of query contigs.
 * Please note the use can move query contigs to any desired locations using Reference View
 */


public class SortOverlap {
    // Defining class variables from Reference and Query classes
    private static MapOpticsModel model;
    private final List<Query> queries;
    private final Double refLength;

    public SortOverlap(List<Query> queries, Double refLength) {
        // Constructor
        this.queries = queries;
        this.refLength = refLength;
    }


    private ArrayList<Double> accessQueries(Query qry) {
        /* This method return a double ArrayList of the minimum X position of each Query contig (in relation to the Reference contig X postion),
        the position of the first alignment site between the reference and the query contig. Based on the the minimum X position plus the site position.
        Finally the last alignment site it also defined by the last alignment value being added to the minimum X position.
        These are return in the order, minimum X position, alignment start and alignment end.
         */

        ArrayList<Double> rectInputs = new ArrayList<>();
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
        Double alignStart = contig_outline.getMinX() + firstAlign;
        Double alignEnd = contig_outline.getMinX() + lastAlign;

        // Add each parameter to the ArrayList rectInputs in order
        rectInputs.add(contig_outline.getMinX());
        rectInputs.add(alignStart);
        rectInputs.add(alignEnd);

        return rectInputs;
    }

    public HashMap<Integer, ArrayList<Double>> getAllQueryinfo(List<Query> queries) {

        // Create HashMap to contain all query contigs with alignment start and end values.

        HashMap<Integer, ArrayList<Double>> QryAlignmentInfo = new HashMap<>();

        for (int i = 0; i < queries.size(); i++) {
            // For loop to access each query for the selected reference
            Query Qry = queries.get(i);
            ArrayList<Double> QryInputs = accessQueries(Qry);

            // Query information place in HashMap, key represents iteration.
            QryAlignmentInfo.put(i, QryInputs);

        }
        return QryAlignmentInfo;

    }


    private static class Pair {
        // The Pair Class creates a Pair Array containing information pertaining to the start alignment,
        // end alignment and the original position of the rectangle in order of the minimum X position.
        Double x;
        Double y;
        int z;

        // Constructor
        private Pair(Double x, Double y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }

    // class to define user defined conparator
    static class Compare {
        /* This method sorts the Pair Arrays into order of the smallest first alignment site to the largest first alignment site.
        Meaning all alignments are ordered.
         */

        void compare(Pair arr[], int n) {
            // Comparator to sort the pair according to first element
            Arrays.sort(arr, (p1, p2) -> (int) (p1.x - p2.x));

        }
    }

    public void sortingOverlaps() {
        /* This is the main method of SortOverlaps - sortingOverlaps.
        This uses the other methods in the SortOverlaps class to obtain the overlapping alignment sets in sequential order from
        smallest to largest.
        Each set of overlapping alignment regions are captured in an ArrayList of Pairs which are then sorted by one of three methods.
        The alignments pass through a nested for loop moving through the ArrayList of Pairs and comparing each pair by the end alignment of
        the first rectangle and the start alignment or the second rectangle.
        The query contigs are then moved accordingly to produce minimal overlapping query contigs on the Summary and Reference View.
         */

        // HashMap containing all the individual Query parameters for the selected reference
        HashMap<Integer, ArrayList<Double>> contigInfo = getAllQueryinfo(queries);
        // Length of Pair array required
        int n = contigInfo.size();

        // Array of Pair
        Pair rects[] = new Pair[n];

        // Add start and end of alignment values to array.
        for (Map.Entry<Integer, ArrayList<Double>> entry : contigInfo.entrySet()) {
            Integer key = entry.getKey();
            ArrayList value = entry.getValue();
            // Index 1 is the alignment start and Index 2 is the alignment end. (Index 0 is the minimum X position of the query contig)
            rects[key] = new Pair((Double) (value.get(1)), (Double) (value.get(2)), key);

        }

        // Initialise compare class
        Compare obj = new Compare();
        // Sort Alignments by start position (relevant to the reference)
        obj.compare(rects, n);

        //HashMap<Integer, ArrayList<Pair>> overlapping = new HashMap<>();

        if (n >= 20) {

            // Compare align end (rects[i]) to align start (rects[i+1]..[i+n]) and if align end is larger than align start
            // add to ArrayList of Pairs - Overlaps.
            for (int i = 0; i < rects.length; i++) {
                ArrayList<Pair> Overlaps = new ArrayList<>();

                // Add X-1 (i-1) rectangle to ArrayList in position 0 and X (i in position 1)
                // These are used for number of rectangles in an overlap "block" and to calculate space either side for
                // moving the rectangles left or right and down.

                if (i > 0) {
                    Overlaps.add(rects[i - 1]);
                    Overlaps.add(rects[i]);
                } else {
                    // Create a pair that represents with X, Y and Z, that is 10% left of reference contigs end
                    //Pair LeftSide = new Pair(Double x, Double y, int Z) ;
                    //Overlaps.add(Leftside); // Set based on model.totalRectangleWidth((highestOffSetX - lowestOffsetX));
                    // And model.ref.getLength()
                    // model.getSelectedRef().getLength();
                    Overlaps.add(rects[i]);
                }

                // Start working way through each pairwise comparison
                for (int j = 0; j < rects.length; j++) {
                    //Compare ith rect to jth rectangle

                    if (i < j) {
                        // Don't compare the rectangle to itself

                        if (rects[i].y >= rects[j].x && j < rects.length - 1) {
                            // Want to add rectangles that overlap sequentially to list
                            Overlaps.add(rects[j]);
                        } else {
                            // Add surrounding rectangles for how far overlaps can move
                            if (j < rects.length - 1) {
                                Overlaps.add(rects[j + 1]);

                                } else if (j == rects.length) {
                                // Add max offset to the right max X offset

                                //movingRects20(Overlaps,queries);

                            }
                            // If number of contigs over 20:
                            movingRects20plus(Overlaps, queries);

                            break;

                        }
                    }

                }
            }
        } else {
            // If number of contigs under 20:
            // Set variables for query length and combined query lengths
            double QryLength;
            double TotalQryLength =0;

            for(int Qry = 0; Qry <queries.size(); Qry++){
                // A for loop to return sum of query lengths.
                QryLength = queries.get(Qry).getLength();
                TotalQryLength = TotalQryLength+QryLength;
            }

            if (TotalQryLength> refLength*0.8) {
                // Set threshold for choice of moving overlapping query contigs as 80% of the reference contig length.
                // If greater than 80%:
                sumAlignRegionsOverlaps(rects);
            }else{
                // If less than 80%:
                /* Set scales based on the 0.1% of reference length for move left and right
                and hard scale of 10,000 for up and down
                */

               int pointOnePercentRef = (int) (refLength/1000);
               int pointZeroOnePercentRef =(int) (refLength/10000);

               moveLeftandRight(pointOnePercentRef);
               moveUpandDown(pointZeroOnePercentRef);

            }


        }

    }


    // New method for capturing overlap sections based on adding to list whilst overlaps exist and moveable position
    // is negative. When this becomes positive (by adding additional sequential rectangles) move to movingRect20minus method

    public void sumAlignRegionsOverlaps(Pair[] SequentialRects) {
        //
        double sumOfGaps = 0;
        double halfOfGaps =0;
        double median;
        int countUnderMedian = 0;
        int countOverMedian = 0;
       ArrayList<Pair> overlappingRects = new ArrayList<>();

        // Take gaps between alignment sections and add sumOfGaps. If sumOfGaps is negative keeping looping
        // until sumOfGaps is positive or all queries are accounted for. When sumOfGaps turns positive
        // get the difference between the positive sumOfGaps and the last negative value?
        // Then take this set of queries and move left and right based on the median method tried earlier

        // Work through rectangles pairs, comparing alignment start to centre of reference.
        // Then decide which way to move rectangles and which rectangle to move

        for (int i = 1; i < SequentialRects.length; i++) {

            double gapDifference = SequentialRects[i].x - SequentialRects[i - 1].y;

            // This gives total gaps and overlaps in contigs
            sumOfGaps = sumOfGaps + gapDifference;
        }

        // Half sumOfGaps provide pull left and pull right
        double halfSumofGaps = sumOfGaps / 2;


        double numOfContigstoMove = SequentialRects.length - 2;

        if (numOfContigstoMove % 2 == 0) {
            median = ((numOfContigstoMove + 1) / 2); // for zero indexing purposes
        } else {
            median = (numOfContigstoMove / 2);
        }

        for(
                int rec = 1;
                rec <median;rec++)

        {
            // Move rectangle set left
            // get pair class of first overlap rectangle and the one to the left
            Pair RectInterest = SequentialRects[rec];
            Pair RectLeft = SequentialRects[rec - 1];

            // Find gap between alignments of Pair of interest and left

            double overlapsHere = RectInterest.x - RectLeft.y;
            if (overlapsHere < 0) {

                double MoveLeft = halfSumofGaps - overlapsHere;
                halfSumofGaps = halfSumofGaps - overlapsHere;
                countUnderMedian++;

                // Move rectangle
                getAndSetQryRect(queries, RectInterest, MoveLeft, true, countUnderMedian);
            }
        }

        halfSumofGaps = sumOfGaps / 2;

        for(
                int rec = SequentialRects.length - 2;
                rec >median;rec--) {


            // get pair class of first overlap rectangle and the one to the right
            Pair RectInterest = SequentialRects[rec];
            Pair RectRight = SequentialRects[rec + 1];

            // Find gap between alignments of Pair of interest and left

            double overlapsHere = RectInterest.y - RectRight.x;

            if (overlapsHere > 0) {

                double MoveRight = -halfSumofGaps - overlapsHere;
                halfSumofGaps = halfSumofGaps + overlapsHere;
                countOverMedian++;

                // Move rectangle
                getAndSetQryRect(queries, RectInterest, MoveRight, true, countOverMedian);
            }
        }

        halfSumofGaps =sumOfGaps/2;
        //Move rectangle index 0 the halfSumOfGaps distance to the left
        Pair RectFarLeft = SequentialRects[0];
        Pair RectFarRight = SequentialRects[SequentialRects.length - 1];

        if(halfSumofGaps<0)

        {
            getAndSetQryRect(queries, RectFarLeft, halfSumofGaps, false, 1);
            getAndSetQryRect(queries, RectFarRight, -halfSumofGaps, false, 1);
        }




        }


// Count number of overlaps per entry in Overlapping to work out left and right moves

    public void movingRects20plus (ArrayList<Pair> overlappingRects, List<Query> queries) {
        int numOfRects = overlappingRects.size();
        double median;
        double gapsLeft = 0;
        double gapsRight = 0;

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
                             interest_qry.setRectangle(new_interest_rect);
                 }else{
                    Rectangle2D new_interest_rect = new Rectangle2D.Double(interest_rect.getX(), interest_rect.getY() + 52,
                             interest_rect.getWidth(), interest_rect.getHeight());
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
                             interest_qry.setRectangle(new_interest_rect);
                 }else{
                     Rectangle2D new_interest_rect = new Rectangle2D.Double(interest_rect.getX(), interest_rect.getY() + 52,
                             interest_rect.getWidth(), interest_rect.getHeight());
                             interest_qry.setRectangle(new_interest_rect);

                 }
           }else{
               // Leave rectangle as is
                 Pair RectInterest = overlappingRects.get(k);
                 Query original_qry = queries.get(RectInterest.z);
                 Rectangle2D original_rect = original_qry.getRectangle();
                 Rectangle2D new_rect = new Rectangle2D.Double(original_rect.getX(), original_rect.getY()+52,
                         original_rect.getWidth(), original_rect.getHeight());
                         original_qry.setRectangle(new_rect);
           }

        }


    }

    private void getAndSetQryRect(List<Query> queries, Pair p1, double xMove, boolean yMove, double yMultiplier) {

        Query MovingQry = queries.get(p1.z);

        // Get rectangle dimensions
        Rectangle2D MovingRect = MovingQry.getRectangle();

        if(yMove){

           double yChange = MovingRect.getHeight()*yMultiplier+2;
            //Move these rectangles
            Rectangle2D newMovingRect = new Rectangle2D.Double(MovingRect.getX()+xMove,
                    MovingRect.getY()+yChange,
                    MovingRect.getWidth(), MovingRect.getHeight());
            // Set rectangles
            MovingQry.setRectangle(newMovingRect);
        }else {

            //Move these rectangles
            Rectangle2D newMovingRect = new Rectangle2D.Double(MovingRect.getX() + xMove, MovingRect.getY(),
                    MovingRect.getWidth(), MovingRect.getHeight());
            // Set rectangles
            MovingQry.setRectangle(newMovingRect);
        }


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

    private void moveLeftandRight(int scale) {//

        // Check for overlaps in alignment sections and move rectangles apart if they overlap
        boolean[][] overlap = new boolean[queries.size()][queries.size()];
        for (int i = 0; i < overlap.length; i++) {
            // Fill a queries by queries matrix with true values - true represent overlap and
            // the complete matrix must change to false before plotting
            Arrays.fill(overlap[i], true);
        }

        boolean isLeftOf;
        boolean isOverLapping;
        double alignS1;
        double alignE1;
        double alignS2;
        double alignE2;
        Rectangle2D rect1;
        Rectangle2D rect2;

        while (Arrays.deepToString(overlap).contains("true")) {
            for (int i = 0; i < queries.size(); i++) {
                for (int j = 0; j < queries.size(); j++) {
                    // If rectangles are not the same, check if they overlap in regions
                    if (i < j) {
                        Query Qry1 = queries.get(i);
                        Query Qry2 = queries.get(j);

                        // Get rectangle dimensions
                        rect1 = Qry1.getRectangle();
                        rect2 = Qry2.getRectangle();

                        // First alignment site
                        Map.Entry<Integer, List<Double>> firstSiteQ1 = Qry1.getSites().firstEntry();
                        // Last alignment on that contig - based on Treemap so know lastEntry will be the last alignment
                        Map.Entry<Integer, List<Double>> lastSiteQ1 = Qry1.getSites().lastEntry();
                        // Get values need to find first and last alignment position relevant to contig in relation to reference
                        Double firstAlignQ1 = firstSiteQ1.getValue().get(0);
                        Double lastAlignQ1 = lastSiteQ1.getValue().get(0);

                        // First alignment site
                        Map.Entry<Integer, List<Double>> firstSiteQ2 = Qry2.getSites().firstEntry();
                        // Last alignment on that contig - based on Treemap so know lastEntry will be the last alignment
                        Map.Entry<Integer, List<Double>> lastSiteQ2 = Qry2.getSites().lastEntry();
                        // Get values need to find first and last alignment position relevant to contig in relation to reference
                        Double firstAlignQ2 = firstSiteQ2.getValue().get(0);
                        Double lastAlignQ2 = lastSiteQ2.getValue().get(0);

                        // set variables for areas where there is alignment
                        // Test what happens when removing scale component
                        alignS1 = rect1.getMinX() + firstAlignQ1- (3 * scale);
                        alignE1 = rect1.getMinX() +lastAlignQ1+ (3 * scale);
                        alignS2 = rect2.getMinX() + firstAlignQ2- (3 * scale);
                        alignE2 = rect2.getMinX() + lastAlignQ1+ (3 * scale);

                        isOverLapping = isOverLappingX(alignS1, alignE1, alignS2, alignE2);

                        if (isOverLapping) {
                            overlap[i][j] = true;
                            isLeftOf = isLeftOf(rect1.getMinX() +firstAlignQ1, rect2.getMinX() + firstAlignQ2);
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

    }

    private void moveUpandDown(int scale) {

        // Check for overlaps in alignment sections and move rectangles apart if they overlap
        boolean[][] overlap = new boolean[queries.size()][queries.size()];
        for (int i = 0; i < overlap.length; i++) {
            // Fill a queries by queries matrix with true values - true represent overlap and
            // the complete matrix must change to false before plotting
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
            for (int i = 0; i < queries.size(); i++) {
                for (int j = 0; j < queries.size(); j++) {
                    // If rectangles are not the same, check if they overlap in regions
                    if (i < j) {
                        Query Qry1 = queries.get(i);
                        Query Qry2 = queries.get(j);

                        // Get rectangle dimensions
                        rect1 = Qry1.getRectangle();
                        rect2 = Qry2.getRectangle();

                        // set variables for start and end of query contigs
                        start1 = rect1.getMinX() - (5 * scale);
                        end1 = rect1.getMaxX() + (5 * scale);
                        start2 = rect2.getMinX() - (5 * scale);
                        end2 = rect2.getMaxX() + (5 * scale);


                        // First alignment site
                        Map.Entry<Integer, List<Double>> firstSiteQ1 = Qry1.getSites().firstEntry();
                        // Last alignment on that contig - based on Treemap so know lastEntry will be the last alignment
                        Map.Entry<Integer, List<Double>> lastSiteQ1 = Qry1.getSites().lastEntry();
                        // Get values need to find first and last alignment position relevant to contig in relation to reference
                        Double firstAlignQ1 = firstSiteQ1.getValue().get(0);
                        Double lastAlignQ1 = lastSiteQ1.getValue().get(0);

                        // First alignment site
                        Map.Entry<Integer, List<Double>> firstSiteQ2 = Qry2.getSites().firstEntry();
                        // Last alignment on that contig - based on Treemap so know lastEntry will be the last alignment
                        Map.Entry<Integer, List<Double>> lastSiteQ2 = Qry2.getSites().lastEntry();
                        // Get values need to find first and last alignment position relevant to contig in relation to reference
                        Double firstAlignQ2 = firstSiteQ2.getValue().get(0);
                        Double lastAlignQ2 = lastSiteQ2.getValue().get(0);

                        // set variables for areas where there is alignment
                        alignS1 = rect1.getMinX() + firstAlignQ1 - (5 * scale);
                        alignE1 = rect1.getMinX() + lastAlignQ1 + (5 * scale);
                        alignS2 = rect2.getMinX() + firstAlignQ2 - (5 * scale);
                        alignE2 = rect2.getMinX() +  lastAlignQ2 + (5 * scale);

                        // check if contig overlaps alignment areas
                        overlapsAlignment1 = isOverLappingX(start1, end1, alignS2, alignE2);
                        overlapsAlignment2 = isOverLappingX(start2, end2, alignS1, alignE1);

                        if (overlapsAlignment1 && overlapsAlignment2) {
                            overlap[i][j] = true;
                            // if they both overlap alignment regions, then move them further away
                            isLeftOf = isLeftOf(rect1.getMinX() + firstAlignQ1 , rect2.getMinX() + firstAlignQ2);
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
                                isLeftOf = isLeftOf(rect1.getMinX() + firstAlignQ1, rect2.getMinX() + firstAlignQ2);
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

    private Rectangle2D moveLeft(Rectangle2D rect, double amount) {
        Rectangle2D newMovingRect = new Rectangle2D.Double(rect.getX()-amount,
                rect.getMinY(),rect.getWidth(),rect.getHeight());

        return newMovingRect;
    }

    private Rectangle2D moveRight(Rectangle2D rect, double amount) {

        Rectangle2D newMovingRect = new Rectangle2D.Double(rect.getX()+amount,
                rect.getMinY(),rect.getWidth(),rect.getHeight());

  return newMovingRect;



    }

    private Rectangle2D moveDown(Rectangle2D rect, double amount) {

        Rectangle2D newMovingRect = new Rectangle2D.Double(rect.getX(),
                rect.getMinY()+amount,rect.getWidth(),rect.getHeight());

        return newMovingRect;

    }

    private void moveAllDown(Rectangle2D rect, int i, double amount) {
        Query Moving = queries.get(i);
        Moving.setRectangle(moveDown(rect, amount));
    }

    private void moveAllLeft(Rectangle2D rect, int i, double amount) {
        Query Moving = queries.get(i);
        Moving.setRectangle(moveLeft(rect, amount));
    }

    private void moveAllRight(Rectangle2D rect, int i, double amount) {

        Query Moving = queries.get(i);
        Moving.setRectangle(moveRight(rect, amount));

    }

}
