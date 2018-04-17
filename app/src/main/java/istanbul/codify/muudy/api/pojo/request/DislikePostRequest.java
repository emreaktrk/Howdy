package istanbul.codify.muudy.api.pojo.request;

public final class DislikePostRequest {

    public String token;
    public long postId;

    public DislikePostRequest(long postId) {
        this.postId = postId;
    }
}
