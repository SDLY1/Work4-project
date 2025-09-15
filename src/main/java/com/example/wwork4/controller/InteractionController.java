package com.example.wwork4.controller;

import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.services.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("")
public class InteractionController {

    @Autowired
    private InteractionService interactionService;

    /**
     * 点赞操作接口
     * @param video_id 视频ID
     * @param userId 用户ID
     * @param action_type 操作类型
     * @return 点赞结果
     */
    @PostMapping("/like/action")
    public Result likeVideo(@RequestParam String video_id, @RequestParam String userId,@RequestParam String action_type) {
        return interactionService.likeVideo(video_id, userId,action_type);
    }
    /**
     * 点赞列表接口
     *@param user_id 用户ID
     *@param page_num 分页数量
     * @param page_size 分页大小
     */
    @GetMapping ("/like/list")
    public Result getLikeList(@RequestParam Integer user_id,@RequestParam Integer page_num, @RequestParam Integer page_size) {

        PageBean likeList= interactionService.getLikeList(user_id,page_num,page_size);
        return Result.success(likeList);
    }
    /**
     * 评论接口
     * @param video_id 视频ID
     * @param user_id 用户ID
     * @param content 评论内容
     * @param parent_id 父评论ID
     * @return 评论结果
     */
    @PostMapping("/comment/publish")
    public Result commentVideo(String video_id , String content,String user_id,String parent_id) {
        return interactionService.commentVideo(video_id, content,user_id,parent_id);
    }
    /**
     * 评论列表接口
     * @param video_id 视频ID
     * @param page_num 分页数量
     * @param page_size 分页大小
     * @return 评论列表
     */
    @GetMapping("/comment/list")
    public Result getCommentList(String video_id, Integer page_num, Integer page_size) {

         PageBean commentList= interactionService.getCommentList(video_id,page_num,page_size);

        return Result.success(commentList);
    }
    // InteractionController.java（续）
    /**
     * 删除评论接口
     * @param comment_id 评论ID
     * @param video_id 用户ID
     * @return 删除结果
     */
    @DeleteMapping("/comment/delete")
    public Result deleteComment(String video_id,String comment_id) {
        return interactionService.deleteComment(video_id,comment_id);
    }

    /**
     * 视频点击
     * @param video_id 用户ID
     * @return 点击结果
     */
    @PostMapping("/click")
    public Result clickVideo(String video_id) {

        return interactionService.clickVideo(video_id);
    }

}
