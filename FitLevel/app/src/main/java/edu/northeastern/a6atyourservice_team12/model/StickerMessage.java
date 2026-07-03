package edu.northeastern.a6atyourservice_team12.model;

public class StickerMessage {

    private String messageId;
    private String senderUsername;
    private String receiverUsername;
    private String stickerId;
    private long timestamp;


    public StickerMessage() {
    }

    public StickerMessage(String senderUsername, String receiverUsername,
                          String stickerId, long timestamp) {
        this.senderUsername = senderUsername;
        this.receiverUsername = receiverUsername;
        this.stickerId = stickerId;
        this.timestamp = timestamp;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getSenderUsername() {
        return senderUsername;
    }

    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }

    public String getReceiverUsername() {
        return receiverUsername;
    }

    public void setReceiverUsername(String receiverUsername) {
        this.receiverUsername = receiverUsername;
    }

    public String getStickerId() {
        return stickerId;
    }

    public void setStickerId(String stickerId) {
        this.stickerId = stickerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}