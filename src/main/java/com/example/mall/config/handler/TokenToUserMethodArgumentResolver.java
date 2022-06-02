package com.example.mall.config.handler;

import com.example.mall.common.Constants;
import com.example.mall.common.ServiceResultEnum;
import com.example.mall.config.annotation.TokenToUser;
import com.example.mall.domain.User;
import com.example.mall.domain.repo.UserRepo;
import com.example.mall.domain.UserToken;
import com.example.mall.domain.repo.UserTokenRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class TokenToUserMethodArgumentResolver implements HandlerMethodArgumentResolver {


    private UserRepo userRepo;
    private UserTokenRepo userTokenRepo ;

    @Autowired
    public TokenToUserMethodArgumentResolver(UserRepo userRepo, UserTokenRepo userTokenRepo) {
        this.userRepo = userRepo;
        this.userTokenRepo = userTokenRepo;
    }

    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(TokenToUser.class);
    }

    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) {
        String token = webRequest .getHeader("token");
        if (null != token && !"".equals(token) && token.length() == Constants.TOKEN_LENGTH) {
            UserToken userToken = userTokenRepo.findUserTokenByToken(token);
            if (userToken == null || userToken.getExpireTime().getTime() <= System.currentTimeMillis()) {
                throw new  RuntimeException(ServiceResultEnum.NOT_LOGIN_ERROR.getResult());
            }
            User user = userRepo.findUserById(userToken.getUserId());
            if (user == null) {
                throw new  RuntimeException(ServiceResultEnum.USER_NULL_ERROR.getResult());
            }
            return user;
        } else {
            throw new  RuntimeException("NOT_LOGIN_ERROR");
        }
    }
}
