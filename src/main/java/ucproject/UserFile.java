package ucproject;

public class UserFile {

    String orgDate;
    String filePath;

    public UserFile() {
    }

    public UserFile(String orgDate, String filePath) {
        this.orgDate = orgDate;
        this.filePath = filePath;
    }

    public String getOrgDate() {
        return orgDate;
    }

    public void setOrgDate(String orgDate) {
        this.orgDate = orgDate;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
