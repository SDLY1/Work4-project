package com.example.wwork4.utils;

import com.alibaba.fastjson.JSON;
import com.example.wwork4.mapper.ChatMapper;
import com.example.wwork4.mapper.UserMapper;
import com.example.wwork4.pojo.DO.MessageDO;
import com.example.wwork4.pojo.DTO.MessageDTO;
import com.example.wwork4.pojo.Message;
import com.example.wwork4.pojo.VO.UnreadVO;
import com.example.wwork4.rabbitmq.SaveChatMessageQueue;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
@Slf4j
@Component
@RequiredArgsConstructor
public class ChannelContextUtils {
    private final SaveChatMessageQueue saveChatMessageQueue;
    @Resource
    UserMapper userMapper;
    @Resource
    ChatMapper chatMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;
    private static final ConcurrentHashMap<String,Channel> USER_CONTEXT_MAP =new ConcurrentHashMap<>();
    private static final ConcurrentHashMap<String,ChannelGroup>  GROUP_CONTEXT_MAP =new ConcurrentHashMap<>();
    //用户上线
    public void  addContext(String userId, Channel channel){
        String channelId =channel.id().toString();
        AttributeKey attributeKey=null;
        if(!AttributeKey.exists(channelId)){
            attributeKey =AttributeKey.newInstance(channelId);
        }else {
            attributeKey =AttributeKey.valueOf(channelId);
        }
        channel.attr(attributeKey).set(userId);
        USER_CONTEXT_MAP.put(userId,channel);
        List<Integer> groupIdList=chatMapper.selectGroupIdByUserId(Integer.valueOf(userId));
        for(Integer i:groupIdList){
            addGroup(String.valueOf(i),channel);
        }
    }
   //添加至群组内
    private  void addGroup(String groupId,Channel channel){
        ChannelGroup group=GROUP_CONTEXT_MAP.get(groupId);
        if(group==null){
            group=new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
            GROUP_CONTEXT_MAP.put(groupId,group);
        }
        if(channel==null){
            return;
        }
        group.add(channel);
    }

    public  void removeContext(Channel channel){
        Attribute<String> attribute =channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId =attribute.get();
        if(userId != null){
            USER_CONTEXT_MAP.remove(userId);
            //更新用户最后离线时间
            userMapper.updateTime(Integer.valueOf(userId), LocalDateTime.now());

            List<Integer> groupIdList=chatMapper.selectGroupIdByUserId(Integer.valueOf(userId));
            for(Integer i:groupIdList){
                ChannelGroup channelGroup=GROUP_CONTEXT_MAP.get(String.valueOf(i));
                if(channelGroup!=null){
                    channelGroup.remove(channel);
                }
            }
        }

       channel.close();

    }
    //单聊发送消息
    public static boolean sendPersionMessage(Message message, Integer receiverId){
        if(receiverId==null){
            log.info("接收ID错误");
            return false;
        }
        // 取出对应的管道
        Channel channel=USER_CONTEXT_MAP.get(receiverId.toString());
        if(channel==null){
            log.info("用户不在线");
            return false;
        }
        //用fastjson将消息对象转成字符串
        String toJson= JSON.toJSONString(message);
        channel.writeAndFlush(new TextWebSocketFrame(toJson));
        return true;
    }
    //群聊发送消息
    public static Set<Integer> sendGroupMessage(Message message, Integer groupId){
        if(groupId==null){
            return null;
        }
        ChannelGroup channelGroup =GROUP_CONTEXT_MAP.get(groupId.toString());
        if(channelGroup==null){
            return null;
        }
        List<Integer> successUserIdList=new ArrayList<>();
        channelGroup.forEach(channel -> {channel.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(message)));
            String userId= (String) channel.attr(AttributeKey.valueOf(channel.id().toString())).get();
            if(userId!=null) {
                successUserIdList.add(Integer.valueOf(userId));
            }
        });
        return new HashSet<>(successUserIdList);
    }

    public void receiveMessage(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame textWebSocketFrame){
        Channel channel=channelHandlerContext.channel();
        Attribute<String> attribute =channel.attr(AttributeKey.valueOf(channel.id().toString()));
        String userId =attribute.get();
        log.info("收到userid{}的消息 {}", userId,textWebSocketFrame.text() );
        Message message=JSON.parseObject(textWebSocketFrame.text(), Message.class);
        //判断会话类型 U开头为单聊，G开头为群聊
        String contactType= message.getSessionId().substring(0,1);

        if("U".equals(contactType)){
            Boolean isSuccess=sendPersionMessage(message,message.getReceiverId());
            if(isSuccess) {
                saveChatMessageQueue.sendSaveChatMsgQueue(MessageUtils.changeMessage(message));
            }else{
                //0表示未发送
                message.setStatus(0);
                String unreadCountKey="unread:count:uid:{"+userId+"}:room:{"+message.getSessionId()+"}";
                stringRedisTemplate.opsForValue().increment(unreadCountKey);
                saveChatMessageQueue.sendSaveChatMsgQueue(MessageUtils.changeMessage(message));
            }
            return;
        }
        if("G".equals(contactType)){
            Set<Integer> successList = sendGroupMessage(message, message.getReceiverId());
            saveChatMessageQueue.sendSaveChatMsgQueue(MessageUtils.changeMessage(message));
            List<Integer> totalList=chatMapper.getUserIdByGroupId(message.getReceiverId());
            for(Integer i:totalList){
                if(!successList.contains(i)){
                    String unreadCountKey="unread:count:uid:{"+i+"}:room:{"+message.getSessionId()+"}";
                    stringRedisTemplate.opsForValue().increment(unreadCountKey);
                }
            }
            return ;
        }
        throw new RuntimeException("会话ID有误");
    }



}
