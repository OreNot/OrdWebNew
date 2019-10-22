package ucproject.domain;

public class StatusByWorkGroup {

    private String workGroupName;
    private String regStatus;
    private String setRGStatus;
    private String setExecStatus;
    private String returnStatus;
    private String inRGWorkStatus;
    private String inExecWorkStatus;
    private String compliteStatus;

    public StatusByWorkGroup() {
    }

    public StatusByWorkGroup(String workGroupName, String regStatus, String setRGStatus, String setExecStatus, String returnStatus, String inRGWorkStatus, String inExecWorkStatus, String compliteStatus) {
        this.workGroupName = workGroupName;
        this.regStatus = regStatus;
        this.setRGStatus = setRGStatus;
        this.setExecStatus = setExecStatus;
        this.returnStatus = returnStatus;
        this.inRGWorkStatus = inRGWorkStatus;
        this.inExecWorkStatus = inExecWorkStatus;
        this.compliteStatus = compliteStatus;
    }



    public String getWorkGroupName() {
        return workGroupName;
    }

    public void setWorkGroupName(String workGroupName) {
        this.workGroupName = workGroupName;
    }

    public String getRegStatus() {
        return regStatus;
    }

    public void setRegStatus(String regStatus) {
        this.regStatus = regStatus;
    }

    public String getSetRGStatus() {
        return setRGStatus;
    }

    public void setSetRGStatus(String setRGStatus) {
        this.setRGStatus = setRGStatus;
    }

    public String getSetExecStatus() {
        return setExecStatus;
    }

    public void setSetExecStatus(String setExecStatus) {
        this.setExecStatus = setExecStatus;
    }

    public String getReturnStatus() {
        return returnStatus;
    }

    public void setReturnStatus(String returnStatus) {
        this.returnStatus = returnStatus;
    }

    public String getInRGWorkStatus() {
        return inRGWorkStatus;
    }

    public void setInRGWorkStatus(String inRGWorkStatus) {
        this.inRGWorkStatus = inRGWorkStatus;
    }

    public String getInExecWorkStatus() {
        return inExecWorkStatus;
    }

    public void setInExecWorkStatus(String inExecWorkStatus) {
        this.inExecWorkStatus = inExecWorkStatus;
    }

    public String getCompliteStatus() {
        return compliteStatus;
    }

    public void setCompliteStatus(String compliteStatus) {
        this.compliteStatus = compliteStatus;
    }
}
