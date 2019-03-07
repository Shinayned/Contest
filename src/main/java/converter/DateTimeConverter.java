package converter;

import org.joda.time.DateTime;
import org.springframework.core.convert.converter.Converter;

public class DateTimeConverter implements Converter<String, DateTime> {
    @Override
    public DateTime convert(String s) {
        return new DateTime(s);
    }
}
