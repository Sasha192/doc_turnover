package app.controllers.dto;

import java.util.Set;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.ArrayUtils;

public class TaskDto {

    @NotNull
    private String name;

    @NotNull
    private String dateControl;

    @NotNull
    private String deadLine;

    @NotNull
    private String priority;

    @NotNull
    private String description;

    private String[] keyWords;

    @NotNull
    private Long[] performerId;

    @NotNull
    private Integer managerId;

    private Long[] docsId;

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

    public String getDeadLine() {
        return this.deadLine;
    }

    public void setDeadLine(final String deadLine) {
        this.deadLine = deadLine;
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

    public String[] getKeyWords() {
        return this.keyWords;
    }

    public void setKeyWords(final String[] keyWords) {
        this.keyWords = keyWords;
    }

    public void setKeyWords(final Set<String> keyWords) {
        this.keyWords = new String[keyWords.size()];
        int i = 0;
        for (String key : keyWords) {
            this.keyWords[i++] = key;
        }
    }

    public long[] getPerformerId() {
        return ArrayUtils.toPrimitive(this.performerId);
    }

    public void setPerformerId(final Long[] performerId) {
        this.performerId = performerId;
    }

    public void setPerformerId(final Set<Long> performerIds) {
        this.performerId = new Long[performerIds.size()];
        int i = 0;
        for (Long val : performerIds) {
            this.performerId[i++] = val;
        }
    }

    public Integer getManagerId() {
        return this.managerId;
    }

    public void setManagerId(final Integer managerId) {
        this.managerId = managerId;
    }

    public long[] getDocsId() {
        return ArrayUtils.toPrimitive(this.docsId);
    }

    public void setDocsId(final Long[] docsId) {
        this.docsId = docsId;
    }

    public void setDocsId(final Set<Long> docsId) {
        this.docsId = new Long[docsId.size()];
        int i = 0;
        for (Long val : docsId) {
            this.docsId[i++] = val;
        }
    }
}
