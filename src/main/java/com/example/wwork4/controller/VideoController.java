package com.example.wwork4.controller;

import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.services.VideoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/video")
public class VideoController {

    @Autowired
    private VideoService videoService;

    /**
     * 视频投稿接口
     */
    @PostMapping("/publish")
    public Result submitVideo(MultipartFile video, String title , String description,Integer user_id) {

        return videoService.submitVideo(video,title,description,user_id);
    }
    /**
     * 视频发布列表接口
     */
    @GetMapping ("/list")
    public Result getVideoList( Integer user_id,Integer page_num,Integer page_size) {
        log.info("查询发布列表");
        PageBean pageBean=videoService.getVideoList(user_id,page_num,page_size);
        return Result.success(pageBean);
    }
    /**
     * 搜索视频接口
     */
    @PostMapping("/search")
    public Result searchVideo(@RequestParam String keywords,@RequestParam Integer page_num,@RequestParam Integer page_size, @RequestParam(required = false) String from_date,@RequestParam(required = false) String to_date,@RequestParam(required = false) Integer user_id) {
        PageBean pageBean= videoService.searchVideo(keywords,page_num,page_size,from_date,to_date,user_id);
        return Result.success(pageBean);
    }
    /**
     * 点击量排行榜接口
     */
    @GetMapping("/popular")
    public Result getVideoRank(@RequestParam Integer page_num,@RequestParam Integer page_size) {
        PageBean pageBean=videoService.getVideoRank(page_num,page_size);
        return Result.success(pageBean);
    }
    @GetMapping("/order")
    public Result listVideoOrderByCreatedTime (Boolean order){
        return videoService.listVideoOrderByCreatedTime(order);
    }
    @GetMapping("/history")
    public Result listVideoHistory(){
        return videoService.listVideoHistory();
    }
}
