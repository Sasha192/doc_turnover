package app.customtenant.dao.impl;

import app.customtenant.dao.interfaces.IReportCommentDao;
import app.customtenant.dao.persistance.GenericJpaRepository;
import app.customtenant.models.basic.Report;
import app.customtenant.models.basic.ReportComment;
import java.util.List;
import javax.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

@Repository
public class ReportCommentDao extends GenericJpaRepository<ReportComment>
        implements IReportCommentDao {

    private static final String FROM =
            " select rc from ReportComment rc ";

    private static final String FIND_BY_REPORT_ID =
            FROM + " WHERE report_id=:id ";

    public ReportCommentDao() {
        setClazz(ReportComment.class);
    }

    @Override
    public List<ReportComment> retrieveByReport(Report report) {
        return retrieveByReportId(report.getId());
    }

    @Override
    public List<ReportComment> retrieveByReportId(Long id) {
        TypedQuery<ReportComment> query = getEntityManager()
                .createQuery(FIND_BY_REPORT_ID, ReportComment.class);
        query.setParameter("id", id);
        return query.getResultList();
    }
}
