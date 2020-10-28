package app.controllers.dto;

import javax.validation.constraints.NotNull;

public class TaskDto {

    @NotNull
    private String name;

    @NotNull
    private String dateControl;

    @NotNull
    private String deadline;

    @NotNull
    private String description;

    @NotNull
    private String[] keyWords;

    @NotNull
    private Long[] performerList;

    @NotNull
    private Long[] docList;

    @NotNull
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

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getKeyWords() {
        return keyWords;
    }

    public void setKeyWords(String[] keyWords) {
        this.keyWords = keyWords;
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



