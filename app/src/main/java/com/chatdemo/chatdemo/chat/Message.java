package com.chatdemo.chatdemo.chat;

/**
 * Created by baris on 13/01/2018.
 */

public class Message {

    private String message, name, avatarURL;
    private long timestamp;
    private boolean isBelongedToYou;

    public Message(String message, String name, String avatarURL, long timestamp, boolean isBelongedToYou) {
        this.message = message;
        this.name = name;
        this.avatarURL = avatarURL;
        this.timestamp = timestamp;
        this.isBelongedToYou = isBelongedToYou;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getAvatarURL() {
        return avatarURL;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public boolean isBelongedToYou() {
        return isBelongedToYou;
    }
}
