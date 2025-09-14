package com.example.wwork4.mapper;

import com.example.wwork4.pojo.DO.VideoDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface VideoMapper {
    @Insert("insert into video (id,userId,videourl,title,description,createdAt,updatedAt) values (#{videoId},#{id},#{video},#{title},#{description},#{creatTime},#{updateTime})" )
    public void submitVideo(String video, String title, String description, LocalDateTime creatTime,LocalDateTime updateTime,String videoId,Integer id);
    @Select("select count(id) from video where userId= #{id}")
    public Long count(Integer id);
    @Select("select * from video where userId=#{userId} limit #{start},#{pageSize} ")
    public List<VideoDO>page(Integer start, Integer pageSize, Integer userId);

    public List<VideoDO>searchVideo(@Param("keyword")String keyword, @Param("start")Integer start, @Param("page_size")Integer page_size, @Param("from_date")String from_date, @Param("to_date")String to_date, @Param("username")Integer username);

    public List<VideoDO>videoRank(@Param("videoIds")List<String> videoIds);

}
