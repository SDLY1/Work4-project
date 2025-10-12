package com.example.wwork4.services.impl;

import com.example.wwork4.mapper.AdminMapper;
import com.example.wwork4.pojo.DO.VideoDO;
import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {
    @Autowired
    private AdminMapper adminMapper;
    @Override
    public Result getVideoList(Integer pageSize, Integer pageNum) {
        Long count=adminMapper.videoCount();
        Integer start =(pageNum)*pageSize;
        List<VideoDO> videoDOList=adminMapper.getVideoList(start,pageSize);
        PageBean pageBean=new PageBean(count,videoDOList);
        return Result.success(pageBean);
    }

    @Override
    public Result deleteVideo(String videoId) {
        if(adminMapper.deleteVideo(videoId)){
            return Result.success();
        }else{
            return Result.error("删除视频失败");
        }
    }
}
