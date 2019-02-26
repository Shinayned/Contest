package contest.form;

import contest.form.enums.SelectFormType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectForm extends Form implements Cloneable {
    private List<String> fields;

    public SelectForm(SelectFormType type, List<String> fields) {
        super(type.toFormType());

        this.fields = new ArrayList<>(fields);
    }

    public SelectForm(SelectFormType type, String title, List<String> fields) {
        super(type.toFormType(), title);

        this.fields = new ArrayList<>(fields);
    }

    public List<String> getFields() {
        return new ArrayList<>(fields);
    }

    public void setFields(List<String> fields) {
        this.fields = new ArrayList<>(fields);
    }

    @Override
    protected boolean specialValidate(String[] values) {
        if(this.getType() == SelectFormType.MULTIPLE_SELECT_LIST.toFormType()) {
            for(String value : values) {
                if (!fields.contains(value))
                    return false;
            }
        } else {
            String value = values[0];

            if(!fields.contains(value))
                return false;
        }

        return true;
    }

    @Override
    protected Object clone() {
        SelectForm clone = (SelectForm) super.clone();
        clone.fields = new ArrayList<>(fields);

        return clone;
    }
}

