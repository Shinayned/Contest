package enums;

public enum SimpleFormType {
    EMAIL, PHONE_NUMBER, DATA;

    public FormType toFormType() {
        return FormType.valueOf(this.toString());
    }
}
