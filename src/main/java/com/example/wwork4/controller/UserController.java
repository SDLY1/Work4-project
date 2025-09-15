package com.example.wwork4.controller;

import com.example.wwork4.pojo.DTO.RegisterDTO;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.pojo.DO.UserDO;
import com.example.wwork4.services.UserService;
import com.example.wwork4.utils.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 用户注册接口
     */
    @PostMapping("/register")
    public Result register( RegisterDTO registerDTO) {
        userService.register(registerDTO);
        return  Result.success();
    }
    /**
     * 用户登录接口
     */
    @PostMapping("/login")
    public Result login( RegisterDTO registerDTO){
        log.info("用户登入:{}",registerDTO);
        //登录失败，返回错误信息。
        return userService.login(registerDTO);
    }

    /**
     * 获取用户信息接口
     */
    @GetMapping("/info")
    public Result getUserInfo(@RequestParam Integer user_id) {
        log.info("获取 "+user_id+" 用户信息");
        return userService.getUserInfo(user_id);
    }
    /**
     * 上传头像接口
     */
    @PutMapping ("/avatar/upload")
    public Result updateAvatar(MultipartFile image,Integer id)throws Exception {
        log.info(id+"用户更改头像");
        return userService.updateAvatar(image,id);
    }

}
