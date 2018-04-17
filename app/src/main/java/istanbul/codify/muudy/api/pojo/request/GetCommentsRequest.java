package istanbul.codify.muudy.api.pojo.request;

public final class GetCommentsRequest {

    public String token;
    public long postId;

    public GetCommentsRequest(long postId) {
        this.postId = postId;
    }
}
