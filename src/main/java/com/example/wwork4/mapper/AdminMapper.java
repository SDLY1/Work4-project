package com.example.wwork4.mapper;

import com.example.wwork4.pojo.DO.VideoDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AdminMapper {
    @Select("select id,userId,videoUrl,coverUrl,title,description,visitCount,likeCount,commentCount,createdAt,updatedAt,deletedAt from video where state=0 limit #{start},#{pageSize} ")
    public List<VideoDO>getVideoList(Integer start, Integer pageSize);
    @Select("select count(id) from video where state=0")
    public Long videoCount();
    @Delete("delete from video where id=#{videoId}")
    public boolean deleteVideo(String videoId);
    @Update("update video set state=1 where id=#{videoId}")
    void confirmVideo(String videoId);
}
