package app.customtenant.service.impl;

import app.configuration.spring.constants.Constants;
import app.customtenant.dao.interfaces.IBriefDocumentDao;
import app.customtenant.dao.persistance.IGenericDao;
import app.customtenant.models.basic.BriefDocument;
import app.customtenant.service.abstraction.AbstractService;
import app.customtenant.service.interfaces.IBriefDocumentService;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class BriefDocumentService
        extends AbstractService<BriefDocument>
        implements IBriefDocumentService {

    private static final String ROWS_ON_PAGE_ARHIVE_DOC = "rows_on_page_arhive_doc";

    @Autowired
    private Constants constants;

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
    public List<BriefDocument> findBy(int page, Date date, Long start, Long end, String word) {
        int pageSize = constants
                .get(ROWS_ON_PAGE_ARHIVE_DOC)
                .getIntValue();
        if (date != null) {
            return dao.findBy(page, pageSize, date);
        } else if (word != null) {
            return dao.findByWord(page, pageSize, word);
        } else if (start != null && end != null) {
            return dao.findBy(page, pageSize, start, end);
        } else {
            return new LinkedList<>();
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<BriefDocument> findByAndDepartment(int page, Date date,
                                                   long depoId) {
        int pageSize = constants
                .get(ROWS_ON_PAGE_ARHIVE_DOC)
                .getIntValue();
        return dao.findByAndDepartment(page, pageSize, date, depoId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BriefDocument> findByAndPerformerInTaskId(int page, Date date, long perfId) {
        int pageSize = constants
                .get(ROWS_ON_PAGE_ARHIVE_DOC)
                .getIntValue();
        return dao.findByAndPerformerInTaskId(page, pageSize, date, perfId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<BriefDocument> findByWord(int page, String word) {
        int pageSize = constants
                .get(ROWS_ON_PAGE_ARHIVE_DOC)
                .getIntValue();
        return dao.findByWord(page, pageSize, word);
    }
}

