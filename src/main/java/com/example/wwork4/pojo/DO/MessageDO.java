package com.example.wwork4.pojo.DO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageDO {
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
