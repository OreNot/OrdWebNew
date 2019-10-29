package ucproject.domain;

import java.util.List;

public class Mail {

    private String theme;
    private String text;
    private List<String> recepientList;

    public Mail() {
    }

    public Mail(String theme, String text, List<String> recepientList) {
        this.theme = theme;
        this.text = text;
        this.recepientList = recepientList;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<String> getRecepientList() {
        return recepientList;
    }

    public void setRecepientList(List<String> recepientList) {
        this.recepientList = recepientList;
    }
}
