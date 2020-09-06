package app.statisticsmodule.domain;

import app.configuration.spring.constants.Constants;
import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.ANNUALLY)
public class AnnuallyPerformerStatistics
        extends AbstractCalendarPerformerStatistic {

    public AnnuallyPerformerStatistics() {
        super(CalendarPerformerEnum.ANNUALLY, Constants.DAY_IN_MS * 365);
    }

    @Override
    public Object clone() {
        return super.clone(new AnnuallyPerformerStatistics());
    }
}
