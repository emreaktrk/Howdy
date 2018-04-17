package istanbul.codify.muudy.api.pojo.request;

import istanbul.codify.muudy.model.DeviceType;
import istanbul.codify.muudy.model.Gender;
import istanbul.codify.muudy.model.Visibility;

import java.util.Date;

public final class UpdateProfileRequest {

    public String token;
    public String username;
    public String email;
    public String imgpath1;
    public String imgpath2;
    public String imgpath3;
    public String pass;
    public Visibility isprofilehidden;
    public String bio;
    public String namesurname;
    public Gender gender;
    public Date birtDate;
    public String facebookuserid;
    public String phone;
    public String facebooklink;
    public String instagramlink;
    public String twitterlink;
    public int distance_km;
    public String pushToken;
    public DeviceType deviceType;

}
