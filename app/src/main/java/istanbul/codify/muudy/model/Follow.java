package istanbul.codify.muudy.model;

import istanbul.codify.muudy.view.FollowButton;

public final class Follow {

    public User mUser;
    public FollowButton mCompound;

    public Follow(User user, FollowButton compound) {
        mUser = user;
        mCompound = compound;
    }
}
