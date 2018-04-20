package istanbul.codify.muudy.api.pojo.request;


import istanbul.codify.muudy.model.*;

import java.util.ArrayList;
import java.util.List;

public final class CreatePostTextRequest {

    public String token;
    public String placeName;
    public ArrayList<Long> words;
    public ArrayList<Long> otherUsers;
    public Long activityid;

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
                words.add(selected.id());
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