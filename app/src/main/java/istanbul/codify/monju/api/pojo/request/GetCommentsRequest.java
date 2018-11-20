package istanbul.codify.monju.api.pojo.request;

public final class GetCommentsRequest {

    public String token;
    public long postid;

    public GetCommentsRequest(long postId) {
        this.postid = postId;
    }
}
