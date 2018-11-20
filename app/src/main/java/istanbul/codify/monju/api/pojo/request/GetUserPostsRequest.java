package istanbul.codify.monju.api.pojo.request;

import android.support.annotation.NonNull;

public final class GetUserPostsRequest {

    public String token;
    public long userId;
    public long categoryId;
    public long page;

    public GetUserPostsRequest(@NonNull Long userId) {
        this.userId = userId;
    }
}
