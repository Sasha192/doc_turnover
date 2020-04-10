package app.service;

import app.dao.IBriefDocumentDao;
import app.models.BriefDocument;
import app.service.abstraction.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
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

    // API

    @Override
    protected PagingAndSortingRepository<BriefDocument, Long> getDao() {
        return dao;
        JpaRepository
    }

    // custom methods

    @Override
    public BriefDocument retrieveByName(final String name) {
        return dao.retrieveByName(name);
    }
}

