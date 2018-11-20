package istanbul.codify.monju.api.pojo.request;

public final class GetActivityPostsRequest {

    public String token;
    public long activityid;
    public String word;

    public GetActivityPostsRequest(long activityid) {
        this.activityid = activityid;
    }
}
