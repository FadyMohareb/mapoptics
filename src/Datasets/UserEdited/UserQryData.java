<<<<<<< HEAD
package Datasets.UserEdited;

import DataTypes.QryContig;
import DataTypes.RefContig;
import UserInterface.QueryView;

import java.util.LinkedHashMap;

/*
 * @author Josie
   Stores any changes made by the user to query view. Query view is displayed
   from data in this class
 */
@Deprecated
public class UserQryData {

    // Query alignment view data hashmaps set with those coordinates
    private static LinkedHashMap<String, QryContig> queries = new LinkedHashMap();
    private static LinkedHashMap<String, RefContig> references = new LinkedHashMap();
    private static LinkedHashMap<String, String[]> qryConnections = new LinkedHashMap();

    public static void resetData() {
        UserQryData.references = new LinkedHashMap();
        UserQryData.queries = new LinkedHashMap();
        UserQryData.qryConnections = new LinkedHashMap();
    }
/*
    public static void setData() {
        // set all references from default dataset
        for (String refqryId : QueryViewData.getReferences().keySet()) {
            RefContig ref1 = QueryViewData.getReferences(refqryId);
            RefContig ref2 = ref1.copy();
            references.put(refqryId, ref2);
        }

        // set all queries from default dataset
        for (String refqryId : QueryViewData.getQueries().keySet()) {
            QryContig qry1 = QueryViewData.getQueries(refqryId);
            QryContig qry2 = qry1.copy();
            queries.put(refqryId, qry2);
        }

        // set query connections
        for (String qryId : QueryViewData.getQryConnections().keySet()) {
            String[] refIds = QueryViewData.getQryConnections(qryId);
            qryConnections.put(qryId, refIds);
        }
    }

    public static void resetDataToDefault() {
        String chosenRef = QueryView.getChosenRef();
        String[] connections;
        for (String qryId : qryConnections.keySet()) {
            connections = qryConnections.get(qryId);

            for (String refId : connections) {
                if (refId.equals(chosenRef)) {
                    // set all references
                    RefContig ref1 = QueryViewData.getReferences(chosenRef + "-" + qryId);
                    RefContig ref2 = ref1.copy();
                    references.put(chosenRef + "-" + qryId, ref2);

                    // set all queries
                    QryContig qry1 = QueryViewData.getQueries(chosenRef + "-" + qryId);
                    QryContig qry2 = qry1.copy();
                    queries.put(chosenRef + "-" + qryId, qry2);
                    
                    String[] refIds = QueryViewData.getQryConnections(qryId);
                    qryConnections.put(qryId, refIds);
                }
            }
        }
    }
*/
    public static void resetDataToLastSaved() {
        String chosenRef = QueryView.getChosenRef();
        String[] connections;
        for (String qryId : qryConnections.keySet()) {
            connections = qryConnections.get(qryId);

            for (String refId : connections) {
                if (refId.equals(chosenRef)) {
                    // set all references
                    RefContig ref1 = SavedQryData.getReferences(chosenRef + "-" + qryId);
                    RefContig ref2 = ref1.copy();
                    references.put(chosenRef + "-" + qryId, ref2);

                    // set all queries
                    QryContig qry1 = SavedQryData.getQueries(chosenRef + "-" + qryId);
                    QryContig qry2 = qry1.copy();
                    queries.put(chosenRef + "-" + qryId, qry2);

                    String[] refIds = SavedQryData.getQryConnections(qryId);
                    qryConnections.put(qryId, refIds);
                }
            }
        }
    }
/*
    public static void addSequences(LinkedHashMap<String, String> names, LinkedHashMap<String, ArrayList<Integer>> sequences, String refQry) {
        // adds sequence data from fasta file
        if (refQry.equals("ref")) {
            for (String refqryId : references.keySet()) {
                String refId = refqryId.split("-")[0];
                if (names.containsKey(refId)) {
                    String name = names.get(refId);
                    references.get(refqryId).setSequence(sequences.get(name));
                    references.get(refqryId).setName(name);
                }
            }
        }
        if (refQry.equals("qry")) {
            for (String refqryId : queries.keySet()) {
                String qryId = refqryId.split("-")[1];
                if (names.containsKey(qryId)) {
                    String name = names.get(qryId);
                    queries.get(refqryId).setSequence(sequences.get(name));
                    queries.get(refqryId).setName(name);
                }
            }
        }
    }
*/
    public static LinkedHashMap<String, QryContig> getQueries() {
        return queries;
    }

    public static QryContig getQueries(String refqryId) {
        return queries.get(refqryId);
    }

    public static void setQueries(LinkedHashMap<String, QryContig> queries) {
        UserQryData.queries = queries;
    }

    public static LinkedHashMap<String, RefContig> getReferences() {
        return references;
    }

    public static RefContig getReferences(String refqryId) {
        return references.get(refqryId);
    }

    public static void setReferences(LinkedHashMap<String, RefContig> references) {
        UserQryData.references = references;
    }

