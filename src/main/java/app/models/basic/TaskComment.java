package app.models.basic;

import app.models.abstr.TaskHolderComment;
import app.models.basic.taskmodels.Task;
import app.models.serialization.ExcludeForJsonComment;
import java.io.Serializable;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@DiscriminatorValue(value = "task_comment")
public class TaskComment
        extends TaskHolderComment
        implements Serializable {

    @ManyToOne(cascade = {CascadeType.REFRESH},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", insertable = false, updatable = false)
    @ExcludeForJsonComment
    private Task task;

    @Override
    public Long getTaskId() {
        if (this.taskId == null) {
            setTaskId(task.getId());
        }
        return taskId;
    }

    @Override
    public Set<Long> getPerformerIds() {
        if (performerIds == null) {
            Set<Long> ids = task.getPerformerIds();
            ids.add(task.getTaskOwner().getId());
            this.setPerformerIds(ids);
        }
        return performerIds;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof TaskComment)) {
            return false;
        }

        TaskComment that = (TaskComment) o;

        return new EqualsBuilder()
                .appendSuper(super.equals(o))
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .appendSuper(super.hashCode())
                .append(getTask())
                .toHashCode();
    }
}
