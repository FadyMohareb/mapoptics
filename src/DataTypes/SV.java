package DataTypes;

/*
 * @author Anisha
 * Class to create SV objects with SV properties
 *
 * */

abstract public class SV {
    private double qryStartPos;
    private double qryEndPos;
    private double refStartPos;
    private double refEndPos;
    private double confidence;
    private double svSize;
    private String qryID;
    private String refID1;
    private String refID2;
    private String type;
    private String xmapID1;
    private String xmapID2;
    private String orientation;


    public SV() {
        this.qryID = "";
        this.refID1 = "";
        this.refID2 = "";
        this.qryStartPos = 0.0;
        this.qryEndPos = 0.0;
        this.refStartPos = 0.0;
        this.refEndPos = 0.0;
        this.confidence = 0.0;
        this.type = "";
        this.xmapID1 = "";
        this.xmapID2 = "";
        this.svSize = 0.0;
        this.orientation = "-";
    }


    abstract void setSVRegion(SV sv);


    public String getQryID() {
        return qryID;
    }

    public void setQryID(String qryID) {
        this.qryID = qryID;
    }

    public String getRefID1() {
        return refID1;
    }

    public void setRefID1(String refID1){
        this.refID1 = refID1;
    }

    public String getRefID2() {
        return refID2;
    }

    public void setRefID2(String refID2) {
        this.refID2 = refID2;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public double getQryStartPos() {
        return qryStartPos;
    }

    public void setQryStartPos(double qryStartPos) {
        this.qryStartPos = qryStartPos;
    }

    public double getQryEndPos() {
        return qryEndPos;
    }

    public void setQryEndPos(double qryEndPos) {
        this.qryEndPos = qryEndPos;
    }

    public double getRefStartPos() {
        return refStartPos;
    }

    public void setRefStartPos(double refStartPos) {
        this.refStartPos = refStartPos;
    }

    public double getRefEndPos() {
        return refEndPos;
    }

    public void setRefEndPos(double refEndPos) {
        this.refEndPos = refEndPos;
    }

    public double getSVSize() {
        return svSize;
    }

    public void setSVSize(double svSize) {
        this.svSize = svSize;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {this.type = type; }

    public String getXmapID1() {
        return xmapID1;
    }

    public void setXmapID1(String xmapID1) {
        this.xmapID1 = xmapID1;
    }

    public String getXmapID2() {
        return xmapID2;
    }

    public void setXmapID2(String xmapID2) {
        this.xmapID2 = xmapID2;
    }

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }
}
