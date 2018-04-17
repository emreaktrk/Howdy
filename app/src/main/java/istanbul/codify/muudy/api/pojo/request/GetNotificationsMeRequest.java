package istanbul.codify.muudy.api.pojo.request;

import istanbul.codify.muudy.model.NotificationType;

public final class GetNotificationsMeRequest {

    public String token;
    public final NotificationType type = NotificationType.ME;
}
