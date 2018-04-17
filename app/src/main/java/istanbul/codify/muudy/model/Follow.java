package istanbul.codify.muudy.model;

public final class Follow {

    public User mUser;
    public boolean isFollowed;

    public Follow(User user, boolean followed) {
        mUser = user;
        isFollowed = followed;
    }
}
