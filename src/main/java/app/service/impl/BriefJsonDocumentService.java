package app.service.impl;

import app.dao.IBriefDocumentJsonDao;
import app.dao.persistance.IGenericDao;
import app.models.BriefJsonDocument;
import app.service.IBriefJsonDocumentService;
import app.service.abstraction.AbstractService;
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
        return dao;
    }

    public void setDao(final IBriefDocumentJsonDao dao) {
        this.dao = dao;
    }
}
