package com.example.wwork4.utils;

import com.example.wwork4.pojo.DO.MessageDO;
import com.example.wwork4.pojo.DTO.MessageDTO;
import com.example.wwork4.pojo.Message;

public class MessageUtils {
    public  static MessageDTO changeMessage(Message message){
        MessageDTO messageDTO=new MessageDTO();
        MessageDO messageDO=new MessageDO();
        messageDO.setSenderName(messageDO.getSenderName());
        messageDO.setMessageType(message.getMessageType());
        messageDO.setContent(message.getContent());
        messageDO.setStatus(message.getStatus());
        messageDO.setTime(message.getTime());
        messageDO.setReceiverId(message.getReceiverId());
        messageDO.setSenderId(message.getSenderId());
        messageDO.setContactType(messageDO.getContactType());
        messageDO.setSessionId(message.getSessionId());
        messageDTO.setMessageDO(messageDO);
        return messageDTO;
    }

}
