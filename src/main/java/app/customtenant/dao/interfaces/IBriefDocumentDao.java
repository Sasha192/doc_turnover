package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.BriefDocument;
import java.util.Date;
import java.util.List;

public interface IBriefDocumentDao extends IGenericDao<BriefDocument> {

    List<BriefDocument> findBy(int page, int pageSize, Date date);

    List<BriefDocument> findBy(int page, int pageSize,
                               long start, long end);

    List<BriefDocument> findByAndDepartment(int page, int pageSize,
                                            Date date, long depoId);

    List<BriefDocument> findByAndPerformerInTaskId(int page, int pageSize, Date date, long perfId);
}



