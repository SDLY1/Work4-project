package com.example.wwork4.mapper;

import com.example.wwork4.pojo.VO.FriendVO;
import com.example.wwork4.pojo.VO.UserVO;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface SocialMapper {
    @Insert("insert into follow (userid,to_userid) values (#{userid},#{touserid})")
    public void followUser(Integer userid,Integer touserid);
    @Delete("delete from follow where userid=#{userid} and to_userid=#{touserid}")
    public void deleteFollowUser(Integer userid,Integer touserid);
    @Select("select count(userid) from follow where userid= #{id}")
    public Long followCount(Integer id);
    @Select("select id,username,avatar from user where id in (select to_userid from follow where userid=#{userId}) limit #{start},#{pageSize} ")
    public List<FriendVO> followPage(Integer start, Integer pageSize, Integer userId);
    @Select("select count(userid) from follow where to_userid= #{id}")
    public Long followerCount(Integer id);
    @Select("select id,username,avatar from user where id in (select userid from follow where to_userid=#{userId}) limit #{start},#{pageSize} ")
    public List<FriendVO> followerPage(Integer start, Integer pageSize, Integer userId);
    @Select("select count(a.userid)  from follow as a inner join follow as b on a.to_userid=#{id} and b.to_userid=a.userid and a.to_userid =b.userid;")
    public Long friendCount(Integer id);
    @Select("select id,username,avatar from user as d inner join (select  a.userid as uid from follow as a inner join follow as b on a.to_userid=#{userId} and b.to_userid=a.userid and a.to_userid =b.userid) as e on d.id=(e.uid) limit #{start},#{pageSize} ")
    public List<FriendVO> friendsPage(Integer start, Integer pageSize, Integer userId);
}
