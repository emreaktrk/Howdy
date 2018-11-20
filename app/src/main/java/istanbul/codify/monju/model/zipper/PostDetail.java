package istanbul.codify.monju.model.zipper;

import istanbul.codify.monju.model.Comment;
import istanbul.codify.monju.model.Post;

import java.util.List;

public final class PostDetail {

    public final Post post;
    public final List<Comment> comments;

    public PostDetail(Post post, List<Comment> comments) {
        this.post = post;
        this.comments = comments;
    }
}
