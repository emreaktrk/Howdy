package istanbul.codify.monju.api.pojo.request;


import istanbul.codify.monju.model.*;

import java.util.ArrayList;
import java.util.List;

public final class CreatePostTextRequest {

    public String token;
    public String placeName;
    public ArrayList<Long> words;
    public ArrayList<Long> otherUsers;
    public Long activityid;
    public String extraStringForSeries;

    public CreatePostTextRequest() {
        words = new ArrayList<>();
        otherUsers = new ArrayList<>();
    }

    public CreatePostTextRequest(List<Selectable> selecteds) {
        words = new ArrayList<>();
        otherUsers = new ArrayList<>();

        for (Selectable selected : selecteds) {
            if (selected instanceof Word) {
                words.add(selected.id());
            }

            if (selected instanceof User) {
                otherUsers.add(selected.id());
            }

            if (selected instanceof Activity) {
                activityid = selected.id();
            }

            if (selected instanceof Place) {
                placeName = selected.text();
            }
        }
    }
}
