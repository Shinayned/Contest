package contest.form;

import contest.form.enums.FormType;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailForm extends Form {
    private Pattern pattern;

    public EmailForm(String name) {
        super(name, FormType.EMAIL);
        setPattern();
    }

    private void setPattern() {
        this.pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    }

    @Override
    protected void specialValidate(List<String> values) throws InvalidParameterException {
        if(values.size() != 1)
            throw new InvalidParameterException("Form №" + this.getId() + " is single form.");

        String email = values.get(0);
        Matcher matcher = pattern.matcher(email);

        if (!matcher.matches())
            throw new InvalidParameterException("Form №" + this.getId() + " is email form.");
    }
}
