package istanbul.codify.muudy.api.pojo.request;

public final class CommentPostRequest {

    public String token;
    public long postid;
    public String comment;

    public CommentPostRequest(long postId, String comment) {
        this.postid = postId;
        this.comment = comment;
    }
}
