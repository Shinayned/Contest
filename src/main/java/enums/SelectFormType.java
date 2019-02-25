package enums;

public enum SelectFormType {
    SINGLE_SELECT_LIST, MULTIPLE_SELECT_LIST, DROPDOWN_LIST;

    public FormType toFormType() {
        return FormType.valueOf(this.toString());
    }
}
