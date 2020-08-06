package app.controllers.dto.mappers;

import app.configuration.spring.constants.Constants;
import app.controllers.dto.TaskDto;
import app.models.basic.BriefDocument;
import app.models.basic.Performer;
import app.models.basic.Task;
import app.models.basic.TaskStatus;
import app.service.interfaces.IBriefDocumentService;
import app.service.interfaces.IPerformerService;
import app.service.interfaces.IStatusService;
import com.google.common.collect.Sets;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("task_mapper")
public class TaskMapper implements IEntityDtoMapper<Task, TaskDto> {

    private static SimpleDateFormat FORMATTER;

    @Autowired
    private IPerformerService performerService;

    @Autowired
    private IBriefDocumentService documentService;

    @Autowired
    private IStatusService statusService;

    @PostConstruct
    private void init() {
        FORMATTER = Constants.DATE_FORMAT;
    }

    @Override
    public TaskDto getDto(Task entity) {
        TaskDto dto = new TaskDto();
        dto.setKeyWords(entity.getKeys());
        dto.setDateControl(FORMATTER.format(entity.getControlDate()));
        dto.setPriority(entity.getPriority());
        dto.setDeadline(FORMATTER.format(entity.getDeadlineDate()));
        dto.setDescription(FORMATTER.format(entity.getDescription()));
        dto.setName(entity.getToDo());
        dto.setDocsId(entity.getDocumentsIds());
        dto.setPerformerId(entity.getPerformerIds());
        dto.setStatus(entity.getStatus().getName());
        return dto;
    }

    @Override
    public Task getEntity(TaskDto dto) {
        Task task = new Task();
        try {
            setTaskFields(dto, task);
        } catch (ParseException e) {
            return null;
        }
        List<Performer> performers = performerService.findSeveralById(dto.getPerformerId());
        Long[] docIds = dto.getDocsId();
        if (docIds != null && docIds.length > 0) {
            List<BriefDocument> documents = documentService.findSeveralById(docIds);
            task.setDocument(Sets.newHashSet(documents));
        }
        TaskStatus taskStatus = statusService.findByTitle(dto.getStatus());
        if (taskStatus != null) {
            task.setStatus(taskStatus);
        } else {
            throw new IllegalArgumentException("Not valid TaskStatus. Actual : " + dto.getStatus());
        }
        task.setDeadline(false);
        task.setPerformer(Sets.newHashSet(performers));
        return task;
    }

    private void setTaskFields(TaskDto dto, Task task) throws ParseException {
        Date now = Date.valueOf(LocalDate.now());
        task.setToDo(dto.getName());
        task.setControlDate(FORMATTER.parse(dto.getDateControl()).getTime());
        task.setDeadlineDate(FORMATTER.parse(dto.getDeadline()).getTime());
        task.setCreationDate(now);
        task.setPriority(Constants.EMPTY_STRING);
        task.setDescription(dto.getDescription());
        Set<String> keys = new HashSet<>();
        task.setKeys(keys);
        if (dto.getKeyWords() != null) {
            for (Object key : dto.getKeyWords()) {
                keys.add(key.toString());
            }
        }
    }
}
