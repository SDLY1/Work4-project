package com.example.wwork4.rabbitmq;

import com.alibaba.fastjson.JSON;
import com.example.wwork4.mapper.ChatMapper;
import com.example.wwork4.mapper.UserMapper;
import com.example.wwork4.pojo.DO.MessageDO;
import com.example.wwork4.pojo.DTO.MessageDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveChatMessageQueue {
    private  final  RedisTemplate redisTemplate;
    private  final RabbitTemplate rabbitTemplate;
    private  final ChatMapper chatMapper;
    private final UserMapper userMapper;
    public void sendSaveChatMsgQueue(MessageDTO messageDTO) {
        String toJson=JSON.toJSONString(messageDTO);
        String uuid= UUID.randomUUID().toString();
        messageDTO.setUuid(uuid);
        rabbitTemplate.convertAndSend("save.chat.queue",messageDTO);
    }
    @RabbitListener(queues = "save.chat.queue")
    public void listenSaveChatMsgQueue(MessageDTO messageDTO) throws Exception {
        log.info("{}队列收到消息：{}","save.chat.queue",messageDTO);
//       MessageDTO messageDTO = (MessageDTO) JSON.parse(msg);
        MessageDO messageDO=messageDTO.getMessageDO();
        messageDO.setSenderName(userMapper.getUsernameByUserId(messageDO.getSenderId()));
        if (messageDO == null) throw new Exception("messageDO为空");
        // 验证UUID防止重复
        String uuid = messageDTO.getUuid();
        if (uuid == null) {
            log.error("消息uuid为空");
            return;
        }
        String key="messageUuId:"+uuid;
        if (!(redisTemplate.opsForValue().get(key)==null)){
            log.warn("消息重复:{}", uuid);
            return;
        }
        redisTemplate.opsForValue().setIfAbsent(key,uuid,5L, TimeUnit.SECONDS);
        chatMapper.addMessage(messageDO);
    }
}
