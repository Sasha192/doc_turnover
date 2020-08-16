package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.mysqlviews.BriefJsonDocument;
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
