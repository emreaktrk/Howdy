package istanbul.codify.muudy.model;

import java.io.Serializable;

public class PushNotification implements Serializable {
    public Long itemId;
    public NotificationActionType actionType;

    public PushNotification(Long itemId, NotificationActionType actionType) {
        this.itemId = itemId;
        this.actionType = actionType;
    }





}
