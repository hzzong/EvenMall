package com.example.mall.web;

import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.config.annotation.TokenToUser;
import com.example.mall.domain.User;
import com.example.mall.service.UserService;
import com.example.mall.util.PhoneNumberUtil;
import com.example.mall.web.param.UserLoginParam;
import com.example.mall.web.param.UserRegisterParam;
import com.example.mall.web.vo.Result;
import com.example.mall.web.vo.ResultGenerator;
import com.example.mall.web.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class UserController {

    private PhoneNumberUtil phoneNumberUtil;
    private ResultGenerator resultGenerator;
    private UserService userService;
 
    @Autowired
    public UserController(PhoneNumberUtil phoneNumberUtil, ResultGenerator resultGenerator, UserService userService) {
        this.phoneNumberUtil = phoneNumberUtil;
        this.resultGenerator = resultGenerator;
        this.userService = userService;
    }

    @PostMapping("/user/register")
    public Result register(@RequestBody  @Valid UserRegisterParam param){
        if (phoneNumberUtil.isNotPhoneNumber(param.getLoginName())){
             return resultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }

        String registerResult = userService.register(param.getLoginName(), param.getPassword());
        log.info("login api,loginName={},loginResult={}", param .getLoginName(), registerResult);
        if ( ServiceResultEnum.SUCCESS.getResult().equals(registerResult)){ // Enum has to be existed, so put it before the result
            return resultGenerator.genSuccessResult();
        }
        return resultGenerator.genFailResult(registerResult);
    }

    @PostMapping("/user/login")
    public Result<String> login(@RequestBody @Valid UserLoginParam param) {
        if ( phoneNumberUtil.isNotPhoneNumber(param.getLoginName())){
            return resultGenerator.genFailResult(ServiceResultEnum.LOGIN_NAME_IS_NOT_PHONE.getResult());
        }
        String loginResult = userService.login(param.getLoginName(), param.getPassword());

        log.info("login api,loginName={},loginResult={}", param.getLoginName(), loginResult);

        //登录成功
        if (!StringUtils.isBlank( loginResult) && loginResult.length() == Constants.TOKEN_LENGTH) {
            Result result = resultGenerator.genSuccessResult();
            result.setData(loginResult);
            return result;
        }
        //登录失败
        return resultGenerator.genFailResult(loginResult);
    }

    @GetMapping("/user/info")
    public Result<UserVO> getUserDetail(@TokenToUser  User user) {
        //已登录则直接返回
        UserVO userVO = new UserVO ();
        BeanUtils.copyProperties(user, userVO);
        return resultGenerator.genSuccessResult(userVO);
    }

    @PostMapping("/user/logout")
    public Result logout(@TokenToUser User user){
        boolean logoutResult = userService.logout(user.getId());
        log.info("logout api,loginMallUser={}", user.getId());

        //登出成功
        if (logoutResult) {
            return resultGenerator.genSuccessResult();
        }
        //登出失败
        return resultGenerator.genFailResult("logout error");
    }

}
