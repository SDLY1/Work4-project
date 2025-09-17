package com.example.wwork4.mapper;

import com.example.wwork4.pojo.DO.UserDO;
import com.example.wwork4.pojo.DTO.RegisterDTO;
import com.example.wwork4.pojo.VO.UserVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserMapper {


//    public List<User> list();

    @Insert("insert into user(username,password,avatar,created,updated,deleted)values (#{username},#{password},#{avatar},#{createTime},#{updateTime},#{deletedTime})")
    public void register(UserDO user);

    public boolean login(UserDO user);
    @Select("select id,username,avatar,created createTime,updated updateTime,deleted deletedTime from user where id=#{id}")
    public UserVO getUser(Integer id);
    @Update("update user set avatar = (#{file}) where id =#{id}")
    public void updateAvatar(String file,Integer id);
    @Select("select id,username,password,avatar,created createTime,updated updateTime,deleted deletedTime from user where username= #{username} and password=#{password}")
    UserDO getByUsernameAndPassword(RegisterDTO registerDTO);
}
