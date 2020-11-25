package app.tenantdefault.service;

import app.tenantconfiguration.TenantConnectionProvider;
import app.tenantdefault.dao.ITenantDao;
import app.tenantdefault.models.TenantInfoEntity;
import java.util.Collection;
import java.util.List;

import org.bson.BsonDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class TenantService implements ITenantService {

    private ITenantDao dao;

    private TenantConnectionProvider tenantProvider;

    @Autowired
    public TenantService(ITenantDao dao) {
        this.dao = dao;
    }

    @Override
    public Collection<TenantInfoEntity> findPageableOpen(int page) {
        return dao.findPageableOpen(page);
    }

    @Override
    public Collection<TenantInfoEntity> findPageableFilter(int page, BsonDocument filter) {
        return dao.findPageableFilter(page, filter);
    }

    @Override
    public Collection<TenantInfoEntity> findMyTenants(Collection<String> uuids) {
        return dao.findMyTenants(uuids);
    }

    @Override
    public TenantInfoEntity findById(String tenantId) {
        return dao.findById(tenantId);
    }

    @Override
    public Collection<String> findTenants() {
        return dao.findTenants();
    }

    @Override
    public void remove(String tenantId) {
        dao.remove(tenantId);
    }
}
