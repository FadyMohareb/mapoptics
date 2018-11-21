package DataTypes;

/*
 * @author Josie
 */
public class LabelInfo {
    private String labelChannel ;
    private String labelPos ;
    private String stdDev ;
    private String coverage ;
    private String occurance ;
    private String chimQuality;

    public String getLabelChannel() {
        return labelChannel;
    }

    public void setLabelChannel(String labelChannel) {
        this.labelChannel = labelChannel;
    }

    public String getLabelPos() {
        return labelPos;
    }

    public void setLabelPos(String labelPos) {
        this.labelPos = labelPos;
    }

    public String getStdDev() {
        return stdDev;
    }

    public void setStdDev(String stdDev) {
        this.stdDev = stdDev;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getOccurance() {
        return occurance;
    }

    public void setOccurance(String occurance) {
        this.occurance = occurance;
    }

    public String getChimQuality() {
        return chimQuality;
    }

    public void setChimQuality(String chimQuality) {
        this.chimQuality = chimQuality;
    }

    public LabelInfo(String labelChannel, String labelPos, String stdDev, String coverage, String occurance) {
        this.labelChannel = labelChannel;
        this.labelPos = labelPos;
        this.stdDev = stdDev;
        this.coverage = coverage;
        this.occurance = occurance;
    }
}
