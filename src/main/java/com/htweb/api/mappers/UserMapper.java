package com.htweb.api.mappers;

import com.htweb.api.dtos.auth.AuthRegisterRequest;
import com.htweb.api.dtos.user.EmployerProfileResponse;
import com.htweb.api.dtos.user.UserDetailResponse;
import com.htweb.api.dtos.user.UserSimpleResponse;
import com.htweb.api.dtos.user.UserUpdateRequest;
import com.htweb.core.pojo.EmployerProfile;
import com.htweb.core.pojo.User;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {CountryMapper.class, CompanyMapper.class, CandidateProfileMapper.class})
public interface UserMapper {
    @Mapping(source = "userRole", target = "role")
    @Mapping(target = "profile", ignore = true)
    UserDetailResponse toUserDetailResponse(User user);

    UserSimpleResponse toUserSimpleResponse(User user);

    EmployerProfileResponse toEmployerProfileResponse(EmployerProfile profile);

    @Mapping(source = "countryId", target = "country")
    User toUser(AuthRegisterRequest request);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(source = "countryId", target = "country")
    void updateUser(UserUpdateRequest request, @MappingTarget User user);
}
