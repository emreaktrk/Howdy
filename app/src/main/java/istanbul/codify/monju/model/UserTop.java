package istanbul.codify.monju.model;

import java.io.Serializable;
import java.util.Date;

public class UserTop implements Serializable {
    public Long idwtu;
    public Long wtu_userid;
    public Long wtu_awardid;
    public Date wtu_date;
    public String awards_text;
    public String awards_imgpath;
    public String awards_top_user_text_for_this_user;
    public String awards_top_user_text_for_other_users;
}