    public static String[] getQryConnections(String qryId) {
        return qryConnections.get(qryId);
    }

}
=======
package Datasets.UserEdited;

import DataTypes.QryContig;
import DataTypes.RefContig;
import UserInterface.QueryView;

import java.util.LinkedHashMap;

/*
 * @author Josie
   Stores any changes made by the user to query view. Query view is displayed
   from data in this class
 */
@Deprecated
public class UserQryData {

    // Query alignment view data hashmaps set with those coordinates
    private static LinkedHashMap<String, QryContig> queries = new LinkedHashMap();
    private static LinkedHashMap<String, RefContig> references = new LinkedHashMap();
    private static LinkedHashMap<String, String[]> qryConnections = new LinkedHashMap();

    public static void resetData() {
        UserQryData.references = new LinkedHashMap();
        UserQryData.queries = new LinkedHashMap();
        UserQryData.qryConnections = new LinkedHashMap();
    }
/*
    public static void setData() {
        // set all references from default dataset
        for (String refqryId : QueryViewData.getReferences().keySet()) {
            RefContig ref1 = QueryViewData.getReferences(refqryId);
            RefContig ref2 = ref1.copy();
            references.put(refqryId, ref2);
        }

        // set all queries from default dataset
        for (String refqryId : QueryViewData.getQueries().keySet()) {
            QryContig qry1 = QueryViewData.getQueries(refqryId);
            QryContig qry2 = qry1.copy();
            queries.put(refqryId, qry2);
        }

        // set query connections
        for (String qryId : QueryViewData.getQryConnections().keySet()) {
            String[] refIds = QueryViewData.getQryConnections(qryId);
            qryConnections.put(qryId, refIds);
        }
    }

    public static void resetDataToDefault() {
        String chosenRef = QueryView.getChosenRef();
        String[] connections;
        for (String qryId : qryConnections.keySet()) {
            connections = qryConnections.get(qryId);

            for (String refId : connections) {
                if (refId.equals(chosenRef)) {
                    // set all references
                    RefContig ref1 = QueryViewData.getReferences(chosenRef + "-" + qryId);
                    RefContig ref2 = ref1.copy();
                    references.put(chosenRef + "-" + qryId, ref2);

                    // set all queries
                    QryContig qry1 = QueryViewData.getQueries(chosenRef + "-" + qryId);
                    QryContig qry2 = qry1.copy();
                    queries.put(chosenRef + "-" + qryId, qry2);
                    
                    String[] refIds = QueryViewData.getQryConnections(qryId);
                    qryConnections.put(qryId, refIds);
                }
            }
        }
    }
*/
    public static void resetDataToLastSaved() {
        String chosenRef = QueryView.getChosenRef();
        String[] connections;
        for (String qryId : qryConnections.keySet()) {
            connections = qryConnections.get(qryId);

            for (String refId : connections) {
                if (refId.equals(chosenRef)) {
                    // set all references
                    RefContig ref1 = SavedQryData.getReferences(chosenRef + "-" + qryId);
                    RefContig ref2 = ref1.copy();
                    references.put(chosenRef + "-" + qryId, ref2);

                    // set all queries
                    QryContig qry1 = SavedQryData.getQueries(chosenRef + "-" + qryId);
                    QryContig qry2 = qry1.copy();
                    queries.put(chosenRef + "-" + qryId, qry2);

                    String[] refIds = SavedQryData.getQryConnections(qryId);
                    qryConnections.put(qryId, refIds);
                }
            }
        }
    }
/*
    public static void addSequences(LinkedHashMap<String, String> names, LinkedHashMap<String, ArrayList<Integer>> sequences, String refQry) {
        // adds sequence data from fasta file
        if (refQry.equals("ref")) {
            for (String refqryId : references.keySet()) {
                String refId = refqryId.split("-")[0];
                if (names.containsKey(refId)) {
                    String name = names.get(refId);
                    references.get(refqryId).setSequence(sequences.get(name));
                    references.get(refqryId).setName(name);
                }
            }
        }
        if (refQry.equals("qry")) {
            for (String refqryId : queries.keySet()) {
                String qryId = refqryId.split("-")[1];
                if (names.containsKey(qryId)) {
                    String name = names.get(qryId);
                    queries.get(refqryId).setSequence(sequences.get(name));
                    queries.get(refqryId).setName(name);
                }
            }
        }
    }
*/
    public static LinkedHashMap<String, QryContig> getQueries() {
        return queries;
    }

    public static QryContig getQueries(String refqryId) {
        return queries.get(refqryId);
    }

    public static void setQueries(LinkedHashMap<String, QryContig> queries) {
        UserQryData.queries = queries;
    }

    public static LinkedHashMap<String, RefContig> getReferences() {
        return references;
    }

    public static RefContig getReferences(String refqryId) {
        return references.get(refqryId);
    }

    public static void setReferences(LinkedHashMap<String, RefContig> references) {
        UserQryData.references = references;
    }

    public static String[] getQryConnections(String qryId) {
        return qryConnections.get(qryId);
    }

}
>>>>>>> mapOpticsv2/master
