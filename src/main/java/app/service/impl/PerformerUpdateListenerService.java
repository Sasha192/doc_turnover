package app.service.impl;

import app.service.interfaces.IPerformerUpdateEventListenerService;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Service;

@Service("perf_update_listener")
@Deprecated
/**
 * @TODO : Same questions
 * 1) is it normal to use ConcurrentSet (via HashMap) ? is it working as i want ?
 * 2) what if OutOfMemory error ?
 * 3) Should i write it with connection to DB ? special table, as verification_code_table ??
 * 4) Set ? Does it use SelfBalanced Trees instead of LList ?
 * 5) What is better solution, if user was updated ?
 * 5.1) Store ids, like this approach ?
 * 5.2) Or refresh every Constants.PERFORMER_SESSION_INTERVAL_SEC seconds ?
 */
public class PerformerUpdateListenerService
        implements IPerformerUpdateEventListenerService {

    private static final Set<Long> PERFORMER_IDS = ConcurrentHashMap.newKeySet();

    @Override
    public boolean toUpdate(Long performerId) {
        return PERFORMER_IDS.contains(performerId);
    }

    @Override
    public void setUpdate(Long performerId) {
        PERFORMER_IDS.add(performerId);
    }

    @Override
    public void wasUpdated(Long peformerId) {
        PERFORMER_IDS.remove(peformerId);
    }
}
