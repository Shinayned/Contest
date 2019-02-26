package contest.form;

import contest.form.enums.FormType;

public class PhoneForm extends Form {
    public PhoneForm() {
        super(FormType.PHONE_NUMBER);
    }

    public PhoneForm(String title) {
        super(FormType.PHONE_NUMBER, title);
    }

    @Override
    protected boolean specialValidate(String[] values) {
        return true;
    }
}
