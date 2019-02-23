package field;

import enums.FormType;

public class StringForm extends Form {
    private String placeHolder;
    private int minLength;
    private int maxLength;

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
}
