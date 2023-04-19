package db.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.sql.Date;
import java.time.YearMonth;

@Converter(autoApply = true)
public class YearMonthAtributteConverter implements AttributeConverter<YearMonth, Date> {
    @Override
    public Date convertToDatabaseColumn(YearMonth yearMonth) {
        return yearMonth == null ? null : Date.valueOf(yearMonth.atDay(1));
    }

    @Override
    public YearMonth convertToEntityAttribute(Date date) {
        return date == null ? null : YearMonth.from(date.toLocalDate());
    }
}