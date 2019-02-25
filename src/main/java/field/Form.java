package field;

import enums.FormType;
import enums.SimpleFormType;

import java.io.Serializable;

public class Form implements Serializable {
    private String name;
    private FormType type;
    private String title;
    private boolean isObligatory;

    protected Form(String name, FormType type, String title) {
        this.name = name;
        this.type = type;
        this.title = title;

        this.isObligatory = false;
    }

    public Form(String name, SimpleFormType type, String title) {
        this.name = name;
        this.type = type.toFormType();
        this.title = title;

        this.isObligatory = false;
    }

    public String getName() {
        return name;
    }

    public FormType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public boolean isObligatory() {
        return isObligatory;
    }
}
