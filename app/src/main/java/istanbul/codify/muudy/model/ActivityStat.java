package istanbul.codify.muudy.model;

import java.util.ArrayList;
import java.util.List;

public final class ActivityStat {

    public int postCount;
    public String post_emoji_word;
    public String post_emoji;
    public float percent;
    public List<ActivityStat> otherEmojis = new ArrayList<>();

}
