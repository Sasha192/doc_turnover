package app.service.impl;

import app.dao.IBriefDocumentDao;
import app.dao.persistance.IGenericDao;
import app.models.BriefDocument;
import app.service.IBriefDocumentService;
import app.service.abstraction.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.List;
import java.util.Map;

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
}

