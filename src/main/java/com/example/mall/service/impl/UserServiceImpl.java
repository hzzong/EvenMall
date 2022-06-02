package com.example.mall.service.impl;

import com.example.mall.common.ServiceResultEnum;
import com.example.mall.domain.User;
import com.example.mall.domain.UserToken;
import com.example.mall.domain.repo.UserRepo;
import com.example.mall.domain.repo. UserTokenRepo;
import com.example.mall.service.UserService;
import com.example.mall.util.NumberUtil;
import com.example.mall.util.SystemUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class UserServiceImpl implements UserService {

    private UserRepo userRepo;

    private UserTokenRepo userTokenRepo;

    private SystemUtil systemUtil;

    private NumberUtil numberUtil;

    @Autowired
    public UserServiceImpl(UserRepo userRepo, UserTokenRepo userTokenRepo, SystemUtil systemUtil, NumberUtil numberUtil) {
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
        this.systemUtil = systemUtil;
        this.numberUtil = numberUtil;
    }




    @Override
    public String register(String loginName, String password){
        if (userRepo .findUserByLoginName(loginName) != null) {
            return ServiceResultEnum.SAME_LOGIN_NAME_EXIST.getResult();
        }
        User user = new User();
        user.setLoginName(loginName );
        user.setNickName(loginName);
        user.setIntroduceSign("Welcome!");
        user.setPassword(password);
        if (userRepo.save(user) != null  ) {
            return ServiceResultEnum.SUCCESS.getResult();
        }
        return ServiceResultEnum.DB_ERROR.getResult();
    }

    @Override
    public String login(String loginName, String password) {
        User user = userRepo.findUserByLoginNameAndPassword(loginName, password);
        if(user == null){
            return ServiceResultEnum.LOGIN_ERROR.getResult();
         }
        String token = getNewToken(System.currentTimeMillis() + "", user.getId());
         UserToken userToken = userTokenRepo.findUserTokenByUserId(user.getId());
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + 2 * 24 * 3600 * 1000);//过期时间 48 小时
        if (userToken == null) {
            userToken = new UserToken();
            userToken.setUserId(user.getId());
            //剩下不需要在这做了
        }
        userToken.setToken(token);
        userToken.setUpdateTime(now);
        userToken.setExpireTime(expireTime);
        //更新
        userTokenRepo.save(userToken);
        return token;
    }

    private String getNewToken(String timeStr, Long userId) {
        String src = timeStr + userId + numberUtil.genRandomNum(4);
        return  systemUtil.genToken(src);
    }

    @Override
    public boolean logout(Long id){
        try {
            userTokenRepo.deleteById(id);
            return Boolean.TRUE;
        } catch (Exception e){
            return Boolean.FALSE;
        }
    }
}
