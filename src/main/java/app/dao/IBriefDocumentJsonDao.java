package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.mysqlviews.BriefJsonDocument;
import java.util.List;

public interface IBriefDocumentJsonDao extends IGenericDao<BriefJsonDocument> {

    List<BriefJsonDocument> findArchived();

    List<BriefJsonDocument> findActive();

    List<BriefJsonDocument> findBy(int pageId, String search,
                                   Integer year, Integer month,
                                   Integer date);

}
