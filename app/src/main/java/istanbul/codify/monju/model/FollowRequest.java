package istanbul.codify.monju.model;

import java.io.Serializable;

public final class FollowRequest implements Serializable {
    public long idfr;
    public long fr_follower_userid;
    public String msg;
    public String humanDate;
    public User user;
}
