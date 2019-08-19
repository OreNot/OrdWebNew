package ucproject;

import java.util.ArrayList;
import java.util.List;

public class UserCatalog {

    private String fio;
    private List<UserFile> userFiles = new ArrayList<>();

    public UserCatalog() {
    }

    public UserCatalog(String fio, List<UserFile> userFiles) {
        this.fio = fio;
        this.userFiles = userFiles;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public List<UserFile> getUserFiles() {
        return userFiles;
    }

    public void setUserFiles(List<UserFile> userFiles) {
        this.userFiles = userFiles;
    }
}
