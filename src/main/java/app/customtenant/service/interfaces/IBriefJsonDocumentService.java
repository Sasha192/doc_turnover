package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.mysqlviews.BriefJsonDocument;
import java.util.List;

public interface IBriefJsonDocumentService extends IOperations<BriefJsonDocument> {

    List<BriefJsonDocument> findArchived();

    List<BriefJsonDocument> findActive();

    List<BriefJsonDocument> findBy(int pageId, String search,
                                   Integer year, Integer month,
                                   Integer date);

    List<BriefJsonDocument> findByAndDepartment(int pageId,
                             String search,
                             Integer yearInt,
                             Integer monthInt,
                             Integer dayInt,
                             Long departmentId);

    List<BriefJsonDocument> findByAndPerformerInTaskId(int pageId,
                                    String search,
                                    Integer yearInt,
                                    Integer monthInt,
                                    Integer dayInt,
                                    Long performerId);
}
