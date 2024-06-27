package org.dikshit.videochat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Base64;

@Controller
public class WebSocketController {

    private Map<String, String> userStreams = new ConcurrentHashMap<>();

    @MessageMapping("/stream")
    @SendTo("/topic/stream")
    public Map<String, String> stream(UserStreamMessage message) {
        userStreams.put(message.getUserId(), message.getStreamData());
        return userStreams;
    }

    @MessageMapping("/stopStream")
    @SendTo("/topic/stopStream")
    public UserStopMessage stopStream(UserStopMessage message) {
        userStreams.remove(message.getUserId());
        return message;
    }
}
