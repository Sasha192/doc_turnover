package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.BriefDocument;
import java.util.List;

public interface IBriefDocumentDao extends IGenericDao<BriefDocument> {

    List<BriefDocument> findArchived();

    List<BriefDocument> findActive();

    //BriefDocument retrieveByName(String name) throws NoSuchObjectException;
}



