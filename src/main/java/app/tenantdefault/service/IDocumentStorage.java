package app.tenantdefault.service;

import app.tenantdefault.models.DocumentEntity;
import app.tenantdefault.models.DocumentSignEntity;

public interface IDocumentStorage {

    void save(DocumentEntity doc);

    DocumentEntity find(String uuid);

    DocumentEntity findForJson(String uuid);

    void addSign(String uuid, DocumentSignEntity sign);

}
