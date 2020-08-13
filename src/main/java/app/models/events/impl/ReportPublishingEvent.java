package app.models.events.impl;

import app.models.basic.Report;
import app.models.basic.Task;
import app.models.events.Event;
import app.models.serialization.ExcludeForJsonEvent;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "report_event")
public class ReportPublishingEvent extends Event {

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "report_id", insertable = false, updatable = false)
    @ExcludeForJsonEvent
    private Report report;

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "report_id")
    private Long reportId;

    public ReportPublishingEvent() {
        setEventTypeEnum(EventType.REPORT_PUB);
    }

    public Report getReport() {
        return report;
    }

    public Long getReportId() {
        return reportId;
    }

    public void setReportId(Long reportId) {
        this.reportId = reportId;
    }

    public Long getTaskId() {
        return taskId;
    }

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
