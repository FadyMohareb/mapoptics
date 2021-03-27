package Algorithms;

import UserInterface.ModelsAndRenderers.MapOpticsModel;
import UserInterface.ReferenceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/*
 * @author Josie
 */
public class DeleteConflicts {

    private final Map<String, List<String>> deletedContigs = new HashMap<>();

    /* Replaced the deleting contig function with deleted contigs map methods below

    public static void deleteOne(String refId, String qryId) {
        // delete selected contig
        String refqryId = refId + "-" + qryId;
        UserRefData.getQueries().remove(refqryId);
        UserQryData.getQueries().remove(refqryId);

        RefContig ref = UserRefData.getReferences(ReferenceView.getChosenRef());
        ArrayList<String> newConnections = new ArrayList<>();
        for (String connection : ref.getConnections()) {
            if (!ReferenceView.getChosenQry().equals(connection)) {
                newConnections.add(connection);
            }
        }

        ref.setConnections(newConnections.toArray(new String[newConnections.size()]));
    }

     */
    public Map<String, List<String>> getDeletedContigs() {
        return deletedContigs;
    }

    public void setDeletedContigs() {
        // Populate deletedContigs map with deleted contigs
        String refID = MapOpticsModel.getSelectedRefID();
        String qryID = ReferenceView.getChosenQry();
        List<String> queries = new ArrayList<>();
        // check if refID and qryID pair is already present in deletedContigs
        if (deletedContigs.containsKey(refID)) {
            if (!deletedContigs.get(refID).contains(qryID)) {
                // get ID of selected query and add to queries list
                deletedContigs.get(refID).add(qryID);
            }
        }
        else {
            // else create a new entry in deletedContigs map for refID and query ID
            queries.add(qryID);
            deletedContigs.put(refID, queries);
        }
    }

    public void clearDeletedContigs() {
        // empty deleted contigs map
        deletedContigs.clear();
    }
}
