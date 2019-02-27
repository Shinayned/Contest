package contest.form;

import contest.form.enums.FormType;
import org.joda.time.DateTime;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateForm extends Form {
    public DateForm() {
        super(FormType.DATE);
    }

    public DateForm(String title) {
        super(FormType.DATE, title);
    }

    @Override
    protected void specialValidate(List<String> values) throws InvalidParameterException {
        if(values.size() != 1)
            throw new InvalidParameterException("Form №" + this.getId() + " is single form.");

        try {
            String date = values.get(0);
            DateTime.parse(date);
        } catch (Exception exception) {
            throw new InvalidParameterException("Form №" + this.getId() + " is date form.");
        }
    }
}
