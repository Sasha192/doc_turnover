package app.statisticsmodule.domain;

import app.configuration.spring.constants.Constants;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.DAILY)
public class DailyPerformerStatistic
        extends AbstractCalendarPerformerStatistic {

    public DailyPerformerStatistic() {
        super(CalendarPerformerEnum.DAILY, Constants.DAY_IN_MS);
    }

    @Override
    public Object clone() {
        return super.clone(new DailyPerformerStatistic());
    }
}
