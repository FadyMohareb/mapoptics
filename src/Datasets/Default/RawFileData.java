package Datasets.Default;

import DataTypes.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;

/*
    @author Josie
    Class containing all hashmaps of untouched raw data - not to be altered by the user
    Used as a basis for the rest of the class calculations
 */
public class RawFileData {

    // RawData datasets from file inputs
    private static LinkedHashMap<String, ContigInfo> qryContigs = new LinkedHashMap();
    private static LinkedHashMap<String, ContigInfo> refContigs = new LinkedHashMap();
    private static LinkedHashMap<String, AlignmentInfo> alignmentInfo = new LinkedHashMap();

    // hashmaps of query and reference information
    private static LinkedHashMap<String, RefContig> references = new LinkedHashMap();
    private static LinkedHashMap<String, QryContig> queries = new LinkedHashMap();

    // arrays of lengths and label density values for graphs
    private static ArrayList<Double> refLengths = new ArrayList();
    private static ArrayList<Double> refDensity = new ArrayList();

    public static void resetData() {
        RawFileData.qryContigs = new LinkedHashMap();
        RawFileData.refContigs = new LinkedHashMap();
        RawFileData.alignmentInfo = new LinkedHashMap();

        RawFileData.references = new LinkedHashMap();
        RawFileData.queries = new LinkedHashMap();

        RawFileData.refLengths = new ArrayList();
        RawFileData.refDensity = new ArrayList();
    }

    public static ArrayList<Double> getRefLengths() {
        return refLengths;
    }

    public static ArrayList<Double> getRefDensity() {
        return refDensity;
    }

    public static LinkedHashMap<String, ContigInfo> getQryContigs() {
        return qryContigs;
    }

    public static void setQryContigs(LinkedHashMap<String, ContigInfo> qryContigs) {
        RawFileData.qryContigs = qryContigs;
    }

    public static LinkedHashMap<String, ContigInfo> getRefContigs() {
        return refContigs;
    }

    public static void setRefContigs(LinkedHashMap<String, ContigInfo> refContigs) {
        RawFileData.refContigs = refContigs;
    }

    public static void setAlignmentInfo(LinkedHashMap<String, AlignmentInfo> alignmentInfo) {
        RawFileData.alignmentInfo = alignmentInfo;
    }

    public static void setData() {
        LinkedHashMap<String, ArrayList<String>> connections = new LinkedHashMap();

        // get list of queries to add connections
        for (String refqryId : alignmentInfo.keySet()) {
            String refId = refqryId.split("-")[0];
            String qryId = refqryId.split("-")[1];

            QryContig qry = setQryData(refId, qryId);
            queries.put(refId + "-" + qryId, qry);

            if (connections.keySet().contains(refId)) {
                if (!connections.get(refId).contains(qryId)) {
                    connections.get(refId).add(qryId);
                }
            } else {
                connections.put(refId, new ArrayList());
                connections.get(refId).add(qryId);
            }
        }

        for (String refId : connections.keySet()) {
            RefContig ref = setRefData(refId, connections.get(refId).toArray(new String[connections.get(refId).size()]));
            references.put(refId, ref);
        }
        
        for (String refId : references.keySet()) {
            refLengths.add(refContigs.get(refId).getContigLen());
            refDensity.add(((refContigs.get(refId).getLabelInfo().length - 1) / refContigs.get(refId).getContigLen()) * 100000);
        }
        Collections.sort(refLengths);
        Collections.sort(refDensity);

    }

    private static RefContig setRefData(String refId, String[] qryList) {
        // set reference contig length
        double length = refContigs.get(refId).getContigLen();
        Rectangle2D refRect = new Rectangle2D.Double(0, 6, length, 1);
        // set reference contig labels
        ArrayList<Rectangle2D> labels = new ArrayList();
        Rectangle2D label;
        double refLabelPos;
        for (int i = 0; i < refContigs.get(refId).getLabelInfo().length - 1; i++) {
            refLabelPos = Double.parseDouble(refContigs.get(refId).getLabelInfo()[i].getLabelPos());
            label = new Rectangle2D.Double(refRect.getMinX() + refLabelPos, refRect.getMinY(), 1, refRect.getHeight());
            labels.add(label);
        }
        return new RefContig(refRect,
                labels.toArray(new Rectangle2D[labels.size()]),
                qryList);
    }

