package com.htweb.api.mappers;

import com.htweb.api.dtos.user.UserDetailResponse;
import com.htweb.core.pojo.Role;
import com.htweb.core.pojo.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDetailResponse toDetailResponse(User user);

    default String map(Role role) {
        return role.getName();
    }
}
