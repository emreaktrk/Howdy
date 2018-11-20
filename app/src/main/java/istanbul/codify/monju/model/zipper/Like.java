package istanbul.codify.monju.model.zipper;

import istanbul.codify.monju.model.Post;

public class Like {

    public boolean isChecked;
    public Post post;

    public Like(Post post, boolean isChecked) {
        this.post = post;
        this.isChecked = isChecked;
    }
}
