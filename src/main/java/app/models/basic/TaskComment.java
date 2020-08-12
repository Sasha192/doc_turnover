package app.models.basic;

import app.models.abstr.Comment;
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
public class TaskComment extends Comment implements Serializable {

    @ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.MERGE},
            fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id")
    @ExcludeForJsonComment
    private Task task;

    @Override
    public Set<Long> getPerformerIds() {
        if (performerIds == null) {
            Set<Long> ids = task.getPerformerIds();
            ids.add(task.getTaskOwner().getId());
            this.setPerformerIds(ids);
        }
        return performerIds;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
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
