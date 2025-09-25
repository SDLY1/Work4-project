package com.example.wwork4.services.impl;

import com.example.wwork4.mapper.SocialMapper;
import com.example.wwork4.pojo.PageBean;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.pojo.DO.SocialDO;
import com.example.wwork4.pojo.VO.FriendVO;
import com.example.wwork4.pojo.VO.UserVO;
import com.example.wwork4.services.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class SocialServiceImpl implements SocialService {
    @Autowired
    private SocialMapper socialMapper;
    private UserVO userVO;

    @Transactional
    @Override
    public Result saveFollowUser(SocialDO social) {
        // 实现关注操作逻辑，例如更新用户关注关系
        if(social.getAction_type()==0){
            socialMapper.followUser(social.getUser_id(),social.getTo_user_id());
            return Result.success();
        }else if(social.getAction_type()==1){
            socialMapper.deleteFollowUser(social.getUser_id(),social.getTo_user_id());
            return Result.success();
        }
        return Result.error("操作类型有误");
    }

    @Override
    public PageBean getFollowList(String user_id, Integer page_num, Integer page_size) {
        // 实现获取关注列表逻辑，例如从数据库查询关注用户信息
        Integer ID=Integer.valueOf(user_id);
        Long count=socialMapper.followCount(ID);
        Integer start = (page_num)*page_size;
        List<FriendVO> friendVOS =socialMapper.followerPage(start,page_size,ID);
        return new PageBean(count, friendVOS);
    }

    @Override
    public PageBean getFollowerList(String user_id, Integer page_num, Integer page_size) {
        // 实现获取粉丝列表逻辑，例如从数据库查询粉丝用户信息
        Integer ID=Integer.valueOf(user_id);
        Long count=socialMapper.followerCount(ID);
        Integer start = (page_num)*page_size;
        List<FriendVO> friendVOS =socialMapper.followPage(start,page_size,ID);
        return new PageBean(count, friendVOS);
    }

    @Override
    public PageBean getFriendList(Integer userId, Integer pageNum, Integer pageSize) {

        Long count=socialMapper.friendCount(userId);
        Integer start = pageNum*pageSize;
        List<FriendVO> friendVOS =socialMapper.friendsPage(start,pageSize,userId);
        return new PageBean(count, friendVOS);
    }
}
