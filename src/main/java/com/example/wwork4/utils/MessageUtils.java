package com.example.wwork4.utils;

import com.example.wwork4.pojo.DO.MessageDO;
import com.example.wwork4.pojo.DTO.MessageDTO;
import com.example.wwork4.pojo.Message;
import com.example.wwork4.pojo.VO.MessageVO;

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

    public static MessageVO changeMessageToVO(Message message){
        MessageVO messageVO=new MessageVO();
        messageVO.setMessageType(message.getMessageType());
        if("U".equals(message.getSessionId().substring(0,1))) {
            messageVO.setContactType(0);
        }else{
            messageVO.setContactType(1);
        }
        messageVO.setContent(message.getContent());
        messageVO.setTime(message.getTime());
        messageVO.setSenderId(message.getSenderId());
        messageVO.setReceiverId(message.getReceiverId());
        messageVO.setStatus(message.getStatus());
        messageVO.setSenderName(messageVO.getSenderName());
        messageVO.setSessionId(message.getSessionId());
        return messageVO;
    }

}
