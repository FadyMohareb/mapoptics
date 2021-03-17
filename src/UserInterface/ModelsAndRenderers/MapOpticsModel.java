package UserInterface.ModelsAndRenderers;

import DataTypes.Reference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author James
 */
public class MapOpticsModel {

    private File refFile, qryFile, xmapFile;
    private String selectedRefID;
    private Reference selectedRef;
    private boolean isReversed;
    private List<Double> lengths;
    private List<Double> densities;
    private List<Reference> references;
    private double rectangleTotalWidth;

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
        this.selectedRefID = selectedRow;
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

    public void totalRectangleWidth(double rectangleTotalWidth) {
        this.rectangleTotalWidth = rectangleTotalWidth;
    }

    public double getRectangleTotalWidth() {
        return rectangleTotalWidth;
    }
}
