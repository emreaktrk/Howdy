package istanbul.codify.muudy.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class NotificationsMe implements Serializable {

    public ArrayList<Notification> userNotifications;
    public ArrayList<FollowRequest> followRequests;
}