    private static QryContig setQryData(String refId, String qryId) {

        AlignmentInfo alignInfo = alignmentInfo.get(refId + "-" + qryId);

        // set alignment coordinates
        String[] alignmentsString = alignInfo.getAlignment().substring(1, alignInfo.getAlignment().length() - 1).split("\\)\\(");
        ArrayList<String[]> alignments = new ArrayList();
        for (String i : alignmentsString) {
            alignments.add(i.split(","));
        }

        double qryStart = 0.0;
        double qryEnd = 0.0;
        double refStart = Double.parseDouble(alignInfo.getRefAlignStart());
        double refEnd = Double.parseDouble(alignInfo.getRefAlignEnd());

        // set shift of position for query contig relative to reference and orientation
        double qryShift = 0;
        if (alignInfo.getOrientation().equals("+")) {
            qryShift = (Double.parseDouble(alignInfo.getRefAlignStart())) - Double.parseDouble(alignInfo.getQryAlignStart());
            qryStart = Double.parseDouble(alignInfo.getQryAlignStart());
            qryEnd = Double.parseDouble(alignInfo.getQryAlignEnd());
        } else if (alignInfo.getOrientation().equals("-")) {
            qryShift = (Double.parseDouble(alignInfo.getRefAlignStart())) - Double.parseDouble(alignInfo.getQryAlignEnd());
            qryStart = Double.parseDouble(alignInfo.getQryAlignEnd());
            qryEnd = Double.parseDouble(alignInfo.getQryAlignStart());
        }

        // set query rectangle
        Rectangle2D qryRect = new Rectangle2D.Double(qryShift, 10, qryContigs.get(qryId).getContigLen(), 1);

        // set query labels
        ArrayList<Rectangle2D> labels = new ArrayList();
        double labelPos;
        Rectangle2D label;
        for (int i = 0; i < qryContigs.get(qryId).getLabelInfo().length - 1; i++) {
            labelPos = Double.parseDouble(qryContigs.get(qryId).getLabelInfo()[i].getLabelPos());
            label = new Rectangle2D.Double(qryRect.getMinX() + labelPos, qryRect.getMinY(), 1, qryRect.getHeight());
            labels.add(label);
        }
        return new QryContig(qryRect,
                labels.toArray(new Rectangle2D[labels.size()]),
                alignments.toArray(new String[alignments.size()][]),
                qryStart,
                qryEnd,
                refStart,
                refEnd,
                alignInfo.getOrientation());
    }


    public static RefContig setnewRefData(String refqryId) {
        // set reference contig length
        //-------------------------new-----------------------------------------
        String refId = refqryId.split("-")[0];
        String qryId = refqryId.split("-")[1];
        String[]qryList= {qryId};
        String refstart= RawFileData.getAlignmentInfo(refqryId).getRefAlignStart();
        String refend = RawFileData.getAlignmentInfo(refqryId).getRefAlignEnd();
        Double length= Double.parseDouble(refend)-Double.parseDouble(refstart);

        Rectangle2D refRect = new Rectangle2D.Double(0, 6, length, 1);
        // set reference contig labels
        ArrayList<Rectangle2D> labels = new ArrayList();
        Rectangle2D label;
        double refLabelPos;
        for (int i = 0; i < refContigs.get(refId).getLabelInfo().length - 1; i++) {

            refLabelPos = Double.parseDouble(refContigs.get(refId).getLabelInfo()[i].getLabelPos());
            if (refLabelPos>=Double.parseDouble(refend)& refLabelPos<= Double.parseDouble(refend)){
                label = new Rectangle2D.Double(refRect.getMinX() + refLabelPos, refRect.getMinY(), 1, refRect.getHeight());
                labels.add(label);
            }
        }
        return new RefContig(refRect,
                labels.toArray(new Rectangle2D[labels.size()]),
                qryList);
    }


    public static LinkedHashMap<String, RefContig> getReferences() {
        return references;
    }

    public static LinkedHashMap<String, QryContig> getQueries() {
        return queries;
    }

    public static ContigInfo getQryContigs(String qryId) {
        return qryContigs.get(qryId);
    }

    public static ContigInfo getRefContigs(String refId) {
        return refContigs.get(refId);
    }

    public static AlignmentInfo getAlignmentInfo(String refqryId) {
        return alignmentInfo.get(refqryId);
    }

    public static RefContig getReferences(String refId) {
        return references.get(refId);
    }

    public static QryContig getQueries(String refqryId) {
        return queries.get(refqryId);
    }
}
