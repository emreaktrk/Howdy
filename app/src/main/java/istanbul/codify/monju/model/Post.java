package istanbul.codify.monju.model;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public final class Post implements Serializable {

    public long idpost;
    public long post_userid;
    public String post_text;
    public String post_mediapath;
    public PostMediaType post_media_type;
    public String post_video_path;
    public PostVisibility post_visibility;
    public String post_date;
    public long post_activityid;
    public double post_longitude;
    public double post_latitude;
    public int post_likecount;
    public String post_emoji;
    public String post_emoji_word;
    public int post_commentcount;
    public String post_place_name;
    public long post_sharegroup0_wordid;
    public long post_sharegroup3_wordid;

    public long iduser;
    public String username;
    public String imgpath1;
    public String imgpath2;
    public String imgpath3;
    public String email;
    public boolean isliked;
    public String unicode;
    public String tokenstring;
    public int postcount;
    public int followercount;
    public int totallike;
    public String coverimgpath;
    public int isdmclosed;
    public String bio;
    public int isbanned;
    public String namesurname;
    public Gender gender;
    public String birthDate;
    public String words_word;
    public float post_given_point;
    public String words_emoji_url;
    public ArrayList<Badge> rozetler;
    public String humanDate;
    public String extraStringForSeries;



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return idpost == post.idpost;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idpost);
    }
}
