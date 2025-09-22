package com.example.wwork4.services.impl;



import com.example.wwork4.pojo.DTO.RegisterDTO;
import com.example.wwork4.pojo.Result;
import com.example.wwork4.pojo.DO.UserDO;
import com.example.wwork4.mapper.UserMapper;
import com.example.wwork4.pojo.VO.LoginVO;
import com.example.wwork4.pojo.VO.UserVO;
import com.example.wwork4.services.UserService;
import com.example.wwork4.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public void register(RegisterDTO registerDTO) {
            // 实现用户注册逻辑，例如保存用户信息到数据库
            UserDO user=new UserDO();
            user.setUsername(registerDTO.getUsername());
            user.setPassword(registerDTO.getPassword());
            //设置默认头像路径
            user.setAvatar("xxxxxxx");
            user.setCreateTime(LocalDateTime.now());
            user.setUpdateTime(LocalDateTime.now());
            userMapper.register(user);
    }
    @Override
    public Result login(RegisterDTO registerDTO) {
        // 实现用户登录逻辑，例如验证用户名密码
        UserDO u= userMapper.getByUsernameAndPassword(registerDTO);
        //登录成功，生成令牌，下发令牌。
        if(u!=null){
            Map<String,Object> claims =new HashMap<>();
            claims.put("id",u.getId());
            claims.put("username",u.getUsername());
            String jwt = JwtUtils.generateJwt(claims);
            // 创建LoginVO
            LoginVO loginVO=new LoginVO(u.getId().toString(),u.getUsername(),u.getAvatar(),u.getCreateTime(),u.getUpdateTime(),u.getDeletedTime(),jwt);

            return  Result.success(loginVO);
        }
            return Result.error("登录错误");
    }
    @Override
    public Result getUserInfo(Integer id) {
        // 实现获取用户信息逻辑，例如从数据库查询用户信息
        UserVO user =userMapper.getUser(id);
        if(user == null){
            return  Result.error("没有此用户");
        }
        return   Result.success(user);
    }
    @Override
    public Result updateAvatar(MultipartFile file,Integer id) throws IOException {
        // 实现上传头像逻辑，例如保存文件到服务器
        String originalFilename= file.getOriginalFilename();
        int index =originalFilename.lastIndexOf(".");
        String extname=originalFilename.substring(index);
        //判断文件格式是否正确
        if(extname.equals(".jpg")){
            //正确则保存在本地
            //(需要权限测试采用默认url)
            String url = "xxxxxxxxx";
//            file.transferTo(new File("D:\\image\\"+extname));
//            String url= "D:\\image\\"+extname;
            //将视频地址上传至数据库
            userMapper.updateAvatar(url,id);
            return Result.success(userMapper.getUser(id));
        }else{
            return Result.error("文件类型错误");
        }

    }

}
