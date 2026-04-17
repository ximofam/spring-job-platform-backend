package com.htweb.api.mappers;

import com.htweb.api.dtos.UserDto;
import com.htweb.core.pojo.Role;
import com.htweb.core.pojo.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto.DetailResponse toDetailResponse(User user);

    default String map(Role role) {
        return role.getName();
    }
}
