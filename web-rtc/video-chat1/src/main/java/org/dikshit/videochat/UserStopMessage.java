package org.dikshit.videochat;

public class UserStopMessage {

    private String userId;

    public UserStopMessage() {}

    public UserStopMessage(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
