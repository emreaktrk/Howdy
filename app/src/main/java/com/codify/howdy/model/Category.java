package com.codify.howdy.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class Category implements Serializable {

    public long id_word_top_category;
    public String words_top_category_text;
    public int words_top_category_share_group;
    public String words_top_category_icon;
    public ArrayList<Word> words;

    public boolean isLocation() {
        return id_word_top_category == 13;
    }

    public boolean isMeeting() {
        return id_word_top_category == 10;
    }
}
