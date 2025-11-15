package com.example.wwork4.mapper;

import com.example.wwork4.pojo.DO.UserDO;
import com.example.wwork4.pojo.DTO.RegisterDTO;
import com.example.wwork4.pojo.VO.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;
import java.util.Date;

@Mapper
public interface UserMapper {


//    public List<User> list();

    @Insert("insert into user(username,password,avatar,created,updated,deleted,role)values (#{username},#{password},#{avatar},#{createTime},#{updateTime},#{deletedTime},#{role})")
    public void register(UserDO user);


    @Select("select id,username,avatar,created createTime,updated updateTime,deleted deletedTime from user where id=#{id}")
    public UserVO getUser(Integer id);
    @Update("update user set avatar = (#{file}) where id =#{id}")
    public void updateAvatar(String file,Integer id);
    @Select("select id,username,password,avatar,created createTime,updated updateTime,deleted deletedTime,role from user where username= #{username} ")
    UserDO getByUsername(RegisterDTO registerDTO);
    @Select("select id,username,password,avatar,created createTime,updated updateTime,deleted deletedTime,role from user where username=#{username}")
    UserDO getUserByUsername(String username);
    @Update("update user set updated =#{time} where id=#{id}")
    void updateTime(Integer id, LocalDateTime time);
    @Select("select username from user where id=#{id}")
    String getUsernameByUserId(Integer id);
}
