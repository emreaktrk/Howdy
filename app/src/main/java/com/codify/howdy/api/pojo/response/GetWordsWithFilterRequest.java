package com.codify.howdy.api.pojo.response;

import com.codify.howdy.model.Category;

import java.util.ArrayList;
import java.util.Collection;

public final class GetWordsWithFilterRequest {

    public long activityId;
    public ArrayList<Long> categoriesIDarray;

    public GetWordsWithFilterRequest(long activityId, ArrayList<Long> categoriesIDarray) {
        this.activityId = activityId;
        this.categoriesIDarray = categoriesIDarray;
    }

    public GetWordsWithFilterRequest(long activityId, Collection<Category> categories) {
        this.activityId = activityId;
        ArrayList<Long> ids = new ArrayList<>();
        for (Category category : categories) {
            ids.add(category.id_word_top_category);
        }
        this.categoriesIDarray = ids;
    }
}
