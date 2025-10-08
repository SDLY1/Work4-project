package com.example.wwork4.services.impl;

import com.example.wwork4.mapper.UserMapper;
import com.example.wwork4.pojo.DO.UserDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
     UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /**
         * 1/通过userName 获取到userInfo信息
         * 2/通过User（UserDetails）返回details。
         */
        //通过userName获取用户信息
        UserDO user = userMapper.getUserByUsername(username);
        if(user == null) {
            throw new UsernameNotFoundException("not found");
        }


        return User.withUsername(username).password(user.getPassword()).roles(user.getRole()).build();
    }
}
