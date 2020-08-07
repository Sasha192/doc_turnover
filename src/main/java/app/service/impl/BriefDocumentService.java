package app.service.impl;

import app.dao.interfaces.IBriefDocumentDao;
import app.dao.persistance.IGenericDao;
import app.models.basic.BriefDocument;
import app.service.abstraction.AbstractService;
import app.service.interfaces.IBriefDocumentService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BriefDocumentService
        extends AbstractService<BriefDocument>
        implements IBriefDocumentService {

    @Autowired
    private IBriefDocumentDao dao;

    public BriefDocumentService() {

    }

    @Override
    protected IGenericDao<BriefDocument> getDao() {
        return dao;
    }

    public void setDao(final IBriefDocumentDao dao) {
        this.dao = dao;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BriefDocument> findArchived() {
        return dao.findArchived();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BriefDocument> findActive() {
        return dao.findActive();
    }

    @Override
    @Transactional(readOnly = true)
    public List<BriefDocument> findBy(int pageId,
                                      String search,
                                      Integer year,
                                      Integer month,
                                      Integer date) {
        return dao.findBy(pageId, search, year, month, date);
    }
}

