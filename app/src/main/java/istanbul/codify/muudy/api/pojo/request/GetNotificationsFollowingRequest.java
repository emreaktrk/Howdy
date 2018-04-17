package istanbul.codify.muudy.api.pojo.request;

import istanbul.codify.muudy.model.NotificationType;

public final class GetNotificationsFollowingRequest {

    public String token;
    public final NotificationType type = NotificationType.FOLLOWINGS;
}
