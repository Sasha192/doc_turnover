package app.dao.impl;

import app.dao.IBriefDocumentDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.BriefDocument;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class BriefDocumentDao extends GenericJpaRepository<BriefDocument>
        implements IBriefDocumentDao {

    private static final Logger LOGGER = Logger.getLogger(BriefDocument.class);

    public BriefDocumentDao() {
        setClazz(BriefDocument.class);
    }


}
