package com.example.wwork4.services.impl;

import com.alibaba.fastjson.JSON;
import com.example.wwork4.mapper.ChatMapper;
import com.example.wwork4.mapper.UserMapper;
import com.example.wwork4.pojo.DO.GroupDO;
import com.example.wwork4.pojo.DO.MessageDO;
import com.example.wwork4.pojo.Message;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.pojo.VO.MessageVO;
import com.example.wwork4.pojo.VO.SessionVO;
import com.example.wwork4.pojo.VO.UnreadVO;
import com.example.wwork4.services.ChatService;
import com.example.wwork4.utils.MessageUtils;
import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Service
public class ChatServiceImpl implements ChatService {
    private static final String MESSAGE_KEY_PREFIX = "chat:session:";
    @Resource
    ChatMapper chatMapper;
    @Resource
    UserMapper userMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Resource
    RedisTemplate redisTemplate;
    @Transactional
    @Override
    public Result addContact(Integer user1Id, Integer user2Id) {
        String sessionId="U"+Math.max(user1Id,user2Id)+"_"+Math.min(user1Id,user2Id);
        Integer isExist= chatMapper.isExist(sessionId);
        // 判断联系人是否已经添加
        if(isExist==0||isExist==null) {
            // 通过用户ID获得用户名
            String username1 = userMapper.getUsernameByUserId(user1Id);
            String username2 = userMapper.getUsernameByUserId(user2Id);
            chatMapper.addSession(user1Id,user2Id,sessionId,username2,0);
            chatMapper.addSession(user2Id,user1Id,sessionId,username1,0);
        }else{
            return Result.error("联系人已添加");
        }
        return Result.success();
    }

    @Override
    public Result blockSomeone(Integer userId, Integer targetId) {
        Boolean success=chatMapper.blockSomeone(userId,targetId);
        if(success){
            return Result.success();
        }
        return Result.error("屏蔽失败");
    }

    @Override
    public Result getSessionList(Integer userId) {
        List<SessionVO> sessionVOList= chatMapper.getSessionList(userId);


        for(SessionVO i:sessionVOList){
            String unreadCountKey="unread:count:uid:{"+userId+"}:room:{"+i.getSessionId()+"}";
           i.setUnReadCount((stringRedisTemplate.opsForValue().get(unreadCountKey)));
        }
        return Result.success(sessionVOList);
    }
    @Transactional
    @Override
    public Result addGroup(Integer leaderId, String groupName, List<String> userIds,String text) {
        // 判断用户数组是否正确
        List<Integer> user_id=new ArrayList<>();
        for(String i:userIds){
            Integer pos=Integer.valueOf(i);
            if(pos==null){
                return Result.error("用户ID有误");
            }else{
                user_id.add(pos);
            }
        }
        user_id.add(leaderId);
        //通过时间戳来设定群组的sessionId
        String sessionId="G"+leaderId+System.currentTimeMillis();
        GroupDO groupDO=new GroupDO();
        groupDO.setLeaderId(leaderId);
        groupDO.setGroupName(groupName);
        groupDO.setCount(user_id.size());
        groupDO.setText(text);
        groupDO.setSessionId(sessionId);
        chatMapper.addGroup(groupDO);
        Integer groupId=chatMapper.getGroupIdBySessionId(sessionId);
        for(Integer i:user_id){
            chatMapper.addSession(i,groupId,sessionId,groupName,0);
        }
        chatMapper.addGroupUser(groupId,user_id);
        return Result.success();
    }

    @Override
    public Result getSession(String sessionId, Integer userId, LocalDateTime time) {
        if(time==null){
            time=LocalDateTime.now().minusMonths(1);
        }
        Boolean isNeed=false;
        String key = MESSAGE_KEY_PREFIX + sessionId + ":messages";
        LocalDateTime oldestTime=null;
        try {

            String oldestMessageJson = (String) redisTemplate.opsForList().index(key, 0);

            if (oldestMessageJson == null) {
                isNeed=true;
            }
            Message message= JSON.parseObject(oldestMessageJson,Message.class);
            oldestTime=message.getTime();
            if(oldestTime.isAfter(time)){
                isNeed=true;
            }
        } catch (Exception e) {
            throw new RuntimeException("获取最老消息失败", e);
        }
        List<MessageVO> messageVOList = new ArrayList<>();
        if(isNeed) {
             messageVOList = chatMapper.getMessage(sessionId, time);
        }else{
            try {
                // 获取列表长度
                Long size = redisTemplate.opsForList().size(key);
                // 获取索引 0 到 size-1 的所有消息（即全部消息）
                List<String> messageJsons = redisTemplate.opsForList().range(key, 0, -1);
                for (String json : messageJsons) {
                    Message message = JSON.parseObject(json,Message.class);
                    messageVOList.add(MessageUtils.changeMessageToVO(message));
                }
            } catch (Exception e) {
                throw new RuntimeException("获取所有消息失败", e);
            }
        }
        String unreadCountKey="unread:count:uid:{"+userId+"}:room:{"+sessionId+"}";
        stringRedisTemplate.opsForValue().set(unreadCountKey, String.valueOf(0));
        return Result.success(messageVOList);
    }


}
