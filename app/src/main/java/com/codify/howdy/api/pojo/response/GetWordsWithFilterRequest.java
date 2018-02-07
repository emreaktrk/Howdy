package com.codify.howdy.api.pojo.response;

import com.codify.howdy.model.Word;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class GetWordsWithFilterRequest {

    public long activityId;
    public List<Long> categoriesIDarray;

    public GetWordsWithFilterRequest(long activityId, List<Long> categoriesIDarray) {
        this.activityId = activityId;
        this.categoriesIDarray = categoriesIDarray;
    }

    public GetWordsWithFilterRequest(long activityId, Collection<Word> words) {
        this.activityId = activityId;
        ArrayList<Long> ids = new ArrayList<>();
        for (Word word : words) {
            ids.add(word.words_top_category_id);
        }
        this.categoriesIDarray = ids;
    }
}
