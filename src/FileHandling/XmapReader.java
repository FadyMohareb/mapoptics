package FileHandling;

import DataTypes.AlignmentInfo;
import DataTypes.Query;
import DataTypes.Reference;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

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
public class XmapReader {

    private static boolean swap = false;

    public static boolean isSwap() {
        return swap;
    }

    public static void setSwap(boolean swap) {
        XmapReader.swap = swap;
    }

    public static boolean validateXmap (String filePath) {

        if (filePath.endsWith(".xmap")) {
            if (Files.exists(Paths.get(filePath))) {
                try {
                    BufferedReader br = new BufferedReader(new FileReader(filePath));
                    String line;

                    while ((line = br.readLine()) != null && line.startsWith("#")) {
                        if (line.toLowerCase().contains("xmap file version")) {
                            return true;
                        }
                    }

                    br.close();

                    //Show error message if wrong format
                    JOptionPane.showMessageDialog(null,
                            "Error loading XMAP file." +
                                    "\n\nInvalid format!",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                //Show error message if no file found
                JOptionPane.showMessageDialog(null,
                        "Error loading XMAP file." +
                                "\n\nFile does not exist!",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        } else {
            //Show error message if wrong file type
            JOptionPane.showMessageDialog(null,
                    "Error loading XMAP file." +
                            "\n\nInvalid file type!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }

        return false;
    }

    public static Map<Integer, Reference> getSummaryData(File xmapFile, boolean isReversed) {

        Map<Integer, Reference> references = new HashMap<>();

        int refIndex = isReversed ? 1 : 2;
        int queryIndex = isReversed ? 2 : 1;

        try {
            BufferedReader br = new BufferedReader(new FileReader(xmapFile));
            String line;

            while ((line = br.readLine()) != null ) {
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                Reference ref;
                String[] data = line.split("\t");
                int refId = Integer.parseInt(data[refIndex]);
                double start = Double.parseDouble(data[(refIndex*2)+1]);
                double stop = Double.parseDouble(data[(refIndex*2)+2]);

                if (start > stop) {
                    double temp = stop;
                    stop = start;
                    start = temp;
                }

                if (!references.containsKey(refId)) {
                    ref = new Reference(Integer.toString(refId));
                    references.put(refId, ref);
                } else {
                    ref = references.get(refId);
                }

                ref.addQueryRegion(start, stop);
                ref.addQuery(Integer.parseInt(data[queryIndex]));
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

        for (Reference ref : references.values()) {
            ref.setOverlaps();
        }

        return references;
    }

    public static Map<Integer, Query> getReferenceData(MapOpticsModel model) {

        Map<Integer, Query> queries = new HashMap<>();
        Map<Integer, String> refAlignRegion= new HashMap<>();
        List<Integer> queryIDs = model.getSelectedRef().getQueryIDs();
        boolean isReversed = model.isReversed();
        int queryIndex = isReversed ? 2 : 1;
        int refIndex = isReversed ? 1 : 2;
        String refID = model.getSelectedRefID();

        try {
            BufferedReader br = new BufferedReader(new FileReader(model.getXmapFile()));
            String line;

            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                String[] data = line.split("\t");
                int queryID = Integer.parseInt(data[queryIndex]);




                // TODO: Hold data for a given query when it is also found in other reference contigs - could be List<String[refid, orient., conf.]>

//                if (queryIDs.contains(queryID)) {
//                    Query qry;
//                    if (!queries.containsKey(queryID)) {
//                        qry = new Query(data[queryIndex]);
//                    } else {
//                        qry = queries.get(queryID);
//                    }
//
//                    if (data[refIndex].equals(refID)) {
//
//                    }
//
//                    queries.put(queryID, qry);
//                }
//                Query qry = new Query(data[queryIndex]);

                if (data[refIndex].equals(refID)) {
                    Query qry = new Query(data[queryIndex]);
                    qry.setOrientation(data[7]);
                    qry.setConfidence(Double.parseDouble(data[8]));
                    qry.setHitEnum(data[9]);
                    qry.setAlignments(data[13]);

                    queries.put(Integer.parseInt(data[queryIndex]), qry);
//                    queries.put(Integer.parseInt(data[0]), qry); // in case entryID needs to be used
                    String qryStartEnd = data[(refIndex*2)+1]+"-"+data[(refIndex*2)+2];
                    refAlignRegion.put(queryID,qryStartEnd);

                }
            }

            br.close();
            model.getSelectedRef().setRefAlignPos(refAlignRegion);
            setAlignmentSites(model.getSelectedRef(), queries, isReversed);
            return queries;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static Map <String,String[]> getQueryData(MapOpticsModel model,String queryID) {

        boolean isReversed = model.isReversed();
        int queryIndex = isReversed ? 2 : 1;
        int refIndex = isReversed ? 1 : 2;
        List<String> qryIDs= new ArrayList<>();
        Map <String,String[]> connection = new HashMap<>();

       if(queryID !=""){

        try {
            BufferedReader br = new BufferedReader(new FileReader(model.getXmapFile()));
            String line;
            Map<String,String> qryInfos= new HashMap<>();
            Map<String,String> refInfos= new HashMap<>();
            while ((line = br.readLine()) != null) {
                if (line.startsWith("#") || line.isEmpty()) {
                    continue;
                }

                String[] data = line.split("\t");

                if (data[queryIndex].equals(queryID)) {
                    String[] refinfo= {data[7], data[8]};
                    connection.put(data[refIndex],refinfo);
                }
                if(!qryInfos.keySet().contains(data[queryIndex])){
                    qryInfos.put(data[queryIndex],data[queryIndex+9]);
                }
                if(!refInfos.keySet().contains(data[refIndex])){
                    refInfos.put(data[refIndex],data[refIndex+9]);
                }
            }

            MapOpticsModel.setRefList(refInfos);
            MapOpticsModel.setQueryList(qryInfos);

            br.close();
            return connection;

        } catch (IOException e) {
            e.printStackTrace();
        }
       }
        return null;
    }

    private static void setAlignmentSites(Reference selectedRef, Map<Integer, Query> queries, boolean isReversed) {

        Set<Integer> refAlignmentSiteIds = new HashSet<>();

        for (Query qry : queries.values()) {
            Map<Integer, List<Integer>> alignmentSiteIds = new HashMap<>();
            String alignments = qry.getAlignments();
            alignments = alignments.replaceAll("\\(", "");
            String[] siteIds = alignments.split("[,)]");
            List<Integer> refMatches;

            if (!isReversed) {
                for (int i = 0; i < siteIds.length; i++) {
                    int id = Integer.parseInt(siteIds[i]);
                    if (i % 2 == 0) {
                        refAlignmentSiteIds.add(id);
                    } else {
                        if (alignmentSiteIds.containsKey(id)) {
                            refMatches = alignmentSiteIds.get(id);
                            refMatches.add(Integer.parseInt(siteIds[i - 1]));
                            alignmentSiteIds.put(id, refMatches);
                            //System.out.println(Integer.toString(id)+"  "+Integer.toString(Integer.parseInt(siteIds[i - 1])));

                        } else {
                            refMatches = new ArrayList<>();
                            refMatches.add(Integer.parseInt(siteIds[i - 1]));
                            alignmentSiteIds.put(id, refMatches);
                            //System.out.println(Integer.toString(id)+"  "+Integer.toString(Integer.parseInt(siteIds[i - 1])));

                        }
                    }
                }
            } else {
                for (int i = 0; i < siteIds.length; i++) {
                    int id = Integer.parseInt(siteIds[i]);
                    if (i % 2 != 0) {
                        refAlignmentSiteIds.add(id);
                    } else {
                        if (alignmentSiteIds.containsKey(id)) {
                            refMatches = alignmentSiteIds.get(id);
                            refMatches.add(Integer.parseInt(siteIds[i + 1]));
                            alignmentSiteIds.put(id, refMatches);
                            //System.out.println(Integer.toString(id)+"  "+Integer.toString(Integer.parseInt(siteIds[i + 1])));


                        } else {
                            refMatches = new ArrayList<>();
                            refMatches.add(Integer.parseInt(siteIds[i + 1]));
                            alignmentSiteIds.put(id, refMatches);
                           // System.out.println(Integer.toString(id)+"  "+Integer.toString(Integer.parseInt(siteIds[i + 1])));
                        }
                    }
                }
            }

            qry.setAlignmentSites(alignmentSiteIds);
        }

        selectedRef.setAlignmentSites(refAlignmentSiteIds);
    }

//    public static Map<Integer, List<Object>> getSummaryData(File xmapFile, boolean isReversed) {
//
//        Map<Integer, List<Object>> summaryData = new HashMap<>();
//        Map<Integer, Integer> alignments = new HashMap<>();
//        Map<Integer, List<Double>> overlaps = new HashMap<>();
//
//        int refIndex = isReversed ? 1 : 2;
//        int queryIndex = isReversed ? 2 : 1;
//
//        try {
//            BufferedReader br = new BufferedReader(new FileReader(xmapFile));
//            String line;
//
//            while ((line = br.readLine()) != null) {
//                if (line.startsWith("#")) {
//                    continue;
//                }
//
//                String[] data = line.split("\t");
//                int refId = Integer.parseInt(data[refIndex]);
//                double start = Double.parseDouble(data[(refIndex*2)+1]);
//                double stop = Double.parseDouble(data[(refIndex*2)+2]);
//
//                // If not in alignments, will not be in overlaps either
//                if (!alignments.containsKey(refId)) {
//                    alignments.put(refId, 1);
//
//                    List<Double> region = new ArrayList<>();
//                    region.add(start);
//                    region.add(stop);
//                    overlaps.put(refId, region);
//                } else {
//                    alignments.put(refId, alignments.get(refId) + 1);
//                    List<Double> regions = overlaps.get(refId);
//
//                    for (int i = 0; i < regions.size(); i += 2) {
//
//                        if (start < regions.get(i)) {
//                            regions.add(i, start);
//                            regions.add(i + 1, stop);
//                            break;
//                        } else if (start == regions.get(i) && stop < regions.get(i + 1)) {
//                            regions.add(i, start);
//                            regions.add(i + 1, stop);
//                            break;
//                        } else if (i == regions.size() - 2) {
//                            regions.add(start);
//                            regions.add(stop);
//                            break;
//                        }
//                    }
//
//                    overlaps.put(refId, regions, queries);
//                }
//            }
//
//            br.close();
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        Map<Integer, Integer> overlapsCount = CalculateOverlaps.countAllOverlaps(overlaps);
//
//        for (Integer refID : alignments.keySet()) {
//            summaryData.put(refID, Arrays.asList(alignments.get(refID), overlapsCount.get(refID)));
//        }
//
//        return summaryData;
//    }

    public static LinkedHashMap<String, AlignmentInfo> xmapToHashMap(String filename) {
        String line;
        String[] fields;
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
