package Datasets.Default;

import DataTypes.QryContig;
import DataTypes.RefContig;
import DataTypes.Reference;
import FileHandling.CmapReader;
import FileHandling.XmapReader;
import UserInterface.ModelsAndRenderers.MapOpticsModel;

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

    @Deprecated
    public static void resetData() {
        SummaryViewData.references = new LinkedHashMap();
        SummaryViewData.queries = new LinkedHashMap();
    }

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

}
