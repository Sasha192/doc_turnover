package app;

import app.dao.persistance.IOperations;
import app.models.BriefDocument;
import app.models.Department;
import app.models.Performer;
import app.models.Task;
import app.service.IBriefDocumentService;
import app.service.IBriefJsonDocumentService;
import app.service.ICorePropertyService;
import app.service.IDepartmentService;
import app.service.IPerformerService;
import app.service.IStatusService;
import app.service.ITaskService;
import app.spring.TestSpringDataConfiguration;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.annotation.PostConstruct;
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
        iOperations = new IOperations<?>[]{departmentService, documentService,
                jsonDocumentService, corePropertyService, performerService, statusService, taskService};
    }

    @Test
    public void testDepartmentService() {
        List<Department> departs = departmentService.findAll();
        for (Department department : departs) {
            Set<Performer> performers = department.getPerformers();
            for (Performer performer : performers) {
                List<Task> tasks = performer.getTasks();
                for (Task task : tasks) {
                    task.getPerformerIds();
                    task.getDocumentsIds();
                    Set<BriefDocument> briefDocuments = task.getDocument();
                    for (BriefDocument doc : briefDocuments) {
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
        selectAll();
    }

    @Test
    public void PerforDepoTest() {
        System.out.println(performerService.findAll());
        System.out.println(performerService.findByDepartmentId(1l));
    }

    @Test
    public void selectJsonBriefDoc() {
        System.out.println(jsonDocumentService.findAll());
        System.out.println(jsonDocumentService.findOne(1));
    }

    private void selectAll() {
        Arrays.stream(iOperations).forEach(IOperations::findAll);
    }

    private void selectSpecificIds() {
        Arrays.stream(iOperations).forEach(var -> {
            var.findOne(1);
        });
    }

    private void create() {
        ;
    }

    private void update() {
        ;
    }
}
