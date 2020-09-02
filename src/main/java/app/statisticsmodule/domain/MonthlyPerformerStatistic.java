package app.statisticsmodule.domain;

import app.statisticsmodule.abstr.AbstractCalendarPerformerStatistic;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(CalendarPerformerEnum.Values.MONTHLY)
public class MonthlyPerformerStatistic
        extends AbstractCalendarPerformerStatistic {

    @Column(name = "amount")
    private Long amount;

    @Column(name = "completed")
    private Long completed;

    @Column(name = "expired_deadline")
    private Long expiredDeadline;

    public MonthlyPerformerStatistic() {
        super(CalendarPerformerEnum.MONTHLY);
    }

    @Override
    public CalendarPerformerEnum getType() {
        return type;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getCompleted() {
        return completed;
    }

    public void setCompleted(Long completed) {
        this.completed = completed;
    }

    public Long getExpiredDeadline() {
        return expiredDeadline;
    }

    public void setExpiredDeadline(Long expiredDeadline) {
        this.expiredDeadline = expiredDeadline;
    }
}
