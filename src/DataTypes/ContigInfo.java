package DataTypes;

/*
 * @author Josie
 */
@Deprecated
public class ContigInfo {
    private final double contigLen ;
    private final LabelInfo[] labelInfo;

    public ContigInfo(double contigLen, LabelInfo[] labelInfo) {
        this.contigLen = contigLen;
        this.labelInfo = labelInfo;
    }
    public double getContigLen() {
        return contigLen;
    }

    public LabelInfo[] getLabelInfo() {
        return labelInfo;
    }

    /*
    public void setLabelInfo(LabelInfo[] labelInfo) {
       this.labelInfo = labelInfo;
    }

    public void setContigLen(double contigLen) {
        this.contigLen = contigLen;
    }
    */

}
