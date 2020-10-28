package app.customtenant.models.basic.taskmodels;

import app.customtenant.models.basic.TaskStatus;

public class TaskStatusValidator {

    public static boolean check(
            TaskStatus oldState, TaskStatus newState) {
        TaskStatus oldS = getByName(oldState);
        TaskStatus newS = getByName(newState);
        if (oldS == TaskStatus.COMPLETED) {
            return false;
        }
        if (newS == TaskStatus.NEW) {
            return false;
        }
        return true;
    }

    private static TaskStatus getByName(TaskStatus status) {
        return TaskStatus.getByName(status.getName());
    }
}
