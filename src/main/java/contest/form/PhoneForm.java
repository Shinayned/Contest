package contest.form;

import contest.form.enums.FormType;

import java.security.InvalidParameterException;
import java.util.List;

public class PhoneForm extends Form {
    public PhoneForm() {
        super(FormType.PHONE_NUMBER);
    }

    public PhoneForm(String title) {
        super(FormType.PHONE_NUMBER, title);
    }

    @Override
    protected void specialValidate(List<String> values) throws InvalidParameterException {}
}
