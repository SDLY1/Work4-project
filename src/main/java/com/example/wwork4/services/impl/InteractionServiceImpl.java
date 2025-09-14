package com.example.wwork4.services.impl;

import com.example.wwork4.mapper.InteractionMapper;
import com.example.wwork4.pojo.DO.CommentDO;
import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.pojo.DO.VideoDO;
import com.example.wwork4.services.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class InteractionServiceImpl implements InteractionService {
    @Autowired
    private InteractionMapper interactionMapper;
    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Override
    public Result likeVideo(String videoId, String userId,String type) {
        // 实现点赞操作逻辑，例如更新视频点赞数
        String key="video:liked:"+videoId;

        Boolean isMember=stringRedisTemplate.opsForSet().isMember(key,userId);
        if(type.equals("1")){
            //判断当前用户是否已经点赞
            if(!isMember) {
               Boolean isSuccess=interactionMapper.like(videoId);
//               Integer likeCount=interactionMapper.getLikeCount(videoId);
               //数据库操作成功，保存用户到Redis中set集合
                if(isSuccess){
                    stringRedisTemplate.opsForSet().add(key,userId);
                    stringRedisTemplate.opsForSet().add(userId,videoId);
//                    stringRedisTemplate.opsForZSet().incrementScore("videoLikeCount",videoId,1);
                }
            }else{
                return Result.error("用户已点赞");
            }
        }else if(type.equals("2")){
            if(!isMember){
                return Result.error("用户未点赞");
            }
            Boolean isSuccess=interactionMapper.deleteLike(videoId);
//            Integer likeCount=interactionMapper.getLikeCount(videoId);
            if(isSuccess){
                stringRedisTemplate.opsForSet().remove(key,userId);
                stringRedisTemplate.opsForSet().add(userId,videoId);
//                stringRedisTemplate.opsForZSet().incrementScore("videoLikeCount",videoId,-1);
            }
        }else{
            return Result.error("判断参数错误");
        }
        return Result.success();
    }
    @Override
    public PageBean getLikeList(Integer userId,Integer page_num, Integer page_size) {
        // 实现获取点赞列表逻辑，例如从数据库查询点赞用户信息
        Integer start =(page_num-1)*page_size;
        String user_id= userId.toString();
        Set<String>likeList=stringRedisTemplate.opsForSet().members(user_id);
        List<String> video_id=likeList.stream().toList();
        Long count= (long) video_id.size();
        List<VideoDO> videoList=interactionMapper.selectByIds(video_id,start,page_size);
        return new PageBean(count,videoList);
    }

//    @Override
//    public Result islike

    @Override
    public Result commentVideo(String video_id , String content,String user_id,String parent_id) {
        // 实现评论操作逻辑，例如保存评论信息到数据库
        String comment_id= UUID.randomUUID().toString();
        interactionMapper.commentVideo(comment_id,video_id,content,user_id,parent_id, LocalDateTime.now(),LocalDateTime.now());
        if(!parent_id.isEmpty()){
            interactionMapper.childComment(parent_id);
        }
        return Result.success();
    }

    @Override
    public PageBean getCommentList(String videoId, Integer page_num, Integer page_size) {
        // 实现获取评论列表逻辑，例如从数据库查询评论信息
//        Integer ID=Integer.valueOf(videoId);
        Long count=interactionMapper.Commentcount(videoId);
        Integer start =(page_num-1)*page_size;
        List <CommentDO> commentList=interactionMapper.getCommentList( videoId,  start,  page_size);
        return new PageBean(count,commentList);
    }

    // InteractionServiceImpl.java（续）
    @Override
    public Result deleteComment(String video_id,String comment_id) {
        // 实现删除评论逻辑，例如从数据库删除评论信息
        interactionMapper.deleteComment (video_id, comment_id);
        String parent_id= String.valueOf(interactionMapper.findParentId(video_id,comment_id));
        if(parent_id.isEmpty()){
//            Integer id=Integer.valueOf(parent_id);
            interactionMapper.decrease(parent_id);
        }
        return Result.success();
    }

    @Override
    public Result clickVideo(String video_id) {
        String key="video";
        stringRedisTemplate.opsForZSet().incrementScore(key,video_id,1);
        return Result.success();
    }

}
