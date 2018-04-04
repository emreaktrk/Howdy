package com.codify.howdy.api.pojo.request;

import android.support.annotation.Nullable;
import com.codify.howdy.model.*;

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
        categoriesIDarray = new ArrayList<>();
        if (words != null) {
            for (Selectable selected : words) {
                if (selected != null && selected instanceof Word) {
                    categoriesIDarray.add(selected.id());
                }
            }
        }
    }

    public GetWordsWithFilterRequest(@Nullable Collection<Selectable> selecteds) {
        categoriesIDarray = new ArrayList<>();
        if (selecteds != null) {
            for (Selectable selected : selecteds) {
                if (selected != null) {
                    if (selected instanceof Word) {
                        categoriesIDarray.add(selected.id());
                    } else if (selected instanceof Activity) {
                        activityid = selected.id();
                    } else if (selected instanceof User) {
                        if (!categoriesIDarray.contains(Category.MEETING)) {
                            categoriesIDarray.add(Category.MEETING);
                        }
                    }
                }
            }
        }
    }

    public GetWordsWithFilterRequest(long activityid, long[] categoryIds) {
        this.activityid = activityid;
        this.categoriesIDarray = new ArrayList<>();
        for (long id : categoryIds) {
            categoriesIDarray.add(id);
        }
        addWords = true;
    }
}
