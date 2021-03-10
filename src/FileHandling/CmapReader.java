package FileHandling;

import DataTypes.ContigInfo;
import DataTypes.LabelInfo;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

/*
 * @author Josie
 */
public class CmapReader {

    public static boolean validateCmap (String filePath) {

        if (filePath.endsWith(".cmap")) {
            if (Files.exists(Paths.get(filePath))) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(filePath));
                    String line;

                    while ((line = br.readLine()) != null && line.startsWith("#")) {
                        if (line.toLowerCase().contains("cmap file version")) {
                            return true;
                        }
                    }

                    //Show error message if wrong format
                    JOptionPane.showMessageDialog(null,
                            "Error loading CMAP file." +
                                    "\n\nInvalid format!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //Show error message if no file found
                JOptionPane.showMessageDialog(null,
                        "Error loading CMAP file." +
                                "\n\nFile does not exist!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //Show error message if wrong file type
            JOptionPane.showMessageDialog(null,
                    "Error loading CMAP file." +
                            "\n\nInvalid file type!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    public static List<List<Object>> getSummaryData(File cmapFile, List<Integer> refIds) {

        List<List<Object>> summaryData = new ArrayList<>();

        try {
            BufferedReader br = new BufferedReader(new FileReader(cmapFile));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#")) {
                    continue;
                }

                String[] rowData = line.split("\t");
                int id = Integer.parseInt(rowData[0]);

                if (refIds.contains(id)) {
                    refIds.remove(Integer.valueOf(id));
                    double length = Double.parseDouble(rowData[1]);
                    int labels = Integer.parseInt(rowData[2]);
                    double density = (labels / length) * 100000;

                    List<Object> data = new ArrayList<>(Arrays.asList(id, length, labels, density));

                    summaryData.add(data);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return summaryData;
    }

    public static LinkedHashMap cmapToHashMap(String filename) {
        String line;
        String fields[];
        String lastCmapId = "";
        String cmapId = "";
        double length = 0.0;

        String labelChannel;
        String labelPos;
        String stdDev;
        String coverage;
        String occurance;
        String chimQuality;

        LinkedHashMap<String, ContigInfo> contigs = new LinkedHashMap();
        ArrayList<LabelInfo> labels = new ArrayList();
        LabelInfo labelInfo;

        // Check the file is a cmap file
        if (filename.endsWith(".cmap")) {
            // Try to read file and assign variable to hashmap of contigs
            try {
                BufferedReader reader = new BufferedReader(new FileReader(filename));
                while ((line = reader.readLine()) != null) {
                    if (line.startsWith("#")) {
                        continue;
                    } else if (line.startsWith(" ")) {
                        line = line.replaceFirst("   ", "");
                    }

                    fields = line.split("\t");

                    cmapId = fields[0];

                    labelChannel = fields[4];
                    labelPos = fields[5];
                    stdDev = fields[6];
                    coverage = fields[7];
                    occurance = fields[8];

                    // add fields to labelInfo objet
                    labelInfo = new LabelInfo(labelChannel, labelPos, stdDev, coverage, occurance);

                    if (fields.length > 9) {
                        chimQuality = fields[9];
                        labelInfo.setChimQuality(chimQuality);
                    }
                    

                    // if the reference contig is the same, add to same arraylist
                    if (cmapId.equals(lastCmapId) || "".equals(lastCmapId)) {
                        labels.add(labelInfo);
                    } else {
                        // add all contigs to hashmap
                        contigs.put(lastCmapId, new ContigInfo(length, labels.toArray(new LabelInfo[labels.size()])));

                        // clear arraylist and add new alignments
                        labels.clear();
                        labels.add(labelInfo);
                    }

                    lastCmapId = cmapId;
                    length = Double.parseDouble(fields[1]);
                }
                contigs.put(cmapId, new ContigInfo(length, labels.toArray(new LabelInfo[labels.size()])));

            } catch (Exception ex) {
                //Show error message if no file found
                JOptionPane.showMessageDialog(null,
                        "Error loading CMAP file\n\nError: " + ex,
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //Show error message if file not in fasta format
            JOptionPane.showMessageDialog(null,
                    "Error reading file\n\nMake sure file is in CMAP format",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
        return contigs;
    }

}
