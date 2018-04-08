package com.codify.howdy.api.pojo.request;

import com.codify.howdy.model.NotificationType;

public final class GetNotificationsFollowingRequest {

    public String token;
    public final NotificationType type = NotificationType.FOLLOWINGS;
}
