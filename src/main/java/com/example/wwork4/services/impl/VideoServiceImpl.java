package com.example.wwork4.services.impl;

import com.example.wwork4.mapper.VideoMapper;
import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.pojo.DO.VideoDO;
import com.example.wwork4.services.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.Set;
@Service
public class VideoServiceImpl implements VideoService {
    @Autowired
    private VideoMapper videoMapper;
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result submitVideo(MultipartFile video, String title , String description,Integer id) {
        // 实现视频投稿逻辑，例如保存视频信息到数据库
        String orginalFilename= video.getOriginalFilename();
        int index =orginalFilename.lastIndexOf(".");
        String extname=orginalFilename.substring(index);
        //使用UUID生成视频ID
        String videoId= UUID.randomUUID().toString();

        if(extname.equals(".mp4")){
            videoMapper.submitVideo(extname,title,description, LocalDateTime.now(),LocalDateTime.now(),videoId,id);
        }else{
            return Result.error("视频格式错误");
        }
        return Result.success();
    }



    @Override
    public PageBean getVideoList(Integer user_id,Integer page_num,Integer page_size) {
        // 实现获取视频列表逻辑，例如从数据库查询视频信息
        //1.获取总记录数

        Long count=videoMapper.count(user_id);
        //2.获取分页查询结果列表
        Integer start = page_num*page_size;
        List<VideoDO> videoList=videoMapper.page(start,page_size,user_id);
        //3.封装
        return new PageBean(count,videoList);
    }

    @Override
    public PageBean searchVideo(String keyword, Integer page_num, Integer page_size, String from_date, String to_date, Integer user_id) {
        //从数据库中查找数据
        Integer start = page_num*page_size;
        //将空字符串转成null.
        if(from_date.isEmpty()){
            from_date=null;
        }
        if(to_date.isEmpty()){
            to_date=null;
        }
        List<VideoDO> videoList=videoMapper.searchVideo(keyword,start,page_size,from_date,to_date,user_id);
        Long count = (long) videoList.size();
        if(keyword!=null){
            String key= "video:Search:History: ";
            stringRedisTemplate.opsForList().leftPush(key,keyword);
            //只保留最近10条记录
            stringRedisTemplate.opsForList().trim(key,0,9);
        }
        return new PageBean(count,videoList);
    }


    @Override
    public PageBean getVideoRank(Integer page_num,Integer page_size) {
        // 实现获取点击量排行榜逻辑，例如从Redis获取排行榜信息
        //假设点击操作会使用Redis中zset进行点击量的更新。
        Long start=  ((long) page_num *page_size);
        Set<String> videorank=stringRedisTemplate.opsForZSet().range("video",start,page_size);
        List<String> videolist=videorank.stream().toList();
        Long count= (long) videolist.size();
        List<VideoDO> videoedRank=videoMapper.videoRank(videolist);
        return new PageBean(count,videoedRank);
    }

    @Override
    public Result listVideoOrderByCreatedTime( Boolean order) {
        String pos_order;
        if(order){
            pos_order="asc";
        }else{
            pos_order="desc";
        }
        return Result.success(videoMapper.VideoOrderByCreatedTime(pos_order));
    }

    @Override
    public Result listVideoHistory() {
        String key= "video:Search:History: ";
        List<String> list=stringRedisTemplate.opsForList().range(key,0,9);
        return Result.success(list);
    }

}
