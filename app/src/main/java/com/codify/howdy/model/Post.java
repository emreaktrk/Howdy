package com.codify.howdy.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class Post implements Serializable {

    public long idpost;
    public String post_userid;
    public String post_text;
    public String post_mediapath;
    public PostMediaType post_media_type;
    public int post_commentcount;
    public Visibility visibility;
    public String post_date;
    public int post_isliked;
    public long post_activityid;
    public double post_longitude;
    public double post_latitude;
    public ArrayList<Word> post_words_json;
}
