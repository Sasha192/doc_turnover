package app.service;

import app.dao.persistance.IOperations;
import app.models.mysqlviews.BriefJsonDocument;
import java.util.List;

public interface IBriefJsonDocumentService extends IOperations<BriefJsonDocument> {

    List<BriefJsonDocument> findArchived();

    List<BriefJsonDocument> findActive();

    List<BriefJsonDocument> findBy(int pageId, String search,
                                   Integer year, Integer month,
                                   Integer date);

}
