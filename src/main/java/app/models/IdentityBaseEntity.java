package app.models;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class IdentityBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Access(AccessType.FIELD)
    protected Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
