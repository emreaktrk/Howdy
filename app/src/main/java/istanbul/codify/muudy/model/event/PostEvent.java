package istanbul.codify.muudy.model.event;

import istanbul.codify.muudy.model.NewPost;

public final class PostEvent {

    public final NewPost newPost;


    public PostEvent(NewPost post) {
        newPost = post;
    }
}
