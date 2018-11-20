package istanbul.codify.monju.api.pojo.request;

import android.support.annotation.NonNull;

public final class GetUserWeeklyTopRequest {

    public String token;
    public long userId;

    public GetUserWeeklyTopRequest(@NonNull Long userId) {
        this.userId = userId;
    }
}
