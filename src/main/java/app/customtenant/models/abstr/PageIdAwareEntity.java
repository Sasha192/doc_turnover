package app.customtenant.models.abstr;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
// @See table Schema#pages_number
public class PageIdAwareEntity
        extends IdentityBaseEntity {

    private final transient String pageId;

    public PageIdAwareEntity(String pageId) {
        this.pageId = pageId;
    }

    public final String getPageId() {
        return pageId;
    }
}
