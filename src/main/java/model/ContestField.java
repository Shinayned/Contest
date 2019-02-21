package model;

import enums.FieldType;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;

@Embeddable
public class ContestField {
    private String fieldName;

    @Enumerated(EnumType.STRING)
    private FieldType filedType;

    private boolean isObligatory;

    protected ContestField() {}

    public ContestField(String fieldName, FieldType filedType) {
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

    public FieldType getFiledType() {
        return filedType;
    }

    public void setFiledType(FieldType filedType) {
        this.filedType = filedType;
    }

    public boolean isObligatory() {
        return isObligatory;
    }

    public void setObligatory(boolean obligatory) {
        isObligatory = obligatory;
    }
}
