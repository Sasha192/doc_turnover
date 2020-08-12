package app;

import app.configuration.spring.constants.Constants;
import app.dao.persistance.IOperations;
import app.models.basic.BriefDocument;
import app.models.basic.Department;
import app.models.basic.Performer;
import app.models.basic.Report;
import app.models.basic.ReportComment;
import app.models.basic.Task;
import app.models.basic.TaskComment;
import app.models.events.Event;
import app.models.events.PerformerEventAgent;
import app.models.events.impl.DocPublishingEvent;
import app.models.mysqlviews.BriefPerformer;
import app.models.mysqlviews.BriefTask;
import app.models.serialization.ExcludeStrategies;
import app.security.service.IUserService;
import app.service.interfaces.IBriefDocumentService;
import app.service.interfaces.IBriefJsonDocumentService;
import app.service.interfaces.IBriefTaskService;
import app.service.interfaces.ICorePropertyService;
import app.service.interfaces.IDepartmentService;
import app.service.interfaces.IEventService;
import app.service.interfaces.IPerformerService;
import app.service.interfaces.IReportCommentService;
import app.service.interfaces.IStatusService;
import app.service.interfaces.ITaskCommentService;
import app.service.interfaces.ITaskService;
import app.spring.TestSpringDataConfiguration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringDataConfiguration.class,
        loader = AnnotationConfigContextLoader.class)
public class JUnitSpringExample {

    private static final GsonBuilder BUILDER;

