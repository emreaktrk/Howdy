package istanbul.codify.muudy.api.pojo.request;


import istanbul.codify.muudy.model.FacebookProfile;
import istanbul.codify.muudy.model.Gender;

import java.util.Date;

public final class FacebookLoginRequest {

    public String namesurname;
    public String email;
    public Gender gender;
    public Date birthDate;
    public String profileImageUrl;
    public String phone;
    public String facebookID;

    public FacebookLoginRequest(FacebookProfile facebook) {
        namesurname = facebook.name;
        gender = facebook.gender;
        profileImageUrl = facebook.picture.data.url;
        facebookID = facebook.id;
    }
}
