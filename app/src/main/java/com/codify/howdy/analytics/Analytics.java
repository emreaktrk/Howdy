package com.codify.howdy.analytics;

import android.support.annotation.StringDef;

import java.util.ArrayList;
import java.util.List;

public final class Analytics {

    private static Analytics INSTANCE;

    private List<IAnalyst> mList = new ArrayList<>();

    private Analytics() {
    }

    public static Analytics getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Analytics();
        }

        return INSTANCE;
    }

    public void custom(@Events String event) {
        for (IAnalyst analyst : mList) {
            analyst.custom(event);
        }
    }

    public Analytics add(IAnalyst analyst) {
        mList.add(analyst);

        return this;
    }

    @StringDef({Events.COMPOSE, Events.COMMENT, Events.LIKE, Events.DISLIKE, Events.IMAGE, Events.VIDEO})
    public @interface Events {
        String COMPOSE = "compose";
        String COMMENT = "comment";
        String LIKE = "like";
        String DISLIKE = "dislike";
        String IMAGE = "image";
        String VIDEO = "video";
    }
}
