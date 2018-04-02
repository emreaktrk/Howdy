package com.codify.howdy.api.pojo.request;

import com.codify.howdy.model.Word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GetWordsWithFilterRequest {

    public long activityid;
    public List<Long> categoriesIDarray;
    public boolean addWords;

    public GetWordsWithFilterRequest(long activityid, List<Long> categoriesIDarray) {
        this.activityid = activityid;
        this.categoriesIDarray = categoriesIDarray;
    }

    public GetWordsWithFilterRequest(long activityid, Collection<Word> words) {
        this.activityid = activityid;
        ArrayList<Long> ids = new ArrayList<>();
        for (Word word : words) {
            ids.add(word.words_top_category_id);
        }
        this.categoriesIDarray = ids;
    }

    public GetWordsWithFilterRequest(long activityid, long[] categoryIds) {
        this.activityid = activityid;
        ArrayList<Long> ids = new ArrayList<>();
        for (long id : categoryIds) {
            ids.add(id);
        }
        this.categoriesIDarray = ids;
        addWords = true;
    }
}
