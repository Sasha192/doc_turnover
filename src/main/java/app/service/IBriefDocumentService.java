package app.service;

import app.dao.persistance.IOperations;
import app.models.BriefDocument;
import java.util.List;
import java.util.Map;

public interface IBriefDocumentService extends IOperations<BriefDocument> {

    //BriefDocument retrieveByName(String name);

    List<BriefDocument> findBy(Map<String, String> filters);

    List<BriefDocument> findArchived();

    List<BriefDocument> findActive();

    List<BriefDocument> findSeveralById(long... ids);
}