    static {
        BUILDER = new GsonBuilder().setPrettyPrinting()
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_COMMENT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_REPORT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_PERFORMER)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_JSON_EVENT)
                .addSerializationExclusionStrategy(ExcludeStrategies.EXCLUDE_FOR_BRIEF_TASK)
                .setDateFormat(Constants.DATE_FORMAT.toPattern());
    }

    private IDepartmentService departmentService;

    private IBriefDocumentService documentService;

    private IBriefJsonDocumentService jsonDocumentService;

    private ICorePropertyService corePropertyService;

    private IPerformerService performerService;

    private IStatusService statusService;

    private ITaskService taskService;

    private ITaskCommentService taskCommentService;

    private IReportCommentService reportCommentService;

    private IBriefTaskService briefTaskService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IEventService eventService;

    private IOperations<?>[] operations;

    @Autowired
    public JUnitSpringExample(IDepartmentService departmentService,
                              IBriefDocumentService documentService,
                              IBriefJsonDocumentService jsonDocumentService,
                              ICorePropertyService corePropertyService,
                              IPerformerService performerService,
                              IStatusService statusService, ITaskService taskService,
                              ITaskCommentService taskCommentService,
                              IReportCommentService reportCommentService,
                              IBriefTaskService briefTaskService) {
        this.departmentService = departmentService;
        this.documentService = documentService;
        this.jsonDocumentService = jsonDocumentService;
        this.corePropertyService = corePropertyService;
        this.performerService = performerService;
        this.statusService = statusService;
        this.taskService = taskService;
        this.taskCommentService = taskCommentService;
        this.reportCommentService = reportCommentService;
        this.briefTaskService = briefTaskService;
    }

    @BeforeEach
    private void init() {
        this.operations = new IOperations<?>[]{this.departmentService, this.documentService,
                this.jsonDocumentService,
                this.corePropertyService,
                this.performerService,
                this.statusService, this.taskService, briefTaskService,
                reportCommentService, taskCommentService};
    }

    @Test
    public void testDepartmentService() {
        final List<Department> departs = this.departmentService.findAll();
        for (final Department department : departs) {
            Department parent = department.getParentDepartment();
            if (parent != null) {
                parent.getPerformers();
            }
            final Set<Performer> performers = department.getPerformers();
            for (final Performer performer : performers) {
                final List<Task> tasks = performer.getTasks();
                for (final Task task : tasks) {
                    task.getPerformerIds();
                    task.getDocumentsIds();
                    final Set<BriefDocument> briefDocuments = task.getDocument();
                    for (final BriefDocument doc : briefDocuments) {
                        System.out.println(doc.getPerformer());
                        System.out.println(performer);
                        Assertions.assertEquals(performer, doc.getPerformer());
                    }
                }
            }
        }
    }

    @Test
    public void testCommentServices() {
        taskCommentService.findAll();
        reportCommentService.findAll();
        TaskComment taskComment = taskCommentService.findOne(1L);
        ReportComment reportComment = reportCommentService.findOne(3L);
        Task task = taskComment.getTask();
        reportComment.getReport();
        Report report = reportComment.getReport();
        List<?> set = report.getDocuments();
        System.out.println();
    }

    @Test
    public void testServiceMethods() {
        this.selectAll();
    }

    @Test
    public void testBriefTask() {
        List<BriefTask> tasks = briefTaskService.findAll();
        Gson gson = BUILDER.create();
        gson.toJson(tasks);
    }

    @Test
    public void performerDepoTest() {
        System.out.println(this.performerService.findAll());
        System.out.println(this.performerService.findByDepartmentId(1L));
    }

    @Test
    public void selectJsonBriefDoc() {
        System.out.println(this.jsonDocumentService.findAll());
        System.out.println(this.jsonDocumentService.findOne(1));
    }

    @Test
    public void selectJsonFilterDoc() {
        System.out.println(this.jsonDocumentService.findBy(1, null, null, null, null));
    }

    @Test
    public void testBriefTaskJson() {
        BriefTask briefTask = briefTaskService.findAll().get(0);
        briefTask.getDocList();
        briefTask.getManagerImgPath();
        briefTask.getReports().getDocuments();
        briefTask.getReports().getComments();
        briefTask.getKeys();
        briefTask.getBriefPerformers();
    }

    @Test
    public void testForMultipleRetrievePerformersId() {
        TaskComment taskComment = taskCommentService.findOne(1L);
        Long task = taskComment.getTask().getId();
        System.out.println(task);
    }

    @Test
    public void testBriefPerformers() {
        BriefTask task = briefTaskService.findAll().get(0);
        BriefPerformer briefPerformer = task.getBriefPerformers().get(0);
        briefPerformer.getEmail();
        System.out.println(briefPerformer.getEmail());
    }

    @Test
    public void testGsonEvents() {
        Performer performer = performerService.findAll().get(0);
        List<PerformerEventAgent> events = eventService
                .retrieveLastEventsForPerformerId(performer.getId());
        System.out.println();
    }

    @Test
    public void testEventsCrudOp() throws InterruptedException {
        List<Performer> performers = performerService.findAll();
        List<BriefDocument> documents = documentService.findAll();
        DocPublishingEvent event = new DocPublishingEvent();
        event.setDocumentId(documents.get(0).getId());
        event.setAuthorId(performers.get(0).getId());
        event.setDescription("");
        event.setDate(Date.valueOf(LocalDate.now()));
        event.setEventTypeEnum(Event.EventType.DOC_PUB);
        Set<Long> set = performers.stream().map(performer -> {
            return performer.getId();
        }).collect(Collectors.toSet());
        event.setPerformersId(set);
        eventService.create(event);
        Thread.sleep(10_000);
        eventService.retrieveLastEvents();
        if (performers != null && (!performers.isEmpty())) {
            Performer performer = performers.get(0);
            List<PerformerEventAgent> events = eventService
                    .retrieveLastEventsForPerformerId(performer.getId());
            events.stream().forEach(eventS -> {
                eventS.getEvent();
                eventS.getPerformer().getDepartment();
                eventS.getAuthor().getEmail();
            });
        }
    }

    @Test
    public void testBatchPersistence() {
        documentService.create(new LinkedList<>());
    }

    private void selectAll() {
        Arrays.stream(this.operations).forEach(IOperations::findAll);
    }

    private void selectSpecificIds() {
        Arrays.stream(this.operations).forEach(var -> {
            var.findOne(1);
        });
    }

    private void create() {
    }

    private void update() {
    }
}
