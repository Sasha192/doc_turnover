package app.customtenant.dao.impl;

import app.customtenant.dao.interfaces.IBriefDocumentDao;
import app.customtenant.dao.persistance.GenericJpaRepository;
import app.customtenant.models.basic.BriefDocument;
import java.util.Date;
import java.util.List;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.TypedQuery;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

@Repository
public class BriefDocumentDao extends GenericJpaRepository<BriefDocument>
        implements IBriefDocumentDao {

    private static final String FROM = " FROM " + BriefDocument.class.getName() + " bd ";

    private static final Logger LOGGER = Logger.getLogger(BriefDocument.class);

    private static final String FIND_BY_DATE =
            FROM
            + " WHERE bd.date = :date_ ";

    private static final String FIND_BY_TIME =
            FROM
            + " WHERE bd.time BETWEEN :start_time AND :end_time ";

    private static final String FIND_BY_DATE_DEPO_ID =
            "SELECT id, file_name, ext_name, creation_date, "
                    + "creation_time, uuid, performer_id, signed FROM brief_documents\n"
                    + "INNER JOIN tasks_documents td on brief_documents.id = td.doc_id\n"
                    + "INNER JOIN tasks_performers tp on td.task_id = tp.task_id\n"
                    + "INNER JOIN performers p on tp.performer_id = p.id\n"
                    + "WHERE creation_date <= :date_ AND p.department_id = :depo_id";

    private static final String FIND_BY_DATE_PERF_ID =
            "SELECT id, file_name, ext_name, creation_date, "
                    + "creation_time, uuid, performer_id, signed FROM brief_documents \n"
                    + "INNER JOIN tasks_documents td on brief_documents.id = td.doc_id \n"
                    + "INNER JOIN tasks_performers tp on td.task_id = tp.task_id \n"
                    + " WHERE creation_date <= :date_ AND tp.id = :perf_id \n";

    public BriefDocumentDao() {
        setClazz(BriefDocument.class);
    }

    @Override
    public List<BriefDocument> findBy(int page, int pageSize, Date date) {
        TypedQuery<BriefDocument> query = getEntityManager()
                .createQuery(FIND_BY_DATE, BriefDocument.class);
        query.setParameter("date_", date, TemporalType.DATE);
        return offsetLimitQuery(page, pageSize, query);
    }

    @Override
    public List<BriefDocument> findBy(int page, int pageSize, long start, long end) {
        TypedQuery<BriefDocument> query = getEntityManager()
                .createQuery(FIND_BY_TIME, BriefDocument.class);
        query.setParameter("start_time", start);
        query.setParameter("end_time", end);
        return offsetLimitQuery(page, pageSize, query);
    }

    @Override
    public List<BriefDocument> findByAndDepartment(int page, int pageSize, Date date, long depoId) {
        Query query = getEntityManager()
                .createNativeQuery(FIND_BY_DATE_DEPO_ID, BriefDocument.class);
        query.setParameter("date_", date, TemporalType.DATE);
        query.setParameter("depo_id", depoId);
        return offsetLimitQuery(page, pageSize, query);
    }

    @Override
    public List<BriefDocument> findByAndPerformerInTaskId(int page, int pageSize,
                                                          Date date, long perfId) {
        Query query = getEntityManager()
                .createNativeQuery(FIND_BY_DATE_PERF_ID, BriefDocument.class);
        query.setParameter("date_", date, TemporalType.DATE);
        query.setParameter("perf_id", perfId);
        return offsetLimitQuery(page, pageSize, query);
    }

    private List<BriefDocument> offsetLimitQuery(int page, int pageSize, Query query) {
        query.setMaxResults(pageSize);
        query.setFirstResult((page - 1) * pageSize);
        return query.getResultList();
    }

    @Override
    public void delete(BriefDocument entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(long entityId) {
        throw new UnsupportedOperationException();
    }
}
