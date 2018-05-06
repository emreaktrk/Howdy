package istanbul.codify.muudy.api.pojo.request;

public final class GetCommentsRequest {

    public String token;
    public long postid;

    public GetCommentsRequest(long postId) {
        this.postid = postId;
    }
}
