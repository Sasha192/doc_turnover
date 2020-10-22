package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.BriefDocument;
import java.util.Date;
import java.util.List;

public interface IBriefDocumentService extends IOperations<BriefDocument> {

    List<BriefDocument> findBy(int page, Date date, Long start, Long end, String word);

    List<BriefDocument> findByAndDepartment(int pageId,
                                            Date date,
                                            long depoId);

    List<BriefDocument> findByAndPerformerInTaskId(int pageId, Date date, long perfId);

    List<BriefDocument> findByWord(int page, String word);
}
