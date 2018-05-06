package istanbul.codify.muudy.api.pojo.request;

public final class LikePostRequest {

    public String token;
    public long postid;

    public LikePostRequest(long postId) {
        this.postid = postId;
    }
}
