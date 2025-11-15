package com.example.wwork4.pojo.VO;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class MessageVO {
    private Integer senderId;
    private String senderName;

    private Integer receiverId;

    private Integer messageType;
    private String content;
    private String sessionId;
    private LocalDateTime time;
    private Integer contactType;
    private  Integer status;
}
