package app.statisticsmodule.domain;

import app.configuration.spring.constants.Constants;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.MONTHLY)
public class MonthlyPerformerStatistic
        extends AbstractCalendarPerformerStatistic {

    public MonthlyPerformerStatistic() {
        super(CalendarPerformerEnum.MONTHLY, Constants.DAY_IN_MS * 30);
    }

}
