package UserInterface.ModelsAndRenderers;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author James
 */
public class MapOpticsModel {

    private File refFile, qryFile, xmapFile;
    private String selectedRow;
    private List<Object[]> summaryTableRows;
    private boolean isReversed;
    private List<Double> lengths;
    private List<Double> densities;

    public MapOpticsModel() {
        isReversed = false;
        selectedRow = "";
        lengths = new ArrayList<>();
        densities = new ArrayList<>();
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

    public String getSelectedRow() {
        return selectedRow;
    }

    public List<Double> getLengths() {
        return lengths;
    }

    public void setSelectedRow(String selectedRow) {
        this.selectedRow = selectedRow;
    }

    public List<Double> getDensities() {
        return densities;
    }
}
