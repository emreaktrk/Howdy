package istanbul.codify.monju.model;

import android.support.annotation.StringDef;

@StringDef({UsersScreenMode.AROUND_WITH_EMOTION,
        UsersScreenMode.FOLLOWER,
        UsersScreenMode.FOLLOWING,
        UsersScreenMode.CONTACTS,
        UsersScreenMode.USERS,
        UsersScreenMode.LIKERS,
        UsersScreenMode.FACEBOOK
})
public @interface UsersScreenMode {
    String AROUND_WITH_EMOTION = "Etrafındakiler";
    String FOLLOWER = "Takip Edilenler";
    String FOLLOWING = "Takipçiler";
    String CONTACTS = "Kişilerim";
    String USERS = "Takip Ettikleri";
    String LIKERS = "Beğenenler";
    String FACEBOOK = "Facebook Kişilerim";
    String AROUND= "Çevrendekiler";
}