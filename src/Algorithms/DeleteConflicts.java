package Algorithms;

import DataTypes.*;
import Datasets.UserEdited.UserQryData;
import Datasets.UserEdited.UserRefData;
import UserInterface.ReferenceView;
import java.util.ArrayList;
/*
 * @author Josie
 */
public class DeleteConflicts {

    public static void deleteOne(String refId, String qryId) {
        // delete selected contig
        String refqryId = refId + "-" + qryId;
        UserRefData.getQueries().remove(refqryId);
        UserQryData.getQueries().remove(refqryId);

        RefContig ref = UserRefData.getReferences(ReferenceView.getChosenRef());
        ArrayList<String> newConnections = new ArrayList();
        for (String connection : ref.getConnections()) {
            if (!ReferenceView.getChosenQry().equals(connection)) {
                newConnections.add(connection);
            }
        }

        ref.setConnections(newConnections.toArray(new String[newConnections.size()]));
    }

}
