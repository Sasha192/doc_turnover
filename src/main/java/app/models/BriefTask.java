package app.models;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BriefTask {

    private Long id;

    private String toDo;

    private Date creationDate;

    private Date modificationDate;

    private String performer;

    private String department;

    private boolean deadline;

    private String priority;

    private Long docId;

    private Date deadlineDate;

    private Date controlDate;

    private Task.TaskStatus status;

    private BriefTask() {
        ;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getToDo() {
        return this.toDo;
    }

    public void setToDo(final String toDo) {
        this.toDo = toDo;
    }

    public Date getCreationDate() {
        return this.creationDate;
    }

    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getModificationDate() {
        return this.modificationDate;
    }

    public void setModificationDate(final Date modificationDate) {
        this.modificationDate = modificationDate;
    }

    public String getPerformer() {
        return this.performer;
    }

    public void setPerformer(final String performer) {
        this.performer = performer;
    }

    public String getDepartment() {
        return this.department;
    }

    public void setDepartment(final String department) {
        this.department = department;
    }

    public boolean isDeadline() {
        return this.deadline;
    }

    public void setDeadline(final boolean deadline) {
        this.deadline = deadline;
    }

    public String getPriority() {
        return this.priority;
    }

    public void setPriority(final String priority) {
        this.priority = priority;
    }

    public Long getDocId() {
        return this.docId;
    }

    public void setDocId(final Long docId) {
        this.docId = docId;
    }

    public Date getDeadlineDate() {
        return this.deadlineDate;
    }

    public void setDeadlineDate(final Date deadlineDate) {
        this.deadlineDate = deadlineDate;
    }

    public Date getControlDate() {
        return this.controlDate;
    }

    public void setControlDate(final Date controlDate) {
        this.controlDate = controlDate;
    }

    public Task.TaskStatus getStatus() {
        return this.status;
    }

    public void setStatus(final Task.TaskStatus status) {
        this.status = status;
    }

    public static BriefTask toBrief(Task task) {
        BriefTask btask = new BriefTask();
        btask.setId(task.getId());
        btask.setCreationDate(task.getCreationDate());
        btask.setDeadline(task.getDeadline());
        Performer perf = task.getPerformer();
        Department dep = perf.getDepartment();
        String performer = perf.getLastName() + "  " + perf.getFirstName();
        String department = dep.getDepartmentName();
        btask.setDepartment(department);
        btask.setPerformer(performer);
        btask.setModificationDate(task.getModificationDate());
        btask.setDocId(task.getDocument().getId());
        btask.setPriority(task.getPriority());
        btask.setToDo(task.getToDo());
        btask.setControlDate(task.getControlDate());
        btask.setDeadlineDate(task.getDeadlineDate());
        btask.setStatus(task.getStatus());
        return btask;
    }

    public static List<BriefTask> toBrief(List<Task> tasks) {
        return tasks
                .stream()
                .parallel()
                .map(task -> toBrief(task))
                .collect(Collectors.toList());
    }
}
