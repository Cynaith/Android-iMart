package com.ly.imart.bean.Second;

/**
 * Created by moos on 2018/4/20.
 */

public class ReplyDetailBean {
    private String nickName;
    private String userLogo;
    private int id;
    private int userId;

    public ReplyDetailBean() {
    }

    public ReplyDetailBean(String nickName, String userLogo, int id, int userId, String commentId, String content, String status, String createDate) {
        this.nickName = nickName;
        this.userLogo = userLogo;
        this.id = id;
        this.userId = userId;
        this.commentId = commentId;
        this.content = content;
        this.status = status;
        this.createDate = createDate;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    private String commentId;
    private String content;
    private String status;
    private String createDate;

    public ReplyDetailBean(String nickName, String content) {
        this.nickName = nickName;
        this.content = content;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
    public String getNickName() {
        return nickName;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }
    public String getUserLogo() {
        return userLogo;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }
    public String getCommentId() {
        return commentId;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public String getContent() {
        return content;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public String getStatus() {
        return status;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
    public String getCreateDate() {
        return createDate;
    }

}
