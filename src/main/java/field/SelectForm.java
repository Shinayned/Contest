package field;

import enums.FormType;
import enums.SelectFormType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SelectForm extends Form {
    private List<String> fields;
    private String description;

    public SelectForm(String name, SelectFormType type, String title, List<String> fields) {
        super(name, type.toFormType(), title);

        this.fields = new ArrayList<>(fields);
    }

    public List<String> getFields() {
        return new ArrayList<>(fields);
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
