package com.example.wwork4.mapper;

import com.example.wwork4.pojo.DO.CommentDO;
import com.example.wwork4.pojo.DO.VideoDO;
import org.apache.ibatis.annotations.*;

import java.time.LocalDateTime;
import java.util.List;

@Mapper
public interface InteractionMapper {
    @Insert("insert into comment(id,userid,videoid,parentid,content,createdat,updatedat,childcount,likecount)values (#{id},#{user_id},#{video_id},#{parent_id},#{content},#{creatTime},#{updateTime},0,0)")
    public void commentVideo(String id,String video_id  , String content, String user_id, String parent_id, LocalDateTime creatTime,LocalDateTime updateTime);
    @Update("update comment set childcomment=childcomment +1 where id=#{id}")
    public void childComment(String id);
    @Select("select count(videoid) from comment where videoid= #{id}")
    public Long Commentcount(String id);
    @Select("select id,uesrid,videoid,parentid,likecount,childcount,content,createdAt,updatedAt,deletedAt from comment where videoid=#{videoId} limit #{start},#{page_size}")
    public List<CommentDO> getCommentList(String videoId, Integer start , Integer page_size);
    @Delete("delete from comment where videoid=#{video_id} and id=#{comment_id}")
    public  void deleteComment(String video_id,String comment_id);
    @Select("select parentid from comment where videoid=#{video_id} and id=#{comment_id}")
    public  Integer findParentId(String video_id,String comment_id);
    @Update("update comment set childCount=childCount-1 where id=#{parent_id}")
    public void decrease(String parent_id);
    @Update("update video set likecount=likecount+1 where id=#{video_id}")
    public  boolean like(String videoid);
    @Update("update video set likecount=likecount-1 where id=#{video_id}")
    public  boolean deleteLike(String videoid);
    @Select("select count(likecount)from video where id=#{videoid}")
    public Integer getLikeCount(String videoid);
    @Update("update video set visitCount=#{clickCount} where id=#{videoid}")
    public  void  updateVideoClickCount(int clickCount,String videoid);

    public List<VideoDO> selectByIds(@Param("videoIds")List<String> videoIds, @Param("start") Integer start, @Param("page_size") Integer page_size);
}
