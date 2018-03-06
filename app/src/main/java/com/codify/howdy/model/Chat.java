package com.codify.howdy.model;

import java.io.Serializable;
import java.util.Date;

public final class Chat implements Serializable {

    public long idmessage;
    public long message_fromuserid;
    public long message_touserid;
    public String message_text;
    public Date message_date;
    public int message_isreaded;
    public String message_img_path;
}
