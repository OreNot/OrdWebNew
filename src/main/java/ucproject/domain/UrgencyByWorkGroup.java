package ucproject.domain;

public class UrgencyByWorkGroup {

    private String workGroupName;
    private String mostUrgency;
    private String urgency;
    private String standart;
    private String minUrgency;

    public UrgencyByWorkGroup() {
    }

    public UrgencyByWorkGroup(String workGroupName, String mostUrgency, String urgency, String standart, String minUrgency) {
        this.workGroupName = workGroupName;
        this.mostUrgency = mostUrgency;
        this.urgency = urgency;
        this.standart = standart;
        this.minUrgency = minUrgency;
    }

    public String getMostUrgency() {
        return mostUrgency;
    }

    public String getWorkGroupName() {
        return workGroupName;
    }

    public void setWorkGroupName(String workGroupName) {
        this.workGroupName = workGroupName;
    }

    public void setMostUrgency(String mostUrgency) {
        this.mostUrgency = mostUrgency;
    }

    public String getUrgency() {
        return urgency;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    public String getStandart() {
        return standart;
    }

    public void setStandart(String standart) {
        this.standart = standart;
    }

    public String getMinUrgency() {
        return minUrgency;
    }

    public void setMinUrgency(String minUrgency) {
        this.minUrgency = minUrgency;
    }
}
