package app.customtenant.statisticsmodule.domain;

import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.util.Calendar;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.ANNUALLY)
public class AnnuallyPerformerStatistics
        extends AbstractCalendarPerformerStatistic {

    public AnnuallyPerformerStatistics() {
        super(CalendarPerformerEnum.ANNUALLY, null, null);
        Calendar start = getStartOfTheYear();
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.DAY_OF_YEAR, 365);
        setStart(start);
        setEnd(end);
    }

    private Calendar getStartOfTheYear() {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        start.clear(Calendar.MINUTE);
        start.clear(Calendar.SECOND);
        start.clear(Calendar.MILLISECOND);
        start.set(Calendar.DAY_OF_YEAR, 1);
        return start;
    }

}
