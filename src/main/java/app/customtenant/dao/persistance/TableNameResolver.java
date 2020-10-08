package app.customtenant.dao.persistance;

import javax.persistence.EntityManager;
import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.Metamodel;

public class TableNameResolver {
    public static <T> String getTableName(EntityManager em, Class<T> entityClass) {
        Metamodel meta = em.getMetamodel();
        EntityType<T> entityType = meta.entity(entityClass);
        Table t = entityClass.getAnnotation(Table.class);
        String tableName = (t == null)
                ? entityType.getName().toUpperCase()
                : t.name();
        return tableName;
    }
}
