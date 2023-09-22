package com.example.secondhandplatforms;

public class messageobject {
    private String    id;
    private String   fromUserId;
    private String   fromUsername;
    private String   content;
    private String   userId;
    private String  username;
    private String  status;
    private String  createTime;

    public messageobject(String id, String fromUserId,
                         String fromUsername, String content,
                         String userId, String username,
                         String status, String createTime) {
        this.id = id;
        this.fromUserId = fromUserId;
        this.fromUsername = fromUsername;
        this.content = content;
        this.userId = userId;
        this.username = username;
        this.status = status;
        this.createTime = createTime;
    }
    public messageobject(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromUserId() {
        return fromUserId;
    }

    public void setFromUserId(String fromUserId) {
        this.fromUserId = fromUserId;
    }

    public String getFromUsername() {
        return fromUsername;
    }

    public void setFromUsername(String fromUsername) {
        this.fromUsername = fromUsername;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
