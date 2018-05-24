package istanbul.codify.muudy.model;

import java.io.Serializable;

public class WeeklyTopUser implements Serializable {

    public Long idwtu;
    public Long wtu_userid;
    public Long wtu_awardid;
    public String wtu_date;
    public User user;
    public Award award;

}
