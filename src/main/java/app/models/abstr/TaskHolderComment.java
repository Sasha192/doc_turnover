package app.models.abstr;

import app.models.serialization.ExcludeForJsonComment;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "comment_type")
@Table(name = "comment_post")
public abstract class TaskHolderComment
        extends SuperComment {

    @Column(name = "task_id")
    protected Long taskId;

    public abstract Long getTaskId();

    public void setTaskId(Long taskId) {
        this.taskId = taskId;
    }
}
