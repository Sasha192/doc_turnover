package app.statisticsmodule.domain;

import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.ALL_TIME)
public class AllPerformerStatistic
        extends AbstractCalendarPerformerStatistic {

    public AllPerformerStatistic() {
        super(CalendarPerformerEnum.ALL_TIME);
    }

}
