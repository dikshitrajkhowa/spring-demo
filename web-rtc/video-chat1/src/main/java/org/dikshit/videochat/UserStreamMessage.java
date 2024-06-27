package org.dikshit.videochat;

public class UserStreamMessage {

    private String userId;
    private String streamData;

    public UserStreamMessage() {}

    public UserStreamMessage(String userId, String streamData) {
        this.userId = userId;
        this.streamData = streamData;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStreamData() {
        return streamData;
    }

    public void setStreamData(String streamData) {
        this.streamData = streamData;
    }
}
