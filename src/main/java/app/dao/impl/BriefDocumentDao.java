package app.dao.impl;

import app.dao.IBriefDocumentDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.BriefDocument;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class BriefDocumentDao extends GenericJpaRepository<BriefDocument>
        implements IBriefDocumentDao {

    private static final String FROM = " FROM " + BriefDocument.class.getName() + " ";

    private static final String WHERE_ARCHIVED = FROM.concat(" WHERE is_archive=true");

    private static final String WHERE_NO_ARCHIVED = FROM.concat(" WHERE is_archive=false");

    private static final Logger LOGGER = Logger.getLogger(BriefDocument.class);

    public BriefDocumentDao() {
        setClazz(BriefDocument.class);
    }

    @Override
    public List<BriefDocument> findArchived() {
        return getEntityManager().createQuery(WHERE_ARCHIVED).getResultList();
    }

    @Override
    public List<BriefDocument> findActive() {
        return getEntityManager().createQuery(WHERE_NO_ARCHIVED).getResultList();
    }

    @Override
    public List<BriefDocument> findSeveralById(long[] ids) {
        if (ids == null || ids.length == 0) {
            return null;
        }
        String query = FROM.concat(" WHERE ");
        query = query.concat(" id=" + ids[0]);
        for (int i = 1; i < ids.length; i++) {
            query = query.concat(" OR id=" + ids[i]);
        }
        return getEntityManager().createQuery(query).getResultList();
    }
}
