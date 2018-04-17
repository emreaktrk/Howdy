package istanbul.codify.muudy.model.zipper;

import istanbul.codify.muudy.model.Comment;
import istanbul.codify.muudy.model.Post;

import java.util.List;

public final class PostDetail {

    public final Post post;
    public final List<Comment> comments;

    public PostDetail(Post post, List<Comment> comments) {
        this.post = post;
        this.comments = comments;
    }
}
