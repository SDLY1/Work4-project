package com.example.wwork4.services;
import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;

import java.util.List;
public interface InteractionService {
    /**
     * 点赞操作服务
     * @param videoId 视频ID
     * @param userId 用户ID
     * @return 点赞结果
     */
     Result likeVideo(String videoId, String userId,String type);
    /**
     * 点赞列表服务
     * @param videoId 视频ID
     * @return 点赞用户列表
     */
    PageBean getLikeList(Integer userid, Integer page_num, Integer page_size);
    /**
     * 评论服务
     * @param videoId 视频ID
     * @param userId 用户ID
     * @param content 评论内容
     * @return 评论结果
     */
    Result commentVideo(String video_id  ,String content,String user_id,String parent_id);

    /**
     * 评论列表服务
     * @param videoId 视频ID
     * @return 评论列表
     */
    PageBean getCommentList(String videoId, Integer page_num, Integer page_size);

    /**
     * 删除评论服务
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 删除结果
     */
    Result deleteComment(String video_id,String comment_id);

    Result clickVideo(String video_id);
}
