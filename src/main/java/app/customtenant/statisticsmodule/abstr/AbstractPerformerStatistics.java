package app.customtenant.statisticsmodule.abstr;

import static app.customtenant.statisticsmodule.domain.StatisticsTypeEnum.PERFORMER_DEFAULT_STATISTIC;

import app.customtenant.models.abstr.IdentityBaseEntity;
import app.customtenant.models.basic.Performer;
import app.customtenant.models.serialization.ExcludeForJsonPerformer;
import app.customtenant.statisticsmodule.domain.StatisticsTypeEnum;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractPerformerStatistics
        extends IdentityBaseEntity
        implements IStatistics {

    private static StatisticsTypeEnum type = PERFORMER_DEFAULT_STATISTIC;

    @Column(name = "performer_id")
    private Long performerId;

    @JoinColumn(name = "performer_id",
            insertable = false,
            updatable = false)
    @ManyToOne(cascade = {
            CascadeType.REMOVE,
            CascadeType.DETACH })
    private Performer performer;

    @Override
    public StatisticsTypeEnum getStatisticType() {
        return type;
    }

    public Long getPerformerId() {
        return performerId;
    }

    public void setPerformerId(Long performerId) {
        this.performerId = performerId;
    }

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }
}
