package org.dikshit.videochat;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import java.util.HashMap;
import java.util.Map;

@Controller
public class WebSocketController {

    private static final Map<String, String> userStreams = new HashMap<>();

    @MessageMapping("/stream")
    @SendTo("/topic/stream")
    public Map<String, String> stream(Map<String, String> data) {
        // Assuming data contains a unique user identifier and stream data
        String userId = data.get("userId");
        String streamData = data.get("streamData");
        userStreams.put(userId, streamData);
        return new HashMap<>(userStreams); // Return a copy of the current streams
    }
}
