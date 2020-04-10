package app.dao.impl;

import app.dao.persistance.AbstractHibernateDao;
import app.models.BriefDocument;
import org.springframework.stereotype.Repository;

@Repository
public class BriefDocumentDao extends AbstractHibernateDao<BriefDocument> {

    public BriefDocumentDao() {
        setClazz(BriefDocument.class);
    }

}
