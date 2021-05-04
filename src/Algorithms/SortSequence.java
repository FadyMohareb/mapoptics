<<<<<<< HEAD
package Algorithms;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/*
 * @author Josie
 */
@Deprecated
public class SortSequence {

    public static Rectangle2D[] findGaps(String seq, double scale, Rectangle2D rect) {
        ArrayList<Rectangle2D> gaps = new ArrayList<>();
        String match = "N";
        int index1 = seq.indexOf(match);
        int index2;
        int count = 0;
        Rectangle2D gap;
        while (index1 >= 0) {
            index2 = seq.indexOf(match, index1 + 1);

            if (index2 - index1 == 1) {
                count++;
            } else {
                    if (count * scale < 1) {
                        gap = new Rectangle2D.Double(
                                rect.getMinX() + (index1 - count) * scale, rect.getMinY(), 1, rect.getHeight());
                    } else {
                        gap = new Rectangle2D.Double(
                                rect.getMinX() + (index1 - count) * scale, rect.getMinY(),
                                count * scale, rect.getHeight());
                    }
                    gaps.add(gap);
                count = 0;
            }

            index1 = index2;
        }
        gap = new Rectangle2D.Double((index1 - count) * scale, rect.getMinY(), count * scale, rect.getHeight());
        gaps.add(gap);
        return gaps.toArray(new Rectangle2D[0]);
    }
}
=======
package Algorithms;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/*
 * @author Josie
 */
@Deprecated
public class SortSequence {

    public static Rectangle2D[] findGaps(String seq, double scale, Rectangle2D rect) {
        ArrayList<Rectangle2D> gaps = new ArrayList<>();
        String match = "N";
        int index1 = seq.indexOf(match);
        int index2;
        int count = 0;
        Rectangle2D gap;
        while (index1 >= 0) {
            index2 = seq.indexOf(match, index1 + 1);

            if (index2 - index1 == 1) {
                count++;
            } else {
                    if (count * scale < 1) {
                        gap = new Rectangle2D.Double(
                                rect.getMinX() + (index1 - count) * scale, rect.getMinY(), 1, rect.getHeight());
                    } else {
                        gap = new Rectangle2D.Double(
                                rect.getMinX() + (index1 - count) * scale, rect.getMinY(),
                                count * scale, rect.getHeight());
                    }
                    gaps.add(gap);
                count = 0;
            }

            index1 = index2;
        }
        gap = new Rectangle2D.Double((index1 - count) * scale, rect.getMinY(), count * scale, rect.getHeight());
        gaps.add(gap);
        return gaps.toArray(new Rectangle2D[0]);
    }
}
>>>>>>> mapOpticsv2/master
