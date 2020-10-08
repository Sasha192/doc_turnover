package app.tenantdefault.aspects;

import app.tenantdefault.models.DocumentEntity;
import app.tenantdefault.service.IUuidGenerator;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class DocumentEntityAspect {

    @Autowired
    @Qualifier("doc_uuid_mongodb_gen")
    private IUuidGenerator uuidGenerator;

    @Pointcut("execution"
            + "(* app.tenantdefault.service.IDocumentStorage.save("
            + "app.tenantdefault.models.DocumentEntity))")
    public void datastoreMethodSave() {
    }

    @Before("datastoreMethodSave()")
    public void insertUuid(JoinPoint jp) {
        Object[] objects = jp.getArgs();
        Object entityArg = objects[0];
        if (entityArg instanceof DocumentEntity) {
            DocumentEntity en = ((DocumentEntity) entityArg);
            String id = en.getId();
            if (id == null || id.isEmpty()) {
                id = uuidGenerator.generate().toString().replace("-", "");
            }
            en.setId(id);
        }
    }

}
