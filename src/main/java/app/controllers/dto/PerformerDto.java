package app.controllers.dto;

import javax.validation.constraints.NotNull;

public class PerformerDto {

    private @NotNull String name;

    private @NotNull Long departmentId;

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public Long getDepartmentId() {
        return this.departmentId;
    }

    public void setDepartmentId(final Long departmentId) {
        this.departmentId = departmentId;
    }
}
