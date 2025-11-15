package com.example.wwork4.pojo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Message {
    private Integer senderId;
    private Integer receiverId;
    private Integer messageType;
    private String content;
    private String sessionId;
    private LocalDateTime time;
    private Integer status;
}
