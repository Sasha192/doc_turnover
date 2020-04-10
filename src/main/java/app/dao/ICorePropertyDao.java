package app.dao;

import app.models.CoreProperty;
import org.springframework.stereotype.Repository;

@Repository
public interface ICorePropertyDao {

    Integer insert(CoreProperty property);

    CoreProperty findByName(String name);
}
