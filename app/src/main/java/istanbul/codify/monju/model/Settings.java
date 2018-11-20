package istanbul.codify.monju.model;

public final class Settings {

    public Integer distance;
    public ProfileVisibility profileVisibility;

    public Settings(Integer distance) {
        this.distance = distance;
    }

    public Settings(ProfileVisibility profileVisibility) {
        this.profileVisibility = profileVisibility;
    }
}
