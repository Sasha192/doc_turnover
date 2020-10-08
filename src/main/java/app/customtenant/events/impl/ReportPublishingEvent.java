package app.customtenant.events.impl;

import app.customtenant.events.Event;
import app.customtenant.models.basic.Report;
import app.customtenant.models.serialization.ExcludeForJsonEvent;
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

    @ManyToOne(cascade = {CascadeType.REFRESH},
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
