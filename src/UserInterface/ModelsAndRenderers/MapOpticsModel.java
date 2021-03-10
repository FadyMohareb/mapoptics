package UserInterface.ModelsAndRenderers;

import java.io.File;
import java.util.List;

/**
 * @author James
 */
public class MapOpticsModel {

    private File refFile, qryFile, xmapFile;
    private String selectedRow;
    private List<Object[]> summaryTableRows;
    private boolean isReversed;

    public MapOpticsModel() {
        isReversed = false;
    }

    public void swapRefQry() {
        File oldRef = refFile;
        refFile = qryFile;
        qryFile = oldRef;
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
}
