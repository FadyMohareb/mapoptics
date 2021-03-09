package Algorithms;

import DataTypes.QryContig;
import DataTypes.RefContig;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import java.util.LinkedHashMap;

/*
 * @author Josie
 */

/* Glynn's edit starting 09MAR2021 - with adding comments to understand WTF is going on.

 */
public class SortOverlap {
    /* GM: My understanding is that this class attempts to move all overlapping contigs (represented as rectangles,
    parallel to the reference map) so that they no longer overlap and can be viewed by the user.
    GM: the scale and movement of these query contigs is performed iteratively with up and down and/or left or right
    movements within a while loop with fixed vector values to move.
    GM: This process is currently very, very slow.
     */

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

    // Getters for LinkedHashMaps
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

    public static void sortOverlaps(LinkedHashMap<String, RefContig> references, LinkedHashMap<String, QryContig> queries, int scale) {
    /* sortOverlaps receives input of two LinkedHashMaps: references and queries alongside an integer for the scale

     */
    // Clears current LinkedHashMaps
        sortedRects.clear();
        sortedLabels.clear();
        String[] qryIds; // Create string array for Query IDs

        for (String refId : references.keySet()) {
            qryIds = references.get(refId).getConnections();
            moveOverlappingContigs(qryIds, refId, queries, scale);
        }
    }

    private static void moveOverlappingContigs(String[] qryIds, String refId, LinkedHashMap<String, QryContig> queries, int scale) {

        setArrays(qryIds, refId, queries);

        // move left and right and up and down until they no longer overlap
        moveLeftandRight(scale);
        moveUpandDown(scale);

        // now that all rectangles no longer overlap, put them in the new hashmap of queries and labels
        String refqryId;
        for (int i = 0; i < qryIds.length; i++) {
            refqryId = refId + "-" + qryIds[i];
            sortedRects.put(refqryId, qryRects[i]);
            sortedLabels.put(refqryId, qryLabels[i]);
        }
    }

    private static void setArrays(String[] qryIds, String refId, LinkedHashMap<String, QryContig> queries) {

        qryRects = new Rectangle2D[qryIds.length];
        qryStarts = new Double[qryIds.length];
        qryEnds = new Double[qryIds.length];
        qryLabels = new Rectangle2D[qryIds.length][];
        String refqryId;

        // Make array of rectangles that match to this reference
        for (int i = 0; i < qryIds.length; i++) {
            refqryId = refId + "-" + qryIds[i];
            qryRects[i] = queries.get(refqryId).getRectangle().getBounds2D();
            qryStarts[i] = queries.get(refqryId).getQryAlignStart();
            qryEnds[i] = queries.get(refqryId).getQryAlignEnd();
            Rectangle2D[] labels = new Rectangle2D[queries.get(refqryId).getLabels().length];
            for (int j = 0; j < queries.get(refqryId).getLabels().length; j++) {
                labels[j] = queries.get(refqryId).getLabels()[j].getBounds2D();
            }
            qryLabels[i] = labels;
        }
    }

    private static void moveLeftandRight(int scale) {
        // Check for overlaps in alignment sections and move rectangles apart if they overlap
        boolean[][] overlap = new boolean[qryRects.length][qryRects.length];
        for (int i = 0; i < overlap.length; i++) {
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
            for (int i = 0; i < qryRects.length; i++) {
                for (int j = 0; j < qryRects.length; j++) {
                    // If rectangles are not the same, check if they overlap in regions
                    if (i < j) {
                        rect1 = qryRects[i];
                        rect2 = qryRects[j];

                        // set variables for areas where there is alignment
                        alignS1 = rect1.getMinX() + qryStarts[i] - (3 * scale);
                        alignE1 = rect1.getMinX() + qryEnds[i] + (3 * scale);
                        alignS2 = rect2.getMinX() + qryStarts[j] - (3 * scale);
                        alignE2 = rect2.getMinX() + qryEnds[j] + (3 * scale);

                        isOverLapping = isOverLappingX(alignS1, alignE1, alignS2, alignE2);
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
