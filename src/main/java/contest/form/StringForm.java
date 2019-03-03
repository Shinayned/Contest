package contest.form;

import contest.form.enums.FormType;

import java.security.InvalidParameterException;
import java.util.List;

public class StringForm extends Form implements Cloneable {
    private String placeHolder;
    private int minLength;
    private int maxLength;

    public StringForm(String name) {
        super(name, FormType.STRING);
    }

    public StringForm(String name, String title) {
        super(name, FormType.STRING, title);
    }

    public String getPlaceHolder() {
        return placeHolder;
    }

    public void setPlaceHolder(String placeHolder) {
        this.placeHolder = placeHolder;
    }

    public int getMinLength() {
        return minLength;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    protected void specialValidate(List<String> values) throws InvalidParameterException {
        if(values.size() != 1)
            throw new InvalidParameterException("Form №" + this.getId() + " is single form.");

        String value = values.get(0);

        if (value.length() < minLength)
            throw new InvalidParameterException("Form №" + this.getId() + " has min length " + minLength + ".");

        if (maxLength != 0 && value.length() > maxLength)
            throw new InvalidParameterException("Form №" + this.getId() + " has max length" + maxLength + ".");
    }

    @Override
    protected Object clone() {
        return super.clone();
    }
}
