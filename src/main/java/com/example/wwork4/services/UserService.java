package com.example.wwork4.services;

import com.example.wwork4.pojo.DTO.RegisterDTO;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.pojo.DO.UserDO;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface UserService {
    /**
     * 用户注册服务
     * @param user 用户信息
     * @return 注册结果
     */
    void register(RegisterDTO registerDTO);
    /**
     * 用户登录服务
     * @param user 用户信息
     * @return 登录结果（token）
     */
    Result login(RegisterDTO registerDTO);
    /**
     * 获取用户信息服务
     * @param name 用户名
     * @return 用户信息
     */
    Result getUserInfo(Integer id);
    /**
     * 上传头像服务
     * @param name 用户名
     * @param file 头像文件
     * @return 上传结果
     */
    Result updateAvatar(MultipartFile file,Integer id) throws IOException;
}