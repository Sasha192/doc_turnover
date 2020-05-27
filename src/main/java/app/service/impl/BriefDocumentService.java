package app.service.impl;

import app.dao.IBriefDocumentDao;
import app.dao.persistance.IGenericDao;
import app.models.BriefDocument;
import app.service.IBriefDocumentService;
import app.service.abstraction.AbstractService;
import java.util.List;
import java.util.Map;
import javax.persistence.criteria.Predicate;
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
    public List<BriefDocument> findBy(Map<String, String> filters) {
        // retrieveFilter ->
        Predicate predicate = null;
        return this.findBy(predicate);
    }

    @Override
    public List<BriefDocument> findArchived() {
        return dao.findArchived();
    }

    @Override
    public List<BriefDocument> findActive() {
        return dao.findActive();
    }
}

