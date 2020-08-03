package app.controllers.dto;

import javax.validation.constraints.NotNull;

public class CommentDto {

    public interface New {

    }

    @NotNull
    private Integer todoId;

    @NotNull
    private String comment;

    /*@NotNull
    @Null(groups = {New.class})
    private String performerName;

    @NotNull
    @Null(groups = {New.class})
    private Long performerId;

    @Null(groups = {New.class})
    private String performerImgPath;

    @NotNull
    @Null(groups = {New.class})
    private String date;

    @NotNull
    @Null(groups = {New.class})
    private String time;*/

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
