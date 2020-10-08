package app.customtenant.statisticsmodule.domain;

import app.configuration.spring.constants.Constants;
import app.customtenant.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.ANNUALLY)
public class AnnuallyPerformerStatistics
        extends AbstractCalendarPerformerStatistic {

    public AnnuallyPerformerStatistics() {
        super(CalendarPerformerEnum.ANNUALLY, Constants.DAY_IN_MS * 365);
    }

}
