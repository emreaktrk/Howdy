package istanbul.codify.monju.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class NotificationsMe implements Serializable {

    public ArrayList<Notification> userNotifications;

    public NotificationsMe(ArrayList<FollowRequest> followRequests) {
        this.followRequests = followRequests;
    }

    public ArrayList<FollowRequest> followRequests;
}
