package app.controllers.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

public class TaskDto {

    public interface NewTask {

    }

    @NotNull
    private String name;

    @NotNull
    private String dateControl;

    @NotNull
    private String dateDeadline;

    @NotNull
    private String description;

    @NotNull
    private Long[] performerList;

    @NotNull
    private Long[] docList;

    @Null(groups = {NewTask.class})
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateControl() {
        return dateControl;
    }

    public void setDateControl(String dateControl) {
        this.dateControl = dateControl;
    }

    public String getDateDeadline() {
        return dateDeadline;
    }

    public void setDateDeadline(String dateDeadline) {
        this.dateDeadline = dateDeadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long[] getPerformerList() {
        return performerList;
    }

    public void setPerformerList(Long[] performerList) {
        this.performerList = performerList;
    }

    public Long[] getDocList() {
        return docList;
    }

    public void setDocList(Long[] docList) {
        this.docList = docList;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}



