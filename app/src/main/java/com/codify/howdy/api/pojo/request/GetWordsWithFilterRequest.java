package com.codify.howdy.api.pojo.request;

import android.support.annotation.Nullable;
import com.codify.howdy.model.Selectable;
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

    public GetWordsWithFilterRequest(long activityid, @Nullable Collection<Selectable> words) {
        this.activityid = activityid;
        ArrayList<Long> ids = new ArrayList<>();
        if (words != null) {
            for (Selectable selected : words) {
                if (selected != null && selected instanceof Word) {
                    ids.add(selected.id());
                }
            }
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
