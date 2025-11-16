package com.example.wwork4.services;

import com.example.wwork4.pojo.Result;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

public interface ChatService {
    Result addContact(Integer user1Id, Integer user2Id);

    Result blockSomeone(Integer userId, Integer targetId);

    Result getSessionList(Integer userId);


    Result addGroup(Integer leaderId, String groupName, List<String> userIds,String text);

    Result getSession(String sessionId, Integer userId, LocalDateTime time);
}
