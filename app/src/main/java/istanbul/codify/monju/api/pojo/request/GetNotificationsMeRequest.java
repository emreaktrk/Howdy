package istanbul.codify.monju.api.pojo.request;

import istanbul.codify.monju.model.NotificationType;

public final class GetNotificationsMeRequest {

    public String token;
    public final NotificationType type = NotificationType.ME;
}
