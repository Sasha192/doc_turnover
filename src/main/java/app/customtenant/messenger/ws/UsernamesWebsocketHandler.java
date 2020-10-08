package app.customtenant.messenger.ws;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class UsernamesWebsocketHandler {

    private Set<String> names = ConcurrentHashMap.newKeySet();

    public void postUsername(String username) {
        names.add(username);
    }

    public Set<String> getAllUsernames() {
        return names;
    }

}
