package Datasets.UserEdited;

import DataTypes.QryContig;
import DataTypes.RefContig;

import java.util.LinkedHashMap;

/*
 * @author Josie
   Stores all the saved data for query view
 */
@Deprecated
public class SavedQryData {

    // Query alignment view data hashmaps set with those coordinates
    private static LinkedHashMap<String, QryContig> queries = new LinkedHashMap();
    private static LinkedHashMap<String, RefContig> references = new LinkedHashMap();
    private static LinkedHashMap<String, String[]> qryConnections = new LinkedHashMap();

    public static void resetData() {
        SavedQryData.references = new LinkedHashMap();
        SavedQryData.queries = new LinkedHashMap();
        SavedQryData.qryConnections = new LinkedHashMap();
    }
/*
    public static void setData() {
        // set all references to equal the default
        for (String refqryId : QueryViewData.getReferences().keySet()) {
            RefContig ref1 = QueryViewData.getReferences(refqryId);
            RefContig ref2 = ref1.copy();
            references.put(refqryId, ref2);
        }

        // set all queries to equal the default
        for (String refqryId : QueryViewData.getQueries().keySet()) {
            QryContig qry1 = QueryViewData.getQueries(refqryId);
            QryContig qry2 = qry1.copy();
            queries.put(refqryId, qry2);
        }

        for (String qryId : QueryViewData.getQryConnections().keySet()) {
            String[] refIds = QueryViewData.getQryConnections(qryId);
            qryConnections.put(qryId, refIds);
        }
    }
*/
    public static void saveAllData() {
        for (String refqryId : references.keySet()) {
            saveOneData(refqryId.split("-")[0]);
        }
    }

    public static void saveOneData(String chosenRef) {
        // stores all data from current user edited dataset into this one when saved
        String[] connections;
        for (String qryId : qryConnections.keySet()) {
            connections = qryConnections.get(qryId);

            for (String refId : connections) {
                if (refId.equals(chosenRef)) {
                    // set all references
                    RefContig ref1 =  UserQryData.getReferences(chosenRef + "-" + qryId);
                    RefContig ref2 = ref1.copy();
                    references.put(chosenRef + "-" + qryId, ref2);

                    // set all queries
                    QryContig qry1 = UserQryData.getQueries(chosenRef + "-" + qryId);
                    QryContig qry2 = qry1.copy();
                    queries.put(chosenRef + "-" + qryId, qry2);

                    String[] refIds = UserQryData.getQryConnections(qryId);
                    qryConnections.put(qryId, refIds);
                }
            }
        }
    }

    public static LinkedHashMap<String, QryContig> getQueries() {
        return queries;
    }

    public static QryContig getQueries(String refqryId) {
        return queries.get(refqryId);
    }

    public static void setQueries(LinkedHashMap<String, QryContig> queries) {
        SavedQryData.queries = queries;
    }

    public static LinkedHashMap<String, RefContig> getReferences() {
        return references;
    }

    public static RefContig getReferences(String refqryId) {
        return references.get(refqryId);
    }

    public static void setReferences(LinkedHashMap<String, RefContig> references) {
        SavedQryData.references = references;
    }

    public static String[] getQryConnections(String qryId) {
        return qryConnections.get(qryId);
    }

}
