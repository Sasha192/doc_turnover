package app.controllers.customtenant;

import com.google.gson.JsonElement;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@Component
public class ServerSentNotifications {

    private static final Map<String, SseEmitter> connections;

    static {
        // case, when two users with hash(key) = hash(key')
        // are trying to insert their SseEmiter
        connections = new ConcurrentHashMap<>();
    }

    public void put(String id) {
        SseEmitter emit = new SseEmitter(86400000L);
        connections.put(id, emit);
        emit.onCompletion(() -> removeEmitter(id));
        emit.onError((e) -> removeEmitter(id));
        emit.onTimeout(() -> removeEmitter(id));
    }

    public void send(String id, JsonElement element, ServerEvent serverEvent)
            throws IOException {
        SseEmitter.SseEventBuilder builder = SseEmitter.event().name(serverEvent.getLabel())
                .data(element, MediaType.APPLICATION_JSON);
        SseEmitter emitter = connections.get(id);
        emitter.send(builder);
    }

    public boolean contains(String id) {
        return connections.containsKey(id);
    }

    public static void removeEmitter(String id) {
        connections.remove(id);
    }

    public static String getId(Long performerId, String tenantId) {
        return tenantId + performerId;
    }

    public enum ServerEvent {
        NOTIFICATION("NOTIFICATION"), INIT("INIT");

        private String label;

        ServerEvent(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }

}
