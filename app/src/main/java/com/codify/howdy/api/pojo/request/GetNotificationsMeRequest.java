package com.codify.howdy.api.pojo.request;

import com.codify.howdy.model.NotificationType;

public final class GetNotificationsMeRequest {

    public String token;
    public final NotificationType type = NotificationType.ME;
}
