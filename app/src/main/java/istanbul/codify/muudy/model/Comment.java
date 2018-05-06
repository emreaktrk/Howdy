package istanbul.codify.muudy.model;

import java.io.Serializable;
import java.util.Date;

public final class Comment implements Serializable {

    public long idpostcomment;
    public long postcomment_commenterid;
    public String postcomment_text;
    public long postcomment_postid;
    public Date postcomment_date;
    public User commenterUser;
    public String humanDate;
}
