package app.controllers.dto;

import java.util.Set;
import javax.validation.constraints.NotNull;

public class TaskDto {

    @NotNull
    private String name;

    @NotNull
    private String dateControl;

    @NotNull
    private String deadline;

    private transient String priority;

    @NotNull
    private String description;

    @NotNull
    private Object[] keyWords;

    @NotNull
    private Long[] performerList;

    @NotNull
    private Long[] docList;

    @NotNull
    private String status;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getDateControl() {
        return this.dateControl;
    }

    public void setDateControl(final String dateControl) {
        this.dateControl = dateControl;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(final String description) {
        this.description = description;
    }

    public Object[] getKeyWords() {
        return keyWords;
    }

    public Long[] getPerformerList() {
        return performerList;
    }

    public Long[] getDocList() {
        return docList;
    }

    public void setKeyWords(final Set<String> keyWords) {
        this.keyWords = new String[keyWords.size()];
        int i = 0;
        for (String key : keyWords) {
            this.keyWords[i++] = key;
        }
    }

    public Long[] getPerformerId() {
        return this.performerList;
    }

    public void setPerformerId(final Set<Long> performerIds) {
        this.performerList = new Long[performerIds.size()];
        int i = 0;
        for (Long val : performerIds) {
            this.performerList[i++] = val;
        }
    }

    public Long[] getDocsId() {
        return this.docList;
    }

    public void setDocsId(final Set<Long> docsId) {
        this.docList = new Long[docsId.size()];
        int i = 0;
        for (Long val : docsId) {
            this.docList[i++] = val;
        }
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(final String status) {
        this.status = status;
    }
}
