package com.example.wwork4.controller;

import com.example.wwork4.pojo.Result;
import com.example.wwork4.services.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;
    @GetMapping("/list")
    public Result getVideoList(Integer pageSize, Integer pageNum){
        return adminService.getVideoList(pageSize,pageNum);
    }
    @DeleteMapping("/delete")
    public Result deleteVideo(String videoId){
        return  adminService.deleteVideo(videoId);
    }
    @PutMapping("/confirm")
    public Result ConfirmVideo(String videoId){
        return adminService.confirmVideo(videoId);
    }
}
