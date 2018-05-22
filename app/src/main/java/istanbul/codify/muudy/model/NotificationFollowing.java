package istanbul.codify.muudy.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class NotificationFollowing implements Serializable {

    public int actionCount;
    public long notification_fromuserid;
    public NotificationActionType notification_actiontype;
    public ArrayList<User> followedUsers;
    public ArrayList<Post> likedPosts;
    public User fromUser;
    public String notification_msg;
}
