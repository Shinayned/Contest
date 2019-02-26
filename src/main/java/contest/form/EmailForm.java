package contest.form;

import contest.form.enums.FormType;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailForm extends Form {
    private Pattern pattern;

    public EmailForm() {
        super(FormType.EMAIL);
        setPattern();
    }

    private void setPattern() {
        this.pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected boolean specialValidate(String[] values) {
        String email = values[0];

        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }

        return false;
    }
}
