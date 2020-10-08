package app.customtenant.statisticsmodule.abstr;

import app.customtenant.statisticsmodule.domain.CalendarPerformerEnum;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "cal_perf_stat_type")
@Table(name = "cal_perf_statistics")
public abstract class AbstractCalendarPerformerStatistic
        extends AbstractPerformerStatistics {

    @Column(name = "calendar_enum_type")
    @Enumerated(EnumType.ORDINAL)
    protected CalendarPerformerEnum type;

    @Transient
    private long expirationTime;

    @Column(name = "creation_timestamp")
    private Timestamp creationDate;

    @Column(name = "expired")
    private Boolean expired;

    @Column(name = "amount")
    private Integer amount = 0;

    @Column(name = "new_status")
    private Integer newS = 0;

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

    public AbstractCalendarPerformerStatistic(CalendarPerformerEnum type) {
        this(type, -1);
    }

    public AbstractCalendarPerformerStatistic(CalendarPerformerEnum type,
                                              long expirationTime) {
        this.type = type;
        this.expirationTime = expirationTime;
        this.creationDate = Timestamp.valueOf(LocalDateTime.now());
    }

    public CalendarPerformerEnum getType() {
        return type;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Boolean getExpired() {
        if (expirationTime < 0) {
            return false;
        }
        long now = System.currentTimeMillis();
        long crDate = getCreationDate().getTime();
        if (now - crDate < expirationTime) {
            return false;
        } else {
            return true;
        }
    }

    public void setExpired(Boolean expired) {
        this.expired = expired;
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
        this.newS = this.newS + 1;
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
        this.newS = this.newS - 1;
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
