package com.example.wwork4.mapper;

import com.example.wwork4.pojo.DO.GroupDO;
import com.example.wwork4.pojo.DO.MessageDO;
import com.example.wwork4.pojo.VO.MessageVO;
import com.example.wwork4.pojo.VO.SessionVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface ChatMapper {
    @Select("select count(1) from chat_session_user where session_id=#{sessionId}")
    Integer isExist(String sessionId);
    @Insert("insert into chat_session_user (user_id,contact_id,session_id,contact_name,state)values (#{userId},#{contactId},#{sessionId},#{contactName},#{state})")
    void addSession(Integer userId,Integer contactId,String sessionId ,String contactName ,Integer state);
    @Update("update chat_session_user set state=1 where contact_id=#{targetId} and user_id=#{userId}")
    Boolean blockSomeone(Integer userId,Integer targetId);
    @Select("select session_id sessionId,contact_id contactId,contact_name contactName from chat_session_user where user_id=#{userId} and state = 0 ")
    public List<SessionVO> getSessionList(Integer userId);
    @Insert("insert into group_session(group_name,leader_id,count,text,session_id)value (#{groupName},#{leaderId},#{count},#{text},#{sessionId})")
    void addGroup(GroupDO groupDO);
    @Select("select id from group_session where session_id=#{sessionId}")
    Integer getGroupIdBySessionId(String sessionId);

    void addGroupUser(Integer groupId, List<Integer> userIds);
    @Select("select group_id from group_user where user_id=#{userId}")
    List<Integer> selectGroupIdByUserId(Integer userId);
    @Select("select session_id sessionId, contactName from chat_session_user where user_id=#{userId} ")
    public List<String> getSessionIdList(Integer userId);
    @Insert("insert into chat_message (session_id, message_type, message_content, send_user_id,send_user_name, send_time, contact_id, contact_type, status) value(#{sessionId},#{messageType},#{content},#{senderId},#{senderName},#{time},#{receiverId},#{contactType},#{status})")
    void addMessage(MessageDO messageDO);
    @Select("select session_id as sessionId, message_type messageType, message_content content, send_user_id senderId,send_user_name senderName, send_time time, contact_id receiverId, contact_type contactType, status from chat_message where session_id=#{sessionId} and send_time>#{time}")
    List<MessageVO> getMessage(String sessionId, LocalDateTime time);
    @Select("select  user_id from group_user where group_id=#{receiverId}")
    List<Integer> getUserIdByGroupId(Integer receiverId);
}
