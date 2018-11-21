package FileHandling;

import DataTypes.AlignmentInfo;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedHashMap;
import javax.swing.JOptionPane;


/*
 * @author Josie
 */
public class XmapReader {

    private static boolean swap = false;

    public static boolean isSwap() {
        return swap;
    }

    public static void setSwap(boolean swap) {
        XmapReader.swap = swap;
    }

    static public LinkedHashMap<String, AlignmentInfo> xmapToHashMap(String filename) {
        String line;
        String fields[];
        String refId;
        String qryId;

        LinkedHashMap<String, AlignmentInfo> xmapAlignments = new LinkedHashMap();
        AlignmentInfo alignmentInfo;

        String qryAlignStart;
        String qryAlignEnd;
        String refAlignStart;
        String refAlignEnd;
        String orientation;
        String confidence;
        String hitEnum;
        String labelChannel;
        String alignment;

        // Check the file is an xmap file
        if (filename.contains(".xmap")) {
            // Try to read file and assign variable to hashmap of contigs
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#")) {
                        continue;
                    }

                    fields = line.split("\t");

                    if (swap) {
                        refId = fields[1];
                        qryId = fields[2];
                        orientation = fields[7];

                        if (orientation.equals("-")) {
                            qryAlignStart = fields[6];
                            qryAlignEnd = fields[5];
                            refAlignStart = fields[4];
                            refAlignEnd = fields[3];

                        } else {
                            qryAlignStart = fields[5];
                            qryAlignEnd = fields[6];
                            refAlignStart = fields[3];
                            refAlignEnd = fields[4];
                        }

                        confidence = fields[8];
                        hitEnum = fields[9];
                        labelChannel = fields[12];
                        alignment = fields[13];

                        // set alignment coordinates
                        String[] alignmentsString = alignment.substring(1, alignment.length() - 1).split("\\)\\(");
                        alignment = "";
                        for (String i : alignmentsString) {
                            String[] alignmentCoords = i.split(",");
                            String alignmentSwap = "(" + alignmentCoords[1] + "," + alignmentCoords[0] + ")";
                            alignment = alignment + alignmentSwap;
                        }

                    } else {
                        refId = fields[2];
                        qryId = fields[1];

                        qryAlignStart = fields[3];
                        qryAlignEnd = fields[4];
                        refAlignStart = fields[5];
                        refAlignEnd = fields[6];
                        orientation = fields[7];
                        confidence = fields[8];
                        hitEnum = fields[9];
                        labelChannel = fields[12];
                        alignment = fields[13];

                    }

                    // add fields to alignmentInfo object
                    alignmentInfo = new AlignmentInfo(
                            qryAlignStart,
                            qryAlignEnd,
                            refAlignStart,
                            refAlignEnd,
                            orientation,
                            confidence,
                            hitEnum,
                            labelChannel,
                            alignment);

                    xmapAlignments.put(refId + "-" + qryId, alignmentInfo);
                }

            } catch (Exception ex) {
                //Show error message if no file found
                JOptionPane.showMessageDialog(null,
                        "Error loading XMAP file\n\nError: " + ex,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //Show error message if file not in fasta format
            JOptionPane.showMessageDialog(null,
                    "Error reading file\n\nMake sure file is in XMAP format",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return xmapAlignments;
    }
}
