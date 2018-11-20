package istanbul.codify.monju.model;

import istanbul.codify.monju.view.FollowButton;

public final class Follow {

    public User mUser;
    public FollowButton mCompound;

    public Follow(User user, FollowButton compound) {
        mUser = user;
        mCompound = compound;
    }
}
