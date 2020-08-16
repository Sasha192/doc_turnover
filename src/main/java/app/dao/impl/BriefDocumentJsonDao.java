package app.dao.impl;

import app.configuration.spring.constants.Constants;
import app.dao.interfaces.IBriefDocumentJsonDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.mysqlviews.BriefJsonDocument;
import java.util.List;
import java.util.Set;
import javax.persistence.Parameter;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class BriefDocumentJsonDao extends GenericJpaRepository<BriefJsonDocument>
        implements IBriefDocumentJsonDao {

    private static final String FROM = " FROM " + BriefJsonDocument.class.getName() + " ";

    private static final String WHERE_ARCHIVED = FROM.concat(" WHERE is_archive=true");

    private static final String WHERE_NO_ARCHIVED = FROM.concat(" WHERE is_archive=false");

    private static final Logger LOGGER = Logger.getLogger(BriefJsonDocument.class);

    private static final String AND = " and ";

    private static final String WHERE = " WHERE ";

    private static final String QUERY_FIND_BY_FILTERS;

    private static final String QUERY_FIND_BY_FILTERS_FOR_PERFORMER;

    private static final String QUERY_FIND_BY_FILTERS_FOR_DEPARTMENT;

    static {
        QUERY_FIND_BY_FILTERS =
                "FROM BriefJsonDocument bd WHERE\n"
                + "     bd.id > :offsetValue and\n"
                + "    (:fileName is null or bd.name = :fileName)  and\n"
                + "    (:extName is null or bd.extName = :extName)  and\n"
                + "    (:creationDate is null or bd.date = :creationDate)  and\n"
                + "    (:patterLike is null or bd.name LIKE :patterLike "
                + "       or bd.extName LIKE :patterLike)  and\n"
                + "    (:yearValue is null or YEAR(bd.date)=:yearValue)  and\n"
                + "    (:monthValue is null or MONTH(bd.date) = :monthValue)  and\n"
                + "    (:dayValue is null or DAY(bd.date) = :dayValue)\n"
                + "     ORDER BY bd.id DESC";

        QUERY_FIND_BY_FILTERS_FOR_PERFORMER =
                "SELECT bd.id, bd.creation_date, bd.file_name,"
                        + " bd.ext_name, bd.task_count,"
                        + " bd.performer_id, bd.department_id,"
                        + " bd.performer_name, bd.department_name "
                        + " FROM json_view_brief_archive_doc bd "
                        + " INNER JOIN tasks_documents td ON td.doc_id = bd.id "
                        + " INNER JOIN tasks_performers tp ON tp.task_id = td.task_id "
                        + " WHERE tp.performer_id = :perf_id_ AND "
                        + "     bd.id > :offsetValue and\n"
                        + "    (:fileName is null or bd.name = :fileName)  and\n"
                        + "    (:extName is null or bd.extName = :extName)  and\n"
                        + "    (:creationDate is null or bd.date = :creationDate)  and\n"
                        + "    (:patterLike is null or bd.name LIKE :patterLike "
                        + "       or bd.extName LIKE :patterLike)  and\n"
                        + "    (:yearValue is null or YEAR(bd.date)=:yearValue)  and\n"
                        + "    (:monthValue is null or MONTH(bd.date) = :monthValue)  and\n"
                        + "    (:dayValue is null or DAY(bd.date) = :dayValue)\n"
                        + "     ORDER BY bd.id DESC";

        QUERY_FIND_BY_FILTERS_FOR_DEPARTMENT =
                "SELECT bd.id, bd.creation_date, bd.file_name,"
                        + " bd.ext_name, bd.task_count,"
                        + " bd.performer_id, bd.department_id,"
                        + " bd.performer_name, bd.department_name "
                        + " FROM json_view_brief_archive_doc bd "
                        + " INNER JOIN tasks_documents td ON td.doc_id = bd.id "
                        + " INNER JOIN tasks_performers tp ON tp.task_id = td.task_id "
                        + " INNER JOIN performers per ON per.id = tp.performer_id "
                        + " WHERE per.department_id = :depo_id_  AND "
                        + "     bd.id > :offsetValue and\n"
                        + "    (:fileName is null or bd.name = :fileName)  and\n"
                        + "    (:extName is null or bd.extName = :extName)  and\n"
                        + "    (:creationDate is null or bd.date = :creationDate)  and\n"
                        + "    (:patterLike is null or bd.name LIKE :patterLike "
                        + "       or bd.extName LIKE :patterLike)  and\n"
                        + "    (:yearValue is null or YEAR(bd.date)=:yearValue)  and\n"
                        + "    (:monthValue is null or MONTH(bd.date) = :monthValue)  and\n"
                        + "    (:dayValue is null or DAY(bd.date) = :dayValue)\n"
                        + "     ORDER BY bd.id DESC";
    }

    private static final char PERCENTAGE = '%';
    private static final String ROWS_ON_PAGE_ARHIVE_DOC = "rows_on_page_arhive_doc";

    @Autowired
    private Constants constants;

    public BriefDocumentJsonDao() {
        this.setClazz(BriefJsonDocument.class);
    }

    @Override
    public List<BriefJsonDocument> findArchived() {
        return this.getEntityManager().createQuery(WHERE_ARCHIVED).getResultList();
    }

    @Override
    public List<BriefJsonDocument> findActive() {
        return this.getEntityManager().createQuery(WHERE_NO_ARCHIVED).getResultList();
    }

    @Override
    public List<BriefJsonDocument> findBy(final int pageId, String search,
                                          final Integer year, final Integer month,
                                          final Integer day) {
        final int pageSize = getPageSize();
        if (null != search) {
            search = PERCENTAGE + search + PERCENTAGE;
        }
        Query query = this.getEntityManager()
                .createQuery(QUERY_FIND_BY_FILTERS, BriefJsonDocument.class);
        query = this.setAllParametersToNull(query);
        query.setParameter("patterLike", search);
        query.setParameter("yearValue", year);
        query.setParameter("monthValue", month);
        query.setParameter("dayValue", day);
        int offset = pageSize * (pageId - 1);
        query.setParameter("offsetValue", Long.valueOf(offset));
        query.setMaxResults(pageSize);
        query.setHint("org.hibernate.readOnly", Boolean.TRUE);
        return query.getResultList();
    }

    private int getPageSize() {
        return this.constants
                .get(ROWS_ON_PAGE_ARHIVE_DOC)
                .getIntValue();
    }

    private Query setAllParametersToNull(final Query query) {
        final Set<Parameter<?>> parameters = query.getParameters();
        for (final Parameter<?> parameter : parameters) {
            query.setParameter(parameter.getName(), null);
        }
        return query;
    }

    @Override
    public List<BriefJsonDocument> findByAndPerformerInTaskId(int pageId,
                                                              String search,
                                                              Integer yearInt,
                                                              Integer monthInt,
                                                              Integer dayInt,
                                                              Long performerId) {
        final int pageSize = getPageSize();
        if (null != search) {
            search = PERCENTAGE + search + PERCENTAGE;
        }
        Query query = this.getEntityManager()
                .createQuery(QUERY_FIND_BY_FILTERS_FOR_PERFORMER, BriefJsonDocument.class);
        query = this.setAllParametersToNull(query);
        query.setParameter("patterLike", search);
        query.setParameter("yearValue", yearInt);
        query.setParameter("monthValue", monthInt);
        query.setParameter("dayValue", dayInt);
        int offset = pageSize * (pageId - 1);
        query.setParameter("offsetValue", Long.valueOf(offset));
        query.setMaxResults(pageSize);
        query.setHint("org.hibernate.readOnly", Boolean.TRUE);
        query.setParameter("perf_id_", performerId);
        return query.getResultList();
    }

    @Override
    public List<BriefJsonDocument> findByAndDepartment(int pageId,
                                                       String search,
                                                       Integer yearInt,
                                                       Integer monthInt,
                                                       Integer dayInt,
                                                       Long departmentId) {
        final int pageSize = getPageSize();
        if (null != search) {
            search = PERCENTAGE + search + PERCENTAGE;
        }
        Query query = this.getEntityManager()
                .createQuery(QUERY_FIND_BY_FILTERS_FOR_PERFORMER, BriefJsonDocument.class);
        query = this.setAllParametersToNull(query);
        query.setParameter("patterLike", search);
        query.setParameter("yearValue", yearInt);
        query.setParameter("monthValue", monthInt);
        query.setParameter("dayValue", dayInt);
        int offset = pageSize * (pageId - 1);
        query.setParameter("offsetValue", Long.valueOf(offset));
        query.setMaxResults(pageSize);
        query.setHint("org.hibernate.readOnly", Boolean.TRUE);
        query.setParameter("depo_id_", departmentId);
        return query.getResultList();
    }

    @Override
    public BriefJsonDocument create(final BriefJsonDocument entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public BriefJsonDocument update(final BriefJsonDocument entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(final BriefJsonDocument entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void deleteById(final long entityId) {
        throw new UnsupportedOperationException();
    }
}
