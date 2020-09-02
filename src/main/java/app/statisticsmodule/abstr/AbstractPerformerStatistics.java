package app.statisticsmodule.abstr;

import static app.statisticsmodule.domain.StatisticsTypeEnum.PERFORMER_DEFAULT_STATISTIC;

import app.models.abstr.IdentityBaseEntity;
import app.models.basic.Performer;
import app.models.mysqlviews.BriefPerformer;
import app.models.serialization.ExcludeForJsonPerformer;
import app.statisticsmodule.domain.StatisticsTypeEnum;
import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractPerformerStatistics
        extends IdentityBaseEntity
        implements IStatistics {

    private static StatisticsTypeEnum type = PERFORMER_DEFAULT_STATISTIC;

    @ExcludeForJsonPerformer
    @JoinColumn(name = "performer_id", insertable = false, updatable = false)
    @ManyToOne(cascade = {CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    private Performer performer;

    @JoinColumn(name = "performer_id", insertable = false, updatable = false)
    @ManyToOne(cascade = {CascadeType.REFRESH})
    private BriefPerformer briefPerformer;

    @Override
    public StatisticsTypeEnum getStatisticType() {
        return type;
    }

    public Performer getPerformer() {
        return performer;
    }

    public void setPerformer(Performer performer) {
        this.performer = performer;
    }

    public BriefPerformer getBriefPerformer() {
        return briefPerformer;
    }

    public void setBriefPerformer(BriefPerformer briefPerformer) {
        this.briefPerformer = briefPerformer;
    }
}
