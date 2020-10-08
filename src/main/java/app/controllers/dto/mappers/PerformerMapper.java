package app.controllers.dto.mappers;

import app.controllers.dto.PerformerDto;
import app.customtenant.models.basic.Department;
import app.customtenant.models.basic.Performer;
import app.customtenant.service.interfaces.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("performer_mapper")
public class PerformerMapper implements IEntityDtoMapper<Performer, PerformerDto> {

    @Autowired
    private IDepartmentService departmentService;

    @Override
    public PerformerDto getDto(Performer entity) {
        PerformerDto dto = new PerformerDto();
        dto.setName(entity.getName());
        dto.setDepartmentId(entity.getDepartment().getId());
        return dto;
    }

    @Override
    public Performer getEntity(PerformerDto dto) {
        Performer newPerformer = new Performer();
        newPerformer.setName(dto.getName());
        Department department = departmentService.findOne(dto.getDepartmentId());
        newPerformer.setDepartmentId(department.getId());
        return newPerformer;
    }
}
