package contest.form;

import contest.form.enums.FormType;
import org.joda.time.DateTime;

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
    protected boolean specialValidate(String[] values) {
        try {
            String date = values[0];
            DateTime.parse(date);
            return true;
        } catch (Exception exception) {
            return false;
        }
    }
}
