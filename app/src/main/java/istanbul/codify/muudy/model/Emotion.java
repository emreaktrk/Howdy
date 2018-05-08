package istanbul.codify.muudy.model;

import java.io.Serializable;

public final class Emotion implements Serializable {

    public long idwords;
    public String words_word;
    public long words_top_category_id;
    public String words_verb_in_middle;
    public String words_verb_at_end;
    public String words_emoji_url;
    public Arrow words_arrow;
    public int words_usage_order;
}
