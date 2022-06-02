package com.example.mall.domain.repo;

import com.example.mall.domain.UserToken;
import org.springframework.data.repository.CrudRepository;

public interface UserTokenRepo extends CrudRepository<UserToken, Long> {

    UserToken findUserTokenByUserId(Long id);

    UserToken findUserTokenByToken(String token);

    UserToken save(UserToken userToken);
}
