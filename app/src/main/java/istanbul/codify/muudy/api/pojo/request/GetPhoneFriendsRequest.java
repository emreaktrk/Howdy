package istanbul.codify.muudy.api.pojo.request;

import java.util.ArrayList;

public final class GetPhoneFriendsRequest {

    public String token;
    public ArrayList<String> phoneNumberArray;

    public GetPhoneFriendsRequest(ArrayList<String> phones) {
        this.phoneNumberArray = phones;
    }
}
