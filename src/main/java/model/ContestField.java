package model;

import enums.FormType;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Embeddable
public class ContestField {
    private String fieldName;

    @Enumerated(EnumType.STRING)
    private FormType filedType;

    private boolean isObligatory;

    protected ContestField() {}

    public ContestField(String fieldName, FormType filedType) {
        this.fieldName = fieldName;
        this.filedType = filedType;

        this.isObligatory = false;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public FormType getFiledType() {
        return filedType;
    }

    public void setFiledType(FormType filedType) {
        this.filedType = filedType;
    }

    public boolean isObligatory() {
        return isObligatory;
    }

    public void setObligatory(boolean obligatory) {
        isObligatory = obligatory;
    }
}
