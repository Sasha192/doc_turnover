package app;

import app.dao.persistance.IOperations;
import app.models.BriefDocument;
import app.models.Department;
import app.models.Performer;
import app.models.Task;
import app.service.*;
import app.spring.TestSpringDataConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringDataConfiguration.class,
        loader = AnnotationConfigContextLoader.class)
public class JUnitSpringExample {

    @Autowired
    private IDepartmentService departmentService;

    @Autowired
    private IBriefDocumentService documentService;

    @Autowired
    private IBriefJsonDocumentService jsonDocumentService;

    @Autowired
    private ICorePropertyService corePropertyService;

    @Autowired
    private IPerformerService performerService;

    @Autowired
    private IStatusService statusService;

    @Autowired
    private ITaskService taskService;

    private IOperations<?>[] iOperations;

    @BeforeEach
    private void init() {
        this.iOperations = new IOperations<?>[]{this.departmentService, this.documentService,
                this.jsonDocumentService, this.corePropertyService, this.performerService, this.statusService, this.taskService};
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
    public void testServiceMethods() {
        this.selectAll();
    }

    @Test
    public void PerforDepoTest() {
        System.out.println(this.performerService.findAll());
        System.out.println(this.performerService.findByDepartmentId(1l));
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

    private void selectAll() {
        Arrays.stream(this.iOperations).forEach(IOperations::findAll);
    }

    private void selectSpecificIds() {
        Arrays.stream(this.iOperations).forEach(var -> {
            var.findOne(1);
        });
    }

    private void create() {
    }

    private void update() {
    }
}
