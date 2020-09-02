package app.statisticsmodule.abstr;

import app.statisticsmodule.domain.CalendarPerformerEnum;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "cal_perf_stat_type")
@Table(name = "cal_perf_statistics")
public abstract class AbstractCalendarPerformerStatistic
        extends AbstractPerformerStatistics {

    @Transient
    protected CalendarPerformerEnum type;

    @Column(name = "creation_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date creationDate;

    @Column(name = "expired")
    private Boolean expired;

    public AbstractCalendarPerformerStatistic(CalendarPerformerEnum type) {
        this.type = type;
    }

    public abstract CalendarPerformerEnum getType();

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getExpired() {
        return expired;
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
    }
}
