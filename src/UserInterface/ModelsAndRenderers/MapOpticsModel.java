package UserInterface.ModelsAndRenderers;

import DataTypes.Reference;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author James
 */
public class MapOpticsModel {

    private File refFile, qryFile, xmapFile;
    private static String selectedRefID;
    private Reference selectedRef;
    private boolean isReversed;
    private List<Double> lengths;
    private List<Double> densities;
    private List<Reference> references;
    private double rectangleTotalWidth;
    private double rectangleTotalHeight;

    public MapOpticsModel() {
        isReversed = false;
        selectedRefID = "";
        lengths = new ArrayList<>();
        densities = new ArrayList<>();
        references = new ArrayList<>();
    }

    public void swapRefQry() {
        File oldRef = refFile;
        refFile = qryFile;
        qryFile = oldRef;

        setReversed(!isReversed);
    }

    public boolean isReversed() {
        return isReversed;
    }

    public void setReversed(boolean reversed) {
        isReversed = reversed;
    }

    public File getRefFile() {
        return refFile;
    }

    public void setRefFile(File refFile) {
        this.refFile = refFile;
    }

    public File getQryFile() {
        return qryFile;
    }

    public void setQryFile(File qryFIle) {
        this.qryFile = qryFIle;
    }

    public File getXmapFile() {
        return xmapFile;
    }

    public void setXmapFile(File xmapFile) {
        this.xmapFile = xmapFile;
    }

    public void setLengths(List<Double> lengths) {
        this.lengths = lengths;
    }

    public void setDensities(List<Double> densities) {
        this.densities = densities;
    }

    public String getSelectedRefID() {
        return selectedRefID;
    }

    public List<Double> getLengths() {
        return lengths;
    }

    public void setSelectedRefID(String selectedRow) {
        selectedRefID = selectedRow;
        setSelectedRef(selectedRow);
    }

    public List<Double> getDensities() {
        return densities;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    public List<Reference> getReferences() {
        return references;
    }

    private void setSelectedRef(String refID) {

        if (refID.isEmpty()) {
            selectedRef = null;
        }
        for (Reference reference : references) {
            if (reference.getRefID().equals(refID)) {
                selectedRef = reference;
            }
        }
    }

    public Reference getSelectedRef() {
        return selectedRef;
    }

    public void setRectangleTotalWidth(double rectangleTotalWidth) {
        this.rectangleTotalWidth = rectangleTotalWidth;
    }

    public double getRectangleTotalWidth() {
        return rectangleTotalWidth;
    }

    public void setRectangleTotalHeight(double rectangleTotalHeight) {
        this.rectangleTotalHeight = rectangleTotalHeight;
    }

    public double getRectangleTotalHeight() {
        return rectangleTotalHeight;
    }
    private static ArrayList<String> qryIds ;
    private static Map <String,String> qrylen ;
    private static Map <String,String> reflen ;
    public static void setQueryList(Map<String,String> qryIDs) {
        qryIds=new ArrayList<>(qryIDs.keySet());
        qrylen=new HashMap<>(qryIDs);
    }
    public ArrayList<String> getQueryList(){
        return qryIds;
    }
    public static Map<String,String> getQueryLen(){
        return qrylen;
    }
    public static void setRefList(Map<String,String> refIDs) {
        reflen=new HashMap<>(refIDs);
    }
    public static Map<String,String> getRefLen(){
        return reflen;
    }
}