package com.codify.howdy.model.zipper;

import com.codify.howdy.model.Post;

public class Like {

    public boolean isChecked;
    public Post post;

    public Like(Post post, boolean isChecked) {
        this.post = post;
        this.isChecked = isChecked;
    }
}
