package app.dao.impl;

import app.dao.interfaces.IReportDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.Report;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDao
        extends GenericJpaRepository<Report>
        implements IReportDao {

    public ReportDao() {
        setClazz(Report.class);
    }
}
