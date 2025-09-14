package com.example.wwork4.controller;

import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.pojo.DO.SocialDO;
import com.example.wwork4.services.SocialService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("")
public class SocialController {

    @Autowired
    private SocialService socialService;

    /**
     * 关注操作接口
     * @param userId 用户ID
     * @param followUserId 被关注用户ID
     * @return 关注结果
     */
    @PostMapping("/relation/action")
    public Result saveFollowUser(@ModelAttribute SocialDO social ){
        return socialService.saveFollowUser(social);
    }

    /**
     * 关注列表接口
     * @param userId 用户ID
     * @return 关注用户列表
     */
    @GetMapping("/follow/list")
    public Result getFollowList(String user_id, Integer page_num, Integer page_size) {
        log.info("查询关注列表");
        PageBean pageBean=socialService.getFollowList(user_id,page_num,page_size);
        return Result.success(pageBean);
    }
    /**
     * 粉丝列表接口
     * @param userId 用户ID
     * @return 粉丝用户列表
     */
    @GetMapping("/follower/list")
    public Result getFollowerList(String user_id, Integer page_num, Integer page_size) {
        log.info("查询粉丝列表");
        PageBean pageBean=socialService.getFollowerList(user_id,page_num,page_size);
        return Result.success(pageBean);
    }
    @GetMapping("/friends/list")
    public Result getFriendList(Integer user_id, Integer page_num, Integer page_size) {
        log.info("查询朋友列表");
        PageBean pageBean=socialService.getFriendList(user_id,page_num,page_size);
        return Result.success(pageBean);
    }
}