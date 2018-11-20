package istanbul.codify.monju.model;

import java.io.Serializable;

public final class UserMessage implements Serializable {

    public long message_touserid;
    public long message_fromuserid;
    public String type;
    public Result message_isreaded;
    public String message_text;
    public User otherUser;
    public String message_humandate;
}
