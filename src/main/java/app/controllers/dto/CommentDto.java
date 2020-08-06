package app.controllers.dto;

import javax.validation.constraints.NotNull;

public class CommentDto {

    @NotNull
    private Integer todoId;

    @NotNull
    private String comment;

    public Integer getTodoId() {
        return todoId;
    }

    public void setTodoId(Integer todoId) {
        this.todoId = todoId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
