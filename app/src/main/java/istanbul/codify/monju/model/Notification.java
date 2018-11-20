package istanbul.codify.monju.model;

import java.io.Serializable;

public final class Notification implements Serializable{

    public long idnotification;
    public long notification_fromuserid;
    public long notification_touserid;
    public NotificationActionType notification_actiontype;
    public String notification_msg;
    public String notification_date;
    public long notification_postid;
    public String notification_answerhi_word_text;
    public String notification_answerhi_word_img;
    public String humanDate;
    public User fromUser;
}
