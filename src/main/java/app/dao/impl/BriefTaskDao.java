package app.dao.impl;

import app.dao.interfaces.IBriefTaskDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.Performer;
import app.models.mysqlviews.BriefTask;
import java.util.List;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

@Repository
public class BriefTaskDao extends GenericJpaRepository<BriefTask>
        implements IBriefTaskDao {

    private static final String FROM = " select bt from BriefTask bt ";

    private static final String FIND_BY_DEPO =
            " select bt FROM BriefTask bt "
                    + " JOIN bt.performers perfs "
                    + " WHERE perfs.departmentId = :depo_id_ ";

    private static final String FIND_BY_PERFORMER = FROM
            + " INNER JOIN bt.performers perf ON perf.id = :performer_id ";

    private static final String FIND_BY_STATUS_N_DEPO =
            FIND_BY_DEPO
            + " AND bt.status = :status_name_ ";

    private static final String FIND_BY_PERF_N_STATUS = FIND_BY_PERFORMER
            + " AND bt.status = :status_name_ ";

    private static final String FIND_BY_STATUS = FROM
            + " WHERE bt.status = :status_name_";

    public BriefTaskDao() {
        setClazz(BriefTask.class);
    }

    @Override
    public List<BriefTask> findByPerformer(Long performerId) {
        TypedQuery<BriefTask> query = getEntityManager()
                .createQuery(FIND_BY_PERFORMER, BriefTask.class);
        query.setParameter("performer_id", performerId);
        return query.getResultList();
    }

    @Override
    public List<BriefTask> findByPerformer(Performer performer) {
        return findByPerformer(performer.getId());
    }

    @Override
    public List<BriefTask> findByPerformerAndStatus(Long performerId, String status) {
        return getEntityManager()
                .createQuery(FIND_BY_PERF_N_STATUS, BriefTask.class)
                .setParameter("performer_id", performerId)
                .setParameter("status_name_", status)
                .getResultList();
    }

    @Override
    public List<BriefTask> findByStatus(String status) {
        TypedQuery<BriefTask> query = getEntityManager()
                .createQuery(FIND_BY_PERF_N_STATUS, BriefTask.class);
        query.setParameter("status_name_", status);
        return query.getResultList();
    }

    @Override
    public List<BriefTask> findByDepartment(Long depoId) {
        return getEntityManager()
                .createQuery(FIND_BY_DEPO, BriefTask.class)
                .setParameter("depo_id_", depoId)
                .getResultList();
    }

    @Override
    public List<BriefTask> findByDepartmentAndStatus(Long depoId, String status) {
        return getEntityManager()
                .createQuery(FIND_BY_STATUS_N_DEPO, BriefTask.class)
                .setParameter("depo_id_", depoId)
                .setParameter("status_name_", status)
                .getResultList();
    }

    @Override
    public BriefTask create(BriefTask entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BriefTask update(BriefTask entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(BriefTask entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(long entityId) {
        throw new UnsupportedOperationException();
    }
}
