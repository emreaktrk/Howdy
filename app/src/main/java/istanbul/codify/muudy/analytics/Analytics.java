package istanbul.codify.muudy.analytics;

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

    @StringDef({
            Events.COMPOSE,
            Events.COMMENT,
            Events.LIKE, Events.DISLIKE,
            Events.FOLLOW, Events.UNFOLLOW,
            Events.CHANGE_PASSWORD, Events.DELETE_POST,
            Events.MUUDY, Events.UPDATE_PROFILE,
            Events.FORGOT_PASSWORD,
            Events.CHAT_MESSAGE,
            Events.CREATE_USER,
            Events.UPDATE_SETTINGS,
            Events.LOGOUT,
            Events.FEEDBACK,
            Events.SOCIAL_MEDIA})
    public @interface Events {
        String COMPOSE = "compose";
        String COMMENT = "comment";
        String LIKE = "like";
        String DISLIKE = "dislike";
        String FOLLOW = "follow";
        String UNFOLLOW = "unfollow";
        String CHANGE_PASSWORD = "change_password";
        String DELETE_POST = "delete_post";
        String MUUDY = "muudy";
        String UPDATE_PROFILE = "update_profile";
        String FORGOT_PASSWORD = "forgot_password";
        String CHAT_MESSAGE = "chat_message";
        String CREATE_USER = "create_user";
        String UPDATE_SETTINGS = "update_settings";
        String LOGOUT = "logout";
        String FEEDBACK = "feedback";
        String SOCIAL_MEDIA = "social_media";
    }
}
