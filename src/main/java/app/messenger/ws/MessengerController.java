package app.messenger.ws.controllers;

import app.messenger.models.WSocketGetMessage;
import app.messenger.ws.UsernamesWebsocketHandler;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.security.Principal;
import java.util.LinkedList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MessengerController {

    @Autowired
    private SimpMessageSendingOperations messaging;

    @Autowired
    private UsernamesWebsocketHandler names;

    @MessageMapping("/send")
    public void sendMessage(Message msg,
                            SimpMessageHeaderAccessor headerAccessor) {
        JsonObject o = new JsonObject();
        o.add("msg", new JsonPrimitive("some message"));
        String user = headerAccessor.getUser().getName();
        messaging.convertAndSendToUser(user, "/queue/reply", o.toString());
    }

    @SubscribeMapping("/messages")
    public void retrieveMessages(Principal principal) {
        List<WSocketGetMessage> msgs = new LinkedList<>();
        messaging.convertAndSendToUser(
                principal.getName(),
                "/queue/reply",
                msgs
        );
    }

}
