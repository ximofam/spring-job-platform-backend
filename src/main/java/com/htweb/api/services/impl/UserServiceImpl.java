package com.htweb.api.services.impl;

import com.htweb.api.dtos.UserDto;
import com.htweb.api.exceptions.users.UserNotFoundException;
import com.htweb.api.mappers.UserMapper;
import com.htweb.api.services.UserService;
import com.htweb.core.pojo.User;
import com.htweb.core.repositories.UserAuthRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service("apiUserService")
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserAuthRepository userAuthRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional(readOnly = true)
    public UserDto.DetailResponse getUserById(Long id) {
        User user = userAuthRepository.findById(id).orElseThrow(UserNotFoundException::new);

        return userMapper.toDetailResponse(user);
    }
}
