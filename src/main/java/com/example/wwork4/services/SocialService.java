package com.example.wwork4.services;
import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.pojo.DO.SocialDO;

public interface SocialService {
    /**
     * 关注操作服务
     * @param userId 用户ID
     * @param followUserId 被关注用户ID
     * @return 关注结果
     */
    Result saveFollowUser(SocialDO social);

    /**
     * 关注列表服务
     * @param userId 用户ID
     * @return 关注用户列表
     */
    PageBean getFollowList(String user_id, Integer page_num, Integer page_size);

    /**
     * 粉丝列表服务
     * @param userId 用户ID
     * @return 粉丝用户列表
     */
    PageBean getFollowerList(String user_id, Integer page_num, Integer page_size);

    PageBean getFriendList(Integer userId, Integer pageNum, Integer pageSize);


}
