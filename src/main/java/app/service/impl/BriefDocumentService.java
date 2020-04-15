package app.service.impl;

import app.dao.IBriefDocumentDao;
import app.dao.persistance.IGenericDao;
import app.models.BriefDocument;
import app.service.IBriefDocumentService;
import app.service.abstraction.AbstractService;

import java.rmi.NoSuchObjectException;
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

    @Override
    public BriefDocument retrieveByName(final String name) {
        try {
            return dao.retrieveByName(name);
        } catch (NoSuchObjectException e) {
            return null;
        }
    }

    public void setDao(final IBriefDocumentDao dao) {
        this.dao = dao;
    }
}

