package com.example.swiftcare.models;

import java.util.Date;

public class Chat {
    public String senderId, receiverId, message, dateTime, latestSender;
    public Date dateObject;

    public String getLatestSender() {
        return latestSender;
    }

    public void setLatestSender(String latestSender) {
        this.latestSender = latestSender;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }

}
