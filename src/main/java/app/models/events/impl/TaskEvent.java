package app.models.events.impl;

import app.models.events.Event;
import app.models.mysqlviews.BriefTask;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue(value = "task_event")
public class TaskEvent extends Event {

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE})
    @JoinColumn(name = "task_id")
    private BriefTask task;

    public BriefTask getTask() {
        return task;
    }

    public void setTask(BriefTask task) {
        this.task = task;
    }
}
