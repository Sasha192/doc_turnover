package app.controllers.dto;

import javax.validation.constraints.NotNull;

public class CommentDto {

    @NotNull
    private Integer taskId;

    @NotNull
    private String comment;

    public Integer getTaskId() {
        return taskId;
    }

    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
