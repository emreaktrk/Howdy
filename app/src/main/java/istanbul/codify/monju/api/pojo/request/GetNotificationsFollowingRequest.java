package istanbul.codify.monju.api.pojo.request;

import istanbul.codify.monju.model.NotificationType;

public final class GetNotificationsFollowingRequest {

    public String token;
    public final NotificationType type = NotificationType.FOLLOWINGS;
}
