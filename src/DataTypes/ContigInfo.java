package DataTypes;

/*
 * @author Josie
 */
public class ContigInfo {
    
    private double contigLen ;
    private LabelInfo[] labelInfo;

    public double getContigLen() {
        return contigLen;
    }

    public void setContigLen(double contigLen) {
        this.contigLen = contigLen;
    }

    public LabelInfo[] getLabelInfo() {
        return labelInfo;
    }

    public void setLabelInfo(LabelInfo[] labelInfo) {
        this.labelInfo = labelInfo;
    }

    public ContigInfo(double contigLen, LabelInfo[] labelInfo) {
        this.contigLen = contigLen;
        this.labelInfo = labelInfo;
    }
    
}
