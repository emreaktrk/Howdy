package istanbul.codify.monju.model;

import java.io.Serializable;
import java.util.ArrayList;

public final class Category implements Serializable {

    public static final long EMOTION = 1;
    public static final long LOCATION = 13;
    public static final long MEETING = 10;
    public static final long GAME = 12;
    public static final long SERIES = 20;
    public static final long FILM = 14;
    public static final long BOOK = 15;

    public long id_word_top_category;
    public String words_top_category_text;
    public int words_top_category_share_group;
    public String words_top_category_icon;
    public ArrayList<Word> words;

    public boolean isLocation() {
        return id_word_top_category == LOCATION;
    }

    public boolean isMeeting() {
        return id_word_top_category == MEETING;
    }
}
