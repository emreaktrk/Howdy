package istanbul.codify.monju.model.event;

import istanbul.codify.monju.model.NewPost;

public final class PostEvent {

    public final NewPost newPost;


    public PostEvent(NewPost post) {
        newPost = post;
    }
}
