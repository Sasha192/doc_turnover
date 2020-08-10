package app.dao.impl;

import app.dao.interfaces.IDepartmentDao;
import app.dao.persistance.GenericJpaRepository;
import app.models.basic.Department;
import java.util.List;
import org.springframework.stereotype.Repository;

@Repository
public class DepartmentDao extends GenericJpaRepository<Department>
        implements IDepartmentDao {

    private static final String RETRIEVE_BY_NAME =
            " FROM Department WHERE department_name=:name";

    private static final String RETRIEVE_BY_PARENT =
            " FROM Department WHERE parent_department_id=:parent_id";

    private static final String UPDATE_NAME =
            "UPDATE Department SET department_name = :newName_ WHERE id = :depoId_";

    public DepartmentDao() {
        setClazz(Department.class);
    }

    @Override
    public Department retrieveOneByName(String name) {
        List<Department> depos = getEntityManager().createQuery(RETRIEVE_BY_NAME, Department.class)
                .setParameter("name", name)
                .getResultList();
        if (depos != null && (!depos.isEmpty())) {
            return depos.get(0);
        }
        return null;
    }

    @Override
    public List<Department> retrieveByParent(Long parentId) {
        return getEntityManager()
                .createQuery(RETRIEVE_BY_PARENT, Department.class)
                .setParameter("parent_id", parentId)
                .getResultList();
    }

    @Override
    public void changeDeparmentName(String newName, Long depId) {
        getEntityManager().createQuery(UPDATE_NAME)
                .setParameter("newName_", newName)
                .setParameter("depoId_", depId)
                .executeUpdate();
    }
}
