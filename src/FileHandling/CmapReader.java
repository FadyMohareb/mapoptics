package FileHandling;

import DataTypes.Query;
import DataTypes.Reference;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

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

                    br.close();

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

    public static void getSummaryData(File cmapFile, Map<Integer, Reference> references) {

        Set<Integer> unvisited = new HashSet<>(references.keySet());

        try {
            BufferedReader br = new BufferedReader(new FileReader(cmapFile));
            String line;

            List<String> header = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.isEmpty()) {
                    if (line.startsWith("#h")) {
                        Collections.addAll(header, line.split("\t"));
                    }
                    continue;
                }

                String[] rowData = line.split("\t");
                int id = Integer.parseInt(rowData[0]);

                if (references.containsKey(id)) {
                    Reference ref = references.get(id);
                    if (unvisited.contains(id)) {
                        unvisited.remove(id);
                        ref.setLength(Double.parseDouble(rowData[1]));
                        ref.setLabels(Integer.parseInt(rowData[2]));
                        ref.setDensity();
                        ref.setCoverage(Double.parseDouble(rowData[header.indexOf("Coverage")]));
                        if (header.contains("ChimQuality")) {
                            ref.setChimQual(Double.parseDouble(rowData[header.indexOf("ChimQuality")]));
                        } else {
                            ref.setChimQual(0.0);
                        }
                    }

                    ref.addSite(Integer.parseInt(rowData[3]), Double.parseDouble(rowData[5]));
                }
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void getReferenceData(File qryFile, Map<Integer,Query> queries) {

        Set<Integer> unvisited = new HashSet<>(queries.keySet());

        try {
            BufferedReader br = new BufferedReader(new FileReader(qryFile));
            String line;

            List<String> header = new ArrayList<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.isEmpty()) {
                    if (line.startsWith("#h")) {
                        Collections.addAll(header, line.split("\t"));
                    }
                    continue;
                }

                String[] rowData = line.split("\t");
                int channel = Integer.parseInt(rowData[4]);
                if(channel!=0){
                int id = Integer.parseInt(rowData[0]);

                if (queries.containsKey(id)) {
                    Query qry = queries.get(id);
                    if (unvisited.contains(id)) {
                        unvisited.remove(id);
                        qry.setLength(Double.parseDouble(rowData[1]));
                        qry.setLabels(Integer.parseInt(rowData[2]));
                    }

                    qry.addSite(Integer.parseInt(rowData[3]), Arrays.asList(
                            Double.parseDouble(rowData[header.indexOf("Position")]),
                            Double.parseDouble(rowData[header.indexOf("StdDev")]),
                            Double.parseDouble(rowData[header.indexOf("Coverage")]),
                            Double.parseDouble(rowData[header.indexOf("Occurrence")]),
                            header.contains("ChimQuality") ?
                                    Double.parseDouble(rowData[header.indexOf("ChimQuality")]) : 0.0));
                }
            }}

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
