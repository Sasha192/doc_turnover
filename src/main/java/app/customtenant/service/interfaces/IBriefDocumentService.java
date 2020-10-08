package app.customtenant.service.interfaces;

import app.customtenant.dao.persistance.IOperations;
import app.customtenant.models.basic.BriefDocument;
import java.util.List;

public interface IBriefDocumentService extends IOperations<BriefDocument> {

    //BriefDocument retrieveByName(String name);

    List<BriefDocument> findBy(int pageId, String search,
                               Integer year, Integer month,
                               Integer date);

    List<BriefDocument> findArchived();

    List<BriefDocument> findActive();
}
