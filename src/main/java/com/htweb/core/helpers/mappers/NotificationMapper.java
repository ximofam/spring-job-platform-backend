package com.htweb.core.helpers.mappers;

import com.htweb.core.helpers.dtos.NotificationRequest;
import com.htweb.core.helpers.dtos.NotificationResponse;
import com.htweb.core.pojo.Notification;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface NotificationMapper {
    @Mapping(target = "user", ignore = true)
    Notification toNotification(NotificationRequest request);

    @Mapping(source = "createdAt", target = "sendAt")
    NotificationResponse toNotificationResponse(Notification notification);

    List<NotificationResponse> toNotificationResponseList(List<Notification> notifications);
}
