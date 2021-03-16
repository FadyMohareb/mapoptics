package Algorithms;

import DataTypes.RefContig;
import Datasets.UserEdited.UserQryData;
import Datasets.UserEdited.UserRefData;
import UserInterface.ReferenceView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static UserInterface.ModelsAndRenderers.MapOpticsModel.getSelectedRefID;

/*
 * @author Josie
 */
public class DeleteConflicts {

    public static Map<String, List<String>> deletedContigs = new HashMap<>();

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

    public static void setDeletedContigs() {
        // Populate deletedContigs map with deleted contigs
        String refID = getSelectedRefID();
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

    public static void clearDeletedContigs() {
        // empty deleted contigs map
        deletedContigs.clear();
    }
}
