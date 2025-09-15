package com.example.wwork4.services;

import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;
import org.springframework.web.multipart.MultipartFile;

public interface VideoService {
    /**
     * 视频投稿服务
     * @param video 视频信息
     * @return 投稿结果
     */
    Result submitVideo(MultipartFile video, String title , String description,Integer id);
    /**
     * 视频发布列表服务
     * @return 视频列表
     */
    PageBean getVideoList(Integer user_id, Integer page_num, Integer page_size);
    /**
     * 搜索视频服务
     * @param keyword 搜索关键词
     * @return 搜索结果
     */
    PageBean searchVideo(String keyword,Integer page_num,Integer page_size,String from_date,String to_date,Integer user_id);
    /**
     * 点击量排行榜服务
     * @return 排行榜视频列表
     */
    PageBean getVideoRank(Integer page_num,Integer page_size);

    Result listVideoOrderByCreatedTime(  Boolean order);

    Result listVideoHistory();
}
