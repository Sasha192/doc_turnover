package app.dao.impl;

import app.configuration.spring.constants.Constants;
import app.dao.IBriefDocumentDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.BriefDocument;
import java.util.List;
import java.util.Set;
import javax.persistence.Parameter;
import javax.persistence.Query;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository
public class BriefDocumentDao extends GenericJpaRepository<BriefDocument>
        implements IBriefDocumentDao {

    private static final String FROM = " FROM " + BriefDocument.class.getName() + " ";

    private static final String WHERE_ARCHIVED = FROM.concat(" WHERE is_archive=true");

    private static final String WHERE_NO_ARCHIVED = FROM.concat(" WHERE is_archive=false");

    private static final Logger LOGGER = Logger.getLogger(BriefDocument.class);

    private static final String AND = " and ";

    private static final String WHERE = " WHERE ";
    
    /*private static final String QUERY_FIND_BY_FILTERS = FROM
            + WHERE
            + (" (:fileName is null or file_name = :fileName) ")
            + (AND)
            + (" (:extName is null or ext_name = :extName) ")
            + (AND)
            + (" (:fullPath is null or full_path = :fullPath) ")
            + (AND)
            + (" (:creationDate is null or creation_date = :creationDate) ")
            + (AND)
            + (" (:like is null or file_name LIKE :like or ext_name LIKE :like) ")
            + (AND)
            + (" (:year is null or YEAR(creation_date)=:year) ")
            + (AND)
            + (" (:month is null or (MONTH(creation_date) + 1) = :month) ")
            + (AND)
            + (" (:day is null or DAY(creation_date) = :day) ")
            + (" ORDER BY creation_date ")
            + (" LIMIT : :offset , :pageSize");*/

    private static final String QUERY_FIND_BY_FILTERS =
            "CALL FILTERED_BRIEF_DOC_INFO("
                    + ":fileName, :extName, :fullPath, :creationDate,"
                    + ":like, :year, :month, :day, :offset, :pageSize)";

    private static final char PERCENTAGE = '%';
    private static final String ROWS_ON_PAGE_ARHIVE_DOC = "rows_on_page_arhive_doc";

    @Autowired
    @Qualifier("app_constants")
    private Constants constants;

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
    public List<BriefDocument> findBy(int pageId, String search,
                                      Integer year, Integer month,
                                      Integer day) {
        int pageSize = constants
                .get(ROWS_ON_PAGE_ARHIVE_DOC)
                .getIntValue();
        final int offset = pageSize * (pageId - 1);
        if (null != search) {
            search = PERCENTAGE + search + PERCENTAGE;
        }
        Query query = getEntityManager()
                .createStoredProcedureQuery(QUERY_FIND_BY_FILTERS, BriefDocument.class);
        query = setAllParametersToNull(query);
        query.setParameter("like", search);
        query.setParameter("year", year);
        query.setParameter("month", month);
        query.setParameter("day", day);
        query.setParameter("offset", offset);
        query.setParameter("pageSize", pageSize);
        return query.getResultList();
    }

    private Query setAllParametersToNull(Query query) {
        Set<Parameter<?>> parameters = query.getParameters();
        for (Parameter<?> parameter : parameters) {
            query.setParameter(parameter.getName(), null);
        }
        return query;
    }
}
