package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    //微信服务地址
    private final static String WX_LOGIN = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private WeChatProperties weChatProperties;

    @Autowired
    private UserMapper userMapper;

    /**
     * 微信登录
     *
     * @param userLoginDTO
     * @return
     */
    @Override
    public User wxLogin(UserLoginDTO userLoginDTO) {
        //判断openid是否为空，如果为空表示登录失败，抛出业务异常
        String openid = getOpenid(userLoginDTO.getCode());

        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        //判断当前用户是否为新用户
        User user = userMapper.select(openid);

        if (user == null) {
            //如果是新用户，自动完成注册
            user = User.builder().
                    openid(openid).
                    createTime(LocalDateTime.now())
                    .build();

            userMapper.insert(user);
        }

        //返回这个用户对象
        return user;
    }

    private String getOpenid(String code) {
        HashMap<String, String> hm = new HashMap<>();
        hm.put("appid", weChatProperties.getAppid());
        hm.put("secret", weChatProperties.getSecret());
        hm.put("grant_type", "authorization_code");
        hm.put("js_code", code);

        String json = HttpClientUtil.doGet(WX_LOGIN, hm);
        JSONObject jsonObject = JSON.parseObject(json);
        String openid = jsonObject.getString("openid");

        return openid;
    }
}
