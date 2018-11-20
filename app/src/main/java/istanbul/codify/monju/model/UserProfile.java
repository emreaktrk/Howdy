package istanbul.codify.monju.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class UserProfile implements Serializable {

    public User user;
    public ArrayList<Post> postlist;
}
