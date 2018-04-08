package com.codify.howdy.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class NotificationsFollowing implements Serializable {

    public int actionCount;
    public long notification_fromuserid;
    public NotificationActionType notification_actiontype;
    public ArrayList<User> followedUsers;
    public User fromUser;
    public String notification_msg;
    public ArrayList<Post> likedPosts;
}
