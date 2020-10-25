package app.customtenant.statisticsmodule.abstr;

import static app.customtenant.statisticsmodule.domain.StatisticsTypeEnum.PERFORMER_DEFAULT_STATISTIC;

import app.customtenant.models.abstr.IdentityBaseEntity;
import app.customtenant.statisticsmodule.domain.StatisticsTypeEnum;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractPerformerStatistics
        extends IdentityBaseEntity
        implements IStatistics {

    private static StatisticsTypeEnum type = PERFORMER_DEFAULT_STATISTIC;

    @Column(name = "performer_id")
    private Long performerId;

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

}
