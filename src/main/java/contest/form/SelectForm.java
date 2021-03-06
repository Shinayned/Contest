package contest.form;

import contest.form.enums.SelectFormType;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SelectForm extends Form implements Cloneable {
    private List<String> fields;

    public SelectForm(String name, SelectFormType type, List<String> fields) {
        super(name, type.toFormType());

        this.fields = new ArrayList<>(fields);
    }

    public SelectForm(String name, SelectFormType type, String title, List<String> fields) {
        super(name, type.toFormType(), title);

        this.fields = new ArrayList<>(fields);
    }

    public List<String> getFields() {
        return new ArrayList<>(fields);
    }

    public void setFields(List<String> fields) {
        this.fields = new ArrayList<>(fields);
    }

    @Override
    protected void specialValidate(List<String> values) throws InvalidParameterException {
        boolean isSingleChoiceList = this.getType() != SelectFormType.MULTIPLE_SELECT_LIST.toFormType();

        if (isSingleChoiceList && values.size() != 1)
            throw new InvalidParameterException("Form #" + this.getId() + " has only one parameter.");

        for (String value : values) {
            if (!fields.contains(value))
                throw new InvalidParameterException("Form #" + this.getId() + " hasn't '" + value + "'.");
        }
    }

    @Override
    protected Object clone() {
        SelectForm clone = (SelectForm) super.clone();
        clone.fields = new ArrayList<>(fields);

        return clone;
    }
}

