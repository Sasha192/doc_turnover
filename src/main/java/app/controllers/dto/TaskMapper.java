package app.controllers.dto;

import app.configuration.spring.constants.Constants;
import app.models.BriefDocument;
import app.models.Performer;
import app.models.Task;
import app.service.IBriefDocumentService;
import app.service.IPerformerService;
import app.service.IStatusService;
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
        dto.setDeadLine(FORMATTER.format(entity.getDeadlineDate()));
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
            task = setTaskFields(dto, task);
        } catch (ParseException e) {
            return null;
        }
        List<Performer> performers = performerService.findSeveralById(dto.getPerformerId());
        if (dto.getPerformerId() != null) {
            List<BriefDocument> documents = documentService.findSeveralById(dto.getDocsId());
            task.setDocument(Sets.newHashSet(documents));
        }
        task.setDeadline(false);
        task.setPerformer(Sets.newHashSet(performers));
        return task;
    }

    private Task setTaskFields(TaskDto dto, Task task) throws ParseException {
        Date now = Date.valueOf(LocalDate.now());
        task.setToDo(dto.getName());
        task.setControlDate(FORMATTER.parse(dto.getDateControl()).getTime());
        task.setDeadlineDate(FORMATTER.parse(dto.getDeadLine()).getTime());
        task.setCreationDate(now);
        task.setPriority(dto.getPriority());
        //task.setStatus();
        task.setDescription(dto.getDescription());
        Set<String> keys = new HashSet<>();
        task.setKeys(keys);
        if (dto.getKeyWords() != null) {
            for (String key : dto.getKeyWords()) {
                keys.add(key);
            }
        }
        return task;
    }
}
