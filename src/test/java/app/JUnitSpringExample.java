package app;

import app.dao.persistance.IOperations;
import app.models.basic.*;
import app.models.mysqlviews.BriefTask;
import app.service.impl.BriefTaskService;
import app.service.interfaces.*;
import app.spring.TestSpringDataConfiguration;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
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

    private IOperations<?>[] operations;

    @BeforeEach
    private void init() {
        this.operations = new IOperations<?>[]{this.departmentService, this.documentService,
                this.jsonDocumentService,
                this.corePropertyService,
                this.performerService,
                this.statusService, this.taskService};
    }

    @Test
    public void testDepartmentService() {
        final List<Department> departs = this.departmentService.findAll();
        for (final Department department : departs) {
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
        BriefTask briefTask = briefTaskService.findOne(3L);
        briefTask.getDocList();
        briefTask.getManagerImgPath();
        briefTask.getReports().getDocuments();
        briefTask.getReports().getComments();
        briefTask.getKeys();
        briefTask.getPerformer();
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
