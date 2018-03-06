package com.codify.howdy.model;

import java.io.Serializable;

public final class UserMessage implements Serializable {

    public long message_touserid;
    public long message_fromuserid;
    public String type;
    public int message_isreaded;
    public String message_text;
}
