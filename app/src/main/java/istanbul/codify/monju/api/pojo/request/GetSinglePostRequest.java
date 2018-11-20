package istanbul.codify.monju.api.pojo.request;

public final class GetSinglePostRequest {

    public String token;
    public long postid;

    public GetSinglePostRequest(long postId) {
        this.postid = postId;
    }
}
