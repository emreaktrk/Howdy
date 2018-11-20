package istanbul.codify.monju.api.pojo.request;

public final class GetFollowersRequest {

    public String token;
    public long userId;

    public GetFollowersRequest() {
    }

    public GetFollowersRequest(long userId) {
        this.userId = userId;
    }
}
