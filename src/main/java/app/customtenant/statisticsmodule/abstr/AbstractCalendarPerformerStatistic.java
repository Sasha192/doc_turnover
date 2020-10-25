package app.customtenant.statisticsmodule.abstr;

import app.customtenant.statisticsmodule.domain.CalendarPerformerEnum;
import java.util.Calendar;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "cal_perf_stat_type")
@Table(name = "cal_perf_statistics")
public abstract class AbstractCalendarPerformerStatistic
        extends AbstractPerformerStatistics {

    @Column(name = "calendar_enum_type")
    @Enumerated(EnumType.ORDINAL)
    protected CalendarPerformerEnum type;

    @Column(name = "creation_calendar")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar start;

    @Column(name = "expiration_calendar")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar end;

    @Column(name = "amount")
    private Integer amount = 0;

    @Column(name = "new_status")
    private Integer newStatus = 0;

    @Column(name = "overdue")
    private Integer overdue = 0;

    @Column(name = "completed")
    private Integer completed = 0;

    @Column(name = "inprogress")
    private Integer inprogress = 0;

    @Column(name = "onhold")
    private Integer onhold = 0;

    @Column(name = "expired_deadline")
    private Integer expiredDeadline = 0;

    public AbstractCalendarPerformerStatistic(CalendarPerformerEnum type,
                                              Calendar start, Calendar end) {
        this.type = type;
        this.start = start;
        this.end = end;
    }

    public AbstractCalendarPerformerStatistic(CalendarPerformerEnum allTime, long expiration) {
        Calendar start = Calendar.getInstance();
        Calendar end = (Calendar) start.clone();
        if (expiration >= 0) {
            end.setTimeInMillis(start.getTimeInMillis() + expiration);
        } else {
            end.setTimeInMillis(Long.MAX_VALUE);
        }
        setStart(start);
        setEnd(end);
    }

    public Calendar getStart() {
        return start;
    }

    protected void setStart(Calendar start) {
        this.start = start;
    }

    public Calendar getEnd() {
        return end;
    }

    protected void setEnd(Calendar end) {
        this.end = end;
    }

    public CalendarPerformerEnum getType() {
        return type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public Integer getCompleted() {
        return completed;
    }

    public void setCompleted(Integer completed) {
        this.completed = completed;
    }

    public Integer getInprogress() {
        return inprogress;
    }

    public void setInprogress(Integer inprogress) {
        this.inprogress = inprogress;
    }

    public Integer getOnhold() {
        return onhold;
    }

    public void setOnhold(Integer onhold) {
        this.onhold = onhold;
    }

    public Integer getExpiredDeadline() {
        return expiredDeadline;
    }

    public void setExpiredDeadline(Integer expiredDeadline) {
        this.expiredDeadline = expiredDeadline;
    }

    public void incrementInProgress() {
        this.inprogress = this.inprogress + 1;
    }

    public void incrementOnhold() {
        this.onhold = this.onhold + 1;
    }

    public void incrementCompleted() {
        this.completed = this.completed + 1;
    }

    public void incrementNew() {
        this.newStatus = this.newStatus + 1;
    }

    public void incrementOverdue() {
        this.overdue = this.overdue + 1;
    }

    public void incrementExpiredDeadline() {
        this.expiredDeadline = this.expiredDeadline + 1;
    }

    public void decrementInProgress() {
        this.inprogress = this.inprogress - 1;
    }

    public void decrementOnhold() {
        this.onhold = this.onhold - 1;
    }

    public void decrementNew() {
        this.newStatus = this.newStatus - 1;
    }

    public void decrementOverdue() {
        this.overdue = this.overdue - 1;
    }

    public void incrementAmount() {
        this.amount = this.amount + 1;
    }

    public void decrementCompleted() {
        this.completed = this.completed + 1;
    }
}
