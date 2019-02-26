package contest.form;

import contest.form.enums.FormType;

public class StringForm extends Form implements Cloneable {
    private String placeHolder;
    private int minLength;
    private int maxLength;

    public StringForm( ) {
        super(FormType.STRING);
    }

    public StringForm(String title) {
        super(FormType.STRING, title);
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
    protected boolean specialValidate(String[] values) {
        String value = values[0];

        if(value.length() >= minLength && value.length() <= maxLength)
            return true;

        return false;
    }

    @Override
    protected Object clone() {
        return super.clone();
    }
}
