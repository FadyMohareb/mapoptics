package UserInterface.ModelsAndRenderers;

import DataTypes.Reference;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author James
 */
public class MapOpticsModel {

    private File refFile, qryFile, xmapFile;
    private String selectedQueryRow;
    private List<Object[]> summaryTableRows;
    private boolean isReversed;
    private List<Double> lengths;
    private List<Double> densities;
    private Map<Integer, Integer> refQueries;
    private List<Reference> references;

    public MapOpticsModel() {
        isReversed = false;
        selectedQueryRow = "";
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

    public void setSummaryTableRows(List<Object[]> summaryTableRows) {
        this.summaryTableRows = summaryTableRows;
    }

    public List<Object[]> getSummaryTableRows() {
        return summaryTableRows;
    }

    public void setLengths(List<Double> lengths) {
        this.lengths = lengths;
    }

    public void setDensities(List<Double> densities) {
        this.densities = densities;
    }

    public String getSelectedQueryRow() {
        return selectedQueryRow;
    }

    public List<Double> getLengths() {
        return lengths;
    }

    public void setSelectedQueryRow(String selectedRow) {
        this.selectedQueryRow = selectedRow;
    }

    public List<Double> getDensities() {
        return densities;
    }

    public void setRefQueries(Map<Integer, Integer> refQueries) {
        this.refQueries = refQueries;
    }

    public void setReferences(List<Reference> references) {
        this.references = references;
    }

    public List<Reference> getReferences() {
        return references;
    }
}
