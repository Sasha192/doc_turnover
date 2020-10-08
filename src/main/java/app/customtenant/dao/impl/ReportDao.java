package app.customtenant.dao.impl;

import app.customtenant.dao.interfaces.IReportDao;
import app.customtenant.dao.persistance.GenericJpaRepository;
import app.customtenant.models.basic.Report;
import org.springframework.stereotype.Repository;

@Repository
public class ReportDao
        extends GenericJpaRepository<Report>
        implements IReportDao {

    public ReportDao() {
        setClazz(Report.class);
    }
}
