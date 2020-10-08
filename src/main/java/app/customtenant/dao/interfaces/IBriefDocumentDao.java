package app.customtenant.dao.interfaces;

import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.BriefDocument;
import java.util.List;

public interface IBriefDocumentDao extends IGenericDao<BriefDocument> {

    List<BriefDocument> findArchived();

    List<BriefDocument> findActive();

    List<BriefDocument> findBy(int pageId, String search,
                               Integer year, Integer month,
                               Integer date);

    //BriefDocument retrieveByName(String name) throws NoSuchObjectException;
}



