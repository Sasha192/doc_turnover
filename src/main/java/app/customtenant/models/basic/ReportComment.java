package app.customtenant.models.basic;

import app.customtenant.models.abstr.TaskHolderComment;
import app.customtenant.models.basic.taskmodels.Task;
import app.customtenant.models.serialization.ExcludeForJsonComment;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@DiscriminatorValue(value = "report_comment")
public class ReportComment
        extends TaskHolderComment {

    @ManyToOne(cascade = {CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id",
            insertable = false,
            updatable = false)
    @ExcludeForJsonComment
    private Report report;

    @Column(name = "report_id")
    private long reportId;

    @Override
    public Set<Long> getPerformerIds() {
        if (performerIds == null) {
            Task task = report.getTask();
            Set<Long> ids = task.getPerformerIds();
            ids.add(task.getTaskOwner().getId());
            this.setPerformerIds(ids);
        }
        return performerIds;
    }

    @Override
    public Long getTaskId() {
        if (this.taskId == null) {
            setTaskId(report.getTask().getId());
        }
        return this.taskId;
    }

    public Report getReport() {
        return report;
    }

    public void setReport(Report report) {
        this.report = report;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof ReportComment)) {
            return false;
        }

        ReportComment that = (ReportComment) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .append(getReport(), that.getReport())
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getReport())
                .toHashCode();
    }
}
