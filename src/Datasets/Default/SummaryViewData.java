package Datasets.Default;

import DataTypes.QryContig;
import DataTypes.RefContig;
import DataTypes.Reference;
import FileHandling.CmapReader;
import FileHandling.XmapReader;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.util.*;

/*
 * @author Josie
   Class containing hashmaps of references and queries where rectangle2D objects
    are scaled relative to summary view panel
 */
public class SummaryViewData {

    // scale variables
    private static double horZoom;
    private static double vertZoom;

    // RefAlignData hashmaps scaled and overlaps sorted
    private static LinkedHashMap<String, RefContig> references = new LinkedHashMap();
    private static LinkedHashMap<String, QryContig> queries = new LinkedHashMap();

    public static void resetData() {
        SummaryViewData.references = new LinkedHashMap();
        SummaryViewData.queries = new LinkedHashMap();
    }

//    public static void setSummaryData(MapOpticsModel model) {
//
//        Map<Integer, List<Object>> xmapSummaryDataMap =
//                XmapReader.getSummaryData(model.getXmapFile(), model.isReversed());
//
//        List<Integer> refIdList = new ArrayList<>(xmapSummaryDataMap.keySet());
//        List<List<Object>> cmapData = CmapReader.getSummaryData(model.getRefFile(), refIdList);
//        Map<Integer, Integer> refQueries = new HashMap<>();
//        List<Double> lengths = new ArrayList<>();
//        List<Double> densities = new ArrayList<>();
//
//        List<Object[]> tableData = new ArrayList<>();
//
//        for (List<Object> row : cmapData) {
//            int id = (Integer) row.get(0);
//            List<Object> xmapData = xmapSummaryDataMap.get(id);
//            Object[] data = {row.get(0), row.get(1), row.get(2), row.get(3), xmapData.get(0), xmapData.get(1)};
//            tableData.add(data);
//
//            refQueries.put(id, (Integer) xmapData.get(2));
//            lengths.add((Double) row.get(1));
//            densities.add((Double) row.get(3));
//        }
//
//        Collections.sort(lengths);
//        Collections.sort(densities);
//
//        model.setSummaryTableRows(tableData);
//        model.setRefQueries(refQueries);
//        model.setLengths(lengths);
//        model.setDensities(densities);
//    }

    public static void setSummaryData(MapOpticsModel model) {

        Map<Integer, Reference> referenceMap = XmapReader.getSummaryData(model.getXmapFile(), model.isReversed());
        List<Reference> references = new ArrayList<>(referenceMap.values());
        CmapReader.getSummaryData(model.getRefFile(), referenceMap);

        List<Double> lengths = new ArrayList<>();
        List<Double> densities = new ArrayList<>();

        for (Reference ref : references) {
            lengths.add(ref.getLength());
            densities.add(ref.getDensity());
        }

        Collections.sort(lengths);
        Collections.sort(densities);

        model.setLengths(lengths);
        model.setDensities(densities);
        model.setReferences(references);
    }

    public static void setData() {
        
       // loop through reference view data and rescale to fit the summary view panel
        for (String refId : RefViewData.getReferences().keySet()) {
            RefContig ref1 = RefViewData.getReferences(refId);
            RefContig ref2 = ref1.copy();
            ref2.setRectangle(resizeRect(ref1.getRectangle().getBounds2D()));
            Rectangle2D[] labels = new Rectangle2D[ref1.getLabels().length];
            for (int j = 0; j < ref1.getLabels().length; j++) {
                labels[j] = resizeRect(ref1.getLabels()[j].getBounds2D());
            }
            ref2.setLabels(labels);
            references.put(refId, ref2);

            // resize all queries
            for (String qryId : ref1.getConnections()) {
                QryContig qry1 = RefViewData.getQueries(refId + "-" + qryId);
                QryContig qry2 = qry1.copy();
                qry2.setRectangle(resizeRect(qry1.getRectangle().getBounds2D()));
                labels = new Rectangle2D[qry1.getLabels().length];
                for (int j = 0; j < qry1.getLabels().length; j++) {
                    labels[j] = resizeRect(qry1.getLabels()[j].getBounds2D());
                }
                qry2.setLabels(labels);
                queries.put(refId + "-" + qryId, qry2);
            }
        }
    }

    private static Rectangle2D resizeRect(Rectangle2D rect) {
        AffineTransform at2 = AffineTransform.getScaleInstance(horZoom, vertZoom);
        rect = at2.createTransformedShape(rect).getBounds2D();
        return rect;
    }

    public static LinkedHashMap<String, RefContig> getReferences() {
        return references;
    }

    public static LinkedHashMap<String, QryContig> getQueries() {
        return queries;
    }

    public static RefContig getReferences(String refId) {
        return references.get(refId);
    }

    public static QryContig getQueries(String refqryId) {
        return queries.get(refqryId);
    }

    public static void setHorZoom(double horZoom) {
        SummaryViewData.horZoom = horZoom;
    }

    public static void setVertZoom(double vertZoom) {
        SummaryViewData.vertZoom = vertZoom;
    }

}
