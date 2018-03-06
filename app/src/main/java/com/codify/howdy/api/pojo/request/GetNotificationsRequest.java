package com.codify.howdy.api.pojo.request;

import com.codify.howdy.model.NotificationType;

public final class GetNotificationsRequest {

    public String token;
    public NotificationType type;

    public GetNotificationsRequest(NotificationType type) {
        this.type = type;
    }
}
