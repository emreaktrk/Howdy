package istanbul.codify.muudy.api.pojo.request;

public final class DislikePostRequest {

    public String token;
    public long postid;

    public DislikePostRequest(long postId) {
        this.postid = postId;
    }
}
