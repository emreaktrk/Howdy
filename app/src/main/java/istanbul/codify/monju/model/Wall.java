package istanbul.codify.monju.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class Wall implements Serializable{

    public ArrayList<Post> posts;
    public ArrayList<User> recomendedUsers;
    public ArrayList<User> post_other_users_json;
    public ArrayList<Emotion> nearEmotions;
}
