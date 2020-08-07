package app.service.impl;

import app.dao.interfaces.IBriefDocumentJsonDao;
import app.dao.persistance.IGenericDao;
import app.models.mysqlviews.BriefJsonDocument;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IBriefJsonDocumentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class BriefJsonDocumentService
        extends AbstractService<BriefJsonDocument>
        implements IBriefJsonDocumentService {

    @Autowired
    private IBriefDocumentJsonDao dao;

    @Override
    protected IGenericDao<BriefJsonDocument> getDao() {
        return this.dao;
    }

    public void setDao(IBriefDocumentJsonDao dao) {
        this.dao = dao;
    }

    @Override
    public List<BriefJsonDocument> findArchived() {
        return this.dao.findArchived();
    }

    @Override
    public List<BriefJsonDocument> findActive() {
        return this.dao.findActive();
    }

    @Override
    public List<BriefJsonDocument> findBy(final int pageId, final String search,
                                          final Integer year, final Integer month,
                                          final Integer date) {
        return this.dao.findBy(pageId, search, year, month, date);
    }
}
