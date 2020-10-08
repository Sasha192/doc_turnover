package app.customtenant.service.interfaces;

public interface IPerformerUpdateEventListenerService {

    boolean toUpdate(Long performerId);

    void setUpdate(Long performerId);

    void wasUpdated(Long peformerId);

}
