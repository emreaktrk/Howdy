package com.codify.howdy.model.zipper;

import com.codify.howdy.model.Comment;
import com.codify.howdy.model.Post;

import java.util.List;

public final class PostDetail {

    public final Post post;
    public final List<Comment> comments;

    public PostDetail(Post post, List<Comment> comments) {
        this.post = post;
        this.comments = comments;
    }
}
