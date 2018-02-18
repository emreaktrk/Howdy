package com.codify.howdy.model;

import java.io.Serializable;
import java.util.Objects;

public final class Word implements Serializable {

    public long idwords;
    public String words_word;
    public long words_award_id;
    public String words_emoji_url;
    public String words_verb_at_end;
    public String words_verb_in_middle;
    public long words_top_category_id;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Word word = (Word) o;
        return idwords == word.idwords &&
                words_award_id == word.words_award_id &&
                words_top_category_id == word.words_top_category_id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idwords, words_award_id, words_top_category_id);
    }
}
