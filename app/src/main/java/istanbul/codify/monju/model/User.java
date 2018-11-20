package istanbul.codify.monju.model;

import com.google.gson.Gson;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public final class User implements Selectable, Serializable {

    public long iduser;
    public String username;
    public String imgpath1;
    public String imgpath2;
    public String imgpath3;
    public String tokenstring;
    public int postcount;
    public int followercount;
    public int followedcount;
    public String coverimgpath;
    public ProfileVisibility isprofilehidden;
    public String bio;
    public int isbanned;
    public String namesurname;
    public Gender gender;
    public Date birtDate;
    public String facebookuserid;
    public String phone;
    public String facebooklink;
    public String instagramlink;
    public String twitterlink;
    public long award1id;
    public long award2id;
    public long award3id;
    public int distance_km;
    public int totallike;
    public String email;
    public String pass;
    public String pushToken;
    public DeviceType deviceType;
    public int push_on_like;
    public int push_on_follow;
    public int push_on_tag;
    public String award1Text;
    public String award2Text;
    public String award3Text;
    public FollowState isfollowing;

    @Override
    public long id() {
        return iduser;
    }

    @Override
    public String emoji() {
        return null;
    }

    @Override
    public long topCategoryId() {
        return 0;
    }

    @Override
    public String text() {
        return "@" + username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null) {
            return false;
        }

        if (o instanceof Long) {
            return iduser == (long) o;
        }

        if (getClass() != o.getClass()) return false;
        User user = (User) o;
        return iduser == user.iduser;
    }

    @Override
    public int hashCode() {
        return Objects.hash(iduser);
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
