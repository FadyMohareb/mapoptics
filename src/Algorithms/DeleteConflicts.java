package Algorithms;


/*
 * @author Josie
 */
@Deprecated
public class DeleteConflicts {
    // This class is no longer needed. Deleted contigs fuctionality has been moved to Reference class.
//    public static void deleteOne(String refId, String qryId) {
//        // delete selected contig
//        String refqryId = refId + "-" + qryId;
//        UserRefData.getQueries().remove(refqryId);
//        UserQryData.getQueries().remove(refqryId);
//
//        RefContig ref = UserRefData.getReferences(ReferenceView.getChosenRef());
//        ArrayList<String> newConnections = new ArrayList<>();
//        for (String connection : ref.getConnections()) {
//            if (!ReferenceView.getChosenQry().equals(connection)) {
//                newConnections.add(connection);
//            }
//        }
//
//        ref.setConnections(newConnections.toArray(new String[newConnections.size()]));
//    }
}
