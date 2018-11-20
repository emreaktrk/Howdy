package istanbul.codify.monju.api.pojo.request;

public final class DislikePostRequest {

    public String token;
    public long postid;

    public DislikePostRequest(long postId) {
        this.postid = postId;
    }
}
