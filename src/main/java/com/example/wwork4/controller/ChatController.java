package com.example.wwork4.controller;

import com.example.wwork4.pojo.Result;
import com.example.wwork4.services.ChatService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/chat")
public class ChatController {
    @Resource
    ChatService chatService;
//    @PostMapping("/send")
//    public Result sendMessage(String contactId,String messageContent,Integer messageType,Long fileSize,String fileName,Integer fileType){
//
//        return null;
//    }

    @PostMapping("/contact")
    public  Result addContact(Integer user1Id, Integer user2Id){
        return chatService.addContact(user1Id,user2Id);
    }
    @PutMapping("/block")
    public Result blockSomeone(Integer userId, Integer targetId){
        return chatService.blockSomeone(userId,targetId);
    }

    @GetMapping("/session/list")
    public Result getSessionList(Integer userId){
        return chatService.getSessionList(userId);
    }

    @PostMapping("/addGroup")
    public Result addGroup(@RequestParam("leader_id") Integer leaderId,
                           @RequestParam("group_name") String groupName,
                           @RequestParam("user_id") List<String> userIds,
                           @RequestParam("text") String text){
        return chatService.addGroup(leaderId,groupName,userIds,text);
    }
    @GetMapping("/session")
    public Result getSession(String sessionId){
        return chatService.getSession(sessionId);
    }

}
