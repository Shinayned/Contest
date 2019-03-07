package converter;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class DateTimeConverter implements Converter<String, DateTime> {
    @Override
    public DateTime convert(String source) {
        if (source == null || source.isEmpty())
            return null;

        String datePlusTime = "yyyy-MM-dd HH:mm";
        DateTimeFormatter formatter = DateTimeFormat.forPattern(datePlusTime);

        if (source.length() == datePlusTime.length())
            return DateTime.parse(source, formatter);

        return new DateTime(source);
    }
}
