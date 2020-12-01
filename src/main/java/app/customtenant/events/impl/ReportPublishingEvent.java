package app.customtenant.events.impl;

import app.customtenant.events.Event;
import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue(value = "report_event")
public class ReportPublishingEvent extends Event {

    @Column(name = "task_id")
    private Long taskId;

    @Column(name = "report_id")
    private Long reportId;

    @Column(name = "task_name")
    private String taskName;

    public ReportPublishingEvent(String taskName) {
        this();
        this.taskName = taskName;
    }

    public ReportPublishingEvent() {
        setEventTypeEnum(EventType.REPORT_PUB);
        setMessage("Опубліковано звіт");
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
