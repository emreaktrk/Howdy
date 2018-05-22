package istanbul.codify.muudy.model;

public final class Notification {

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
