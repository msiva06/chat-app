package com.example.messaging.websocket.model;

public class ChatMessage {
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "{\"message\":\"" + getMessage() + "\"}";
    }


}
