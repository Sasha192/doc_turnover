package app.dao.impl;

import app.dao.IBriefDocumentDao;
import app.dao.persistance.GenericJpaDao;
import app.models.BriefDocument;

import java.rmi.NoSuchObjectException;
import java.util.List;
import javax.persistence.Query;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class BriefDocumentDao extends GenericJpaDao<BriefDocument>
        implements IBriefDocumentDao {

    private static final Logger LOGGER = Logger.getLogger(BriefDocument.class);

    public BriefDocumentDao() {
        setClazz(BriefDocument.class);
    }

    @Override
    public BriefDocument retrieveByName(String name) throws NoSuchObjectException {
        String className = this.getClazz().getName();
        Query query = this.getEntityManager()
                .createQuery("FROM BriefDocument bd WHERE bd.name=:name");
        query.setParameter("name", name);
        List<BriefDocument> list = query.getResultList();
        if (list.isEmpty()) {
            throw new NoSuchObjectException("No such document exception");
        }
        return list.get(0);
    }
}
