package istanbul.codify.muudy.api.pojo.request;

public final class CommentPostRequest {

    public String token;
    public long postId;
    public String comment;

    public CommentPostRequest(long postId, String comment) {
        this.postId = postId;
        this.comment = comment;
    }
}
