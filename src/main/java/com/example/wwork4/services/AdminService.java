package com.example.wwork4.services;


import com.example.wwork4.pojo.Result;

public interface AdminService {

    Result getVideoList(Integer pageSize, Integer pageNum);
    Result deleteVideo(String videoId);

    Result confirmVideo(String videoId);
}
