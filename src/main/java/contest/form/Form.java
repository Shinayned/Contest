package contest.form;

import contest.form.enums.FormType;
import exception.BadRequestException;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.List;

public abstract class Form implements Serializable, Cloneable {
    private int id;
    private FormType type;
    private String title;
    private String description;
    private boolean isObligatory;

    protected Form(FormType type) {
        this.type = type;
        this.isObligatory = false;
    }

    protected Form(FormType type, String title) {
        this.type = type;
        this.title = title;

        this.isObligatory = false;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
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

    public void validate(List<String> values) throws InvalidParameterException {
        boolean noValues = (values == null || values.isEmpty());

        if (noValues) {
            if (this.isObligatory())
                throw new InvalidParameterException("Form №" + this.id + " is required");
        }

        specialValidate(values);
    }

    protected abstract void specialValidate(List<String> values) throws InvalidParameterException;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Form form = (Form) o;

        if (id != form.id) return false;
        if (isObligatory != form.isObligatory) return false;
        if (type != form.type) return false;
        if (title != null ? !title.equals(form.title) : form.title != null) return false;
        return description != null ? description.equals(form.description) : form.description == null;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + type.hashCode();
        result = 31 * result + (title != null ? title.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (isObligatory ? 1 : 0);
        return result;
    }

    @Override
    protected Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            // this shouldn't happen, since we are Cloneable
            throw new InternalError(e);
        }
    }
}
