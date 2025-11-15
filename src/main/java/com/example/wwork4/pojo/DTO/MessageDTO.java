package com.example.wwork4.pojo.DTO;

import com.example.wwork4.pojo.DO.MessageDO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class MessageDTO {
//    private Integer messageId;
    private MessageDO messageDO;
    private String uuid;
}
