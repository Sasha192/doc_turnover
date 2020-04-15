package app.dao;

import app.dao.persistance.IGenericDao;
import app.models.BriefDocument;

import java.rmi.NoSuchObjectException;

public interface IBriefDocumentDao extends IGenericDao<BriefDocument> {

    //

    BriefDocument retrieveByName(String name) throws NoSuchObjectException;

}



