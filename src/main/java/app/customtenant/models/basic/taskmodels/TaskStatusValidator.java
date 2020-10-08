package app.customtenant.models.basic.taskmodels;

import app.customtenant.models.basic.TaskStatus;

public class TaskStatusValidator {

    public static boolean check(
            TaskStatus oldState, TaskStatus newState) {
        TaskStatus.DefaultStatus oldS = getByName(oldState);
        TaskStatus.DefaultStatus newS = getByName(newState);
        if (oldS == TaskStatus.DefaultStatus.COMPLETED) {
            return false;
        }
        if (newS == TaskStatus.DefaultStatus.NEW) {
            return false;
        }
        return true;
    }

    private static TaskStatus.DefaultStatus getByName(TaskStatus status) {
        return TaskStatus.DefaultStatus
                .getByName(status.getName());
    }
}
