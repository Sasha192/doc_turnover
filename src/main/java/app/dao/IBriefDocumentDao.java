package app.dao;

import app.dao.persistance.IOperations;
import app.models.BriefDocument;
import org.springframework.stereotype.Repository;

@Repository
public interface IBriefDocumentDao extends IOperations<BriefDocument> {


}
