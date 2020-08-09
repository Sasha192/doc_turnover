package app.service.interfaces;

import app.dao.persistance.IOperations;
import app.models.basic.BriefDocument;
import java.util.List;

public interface IBriefDocumentService extends IOperations<BriefDocument> {

    //BriefDocument retrieveByName(String name);

    List<BriefDocument> findBy(int pageId, String search,
                               Integer year, Integer month,
                               Integer date);

    List<BriefDocument> findArchived();

    List<BriefDocument> findActive();
}