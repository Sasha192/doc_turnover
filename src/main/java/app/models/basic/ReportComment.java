package app.models.basic;

import app.models.abstr.Comment;
import app.models.serialization.ExcludeForJsonComment;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@DiscriminatorValue(value = "report_comment")
public class ReportComment extends Comment {

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id")
    @ExcludeForJsonComment
    private Report report;

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
