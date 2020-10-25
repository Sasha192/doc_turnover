package app.customtenant.statisticsmodule.domain;

import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import java.util.Calendar;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.WEEKLY)
public class WeeklyPerformerStatistics
        extends AbstractCalendarPerformerStatistic {

    public WeeklyPerformerStatistics() {
        super(CalendarPerformerEnum.WEEKLY, null, null);
        Calendar start = getStartOfTheWeek();
        Calendar end = (Calendar) start.clone();
        end.add(Calendar.WEEK_OF_YEAR, 1);
        setStart(start);
        setEnd(end);
    }

    private Calendar getStartOfTheWeek() {
        Calendar start = Calendar.getInstance();
        start.set(Calendar.HOUR_OF_DAY, 0); // ! clear would not reset the hour of day !
        start.clear(Calendar.MINUTE);
        start.clear(Calendar.SECOND);
        start.clear(Calendar.MILLISECOND);
        start.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return start;
    }
}
