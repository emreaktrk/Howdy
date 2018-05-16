package istanbul.codify.muudy.model;

import android.support.annotation.StringDef;

@StringDef({UsersScreenMode.AROUND_WITH_EMOTION, UsersScreenMode.FOLLOWER, UsersScreenMode.FOLLOWING, UsersScreenMode.CONTACTS})
public @interface UsersScreenMode {
    String AROUND_WITH_EMOTION = "Etrafındakiler";
    String FOLLOWER = "Takip Edilenler";
    String FOLLOWING = "Takipçiler";
    String CONTACTS = "Kişilerim";
}