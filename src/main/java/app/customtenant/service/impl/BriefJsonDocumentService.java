package app.customtenant.service.impl;

import app.customtenant.dao.interfaces.IBriefDocumentJsonDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.mysqlviews.BriefJsonDocument;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IBriefJsonDocumentService;
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

    @Override
    public List<BriefJsonDocument> findByAndDepartment(int pageId,
                                                       String search,
                                                       Integer yearInt,
                                                       Integer monthInt,
                                                       Integer dayInt,
                                                       Long departmentId) {
        return dao.findByAndDepartment(pageId,
                search,
                yearInt,
                monthInt,
                dayInt,
                departmentId);
    }

    @Override
    public List<BriefJsonDocument> findByAndPerformerInTaskId(int pageId,
                                                              String search,
                                                              Integer yearInt,
                                                              Integer monthInt,
                                                              Integer dayInt,
                                                              Long performerId) {
        return dao.findByAndPerformerInTaskId(pageId,
                search,
                yearInt,
                monthInt,
                dayInt,
                performerId);
    }
}
