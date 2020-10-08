package app.tenantdefault.service.converters;

import org.bson.Document;

public interface IEntityConverter<T> {

    public T convert(Document document);

    public T convertExceptFileData(Document document);

}
