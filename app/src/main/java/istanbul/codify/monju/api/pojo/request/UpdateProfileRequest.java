package istanbul.codify.monju.api.pojo.request;

import istanbul.codify.monju.model.DeviceType;
import istanbul.codify.monju.model.Gender;
import istanbul.codify.monju.model.ProfileVisibility;

import java.util.Date;

public final class UpdateProfileRequest {

    public String token;
    public String username;
    public String email;
    public String imgpath1;
    public String imgpath2;
    public String imgpath3;
    public String pass;
    public ProfileVisibility isprofilehidden;
    public String bio;
    public String namesurname;
    public Gender gender;
    public Date birtDate;
    public String facebookuserid;
    public String phone;
    public String facebooklink;
    public String instagramlink;
    public String twitterlink;
    public Integer distance_km;
    public String pushToken;
    public DeviceType deviceType;

}
