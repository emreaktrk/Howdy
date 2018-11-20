package istanbul.codify.monju.api.pojo.request;


import istanbul.codify.monju.model.Coordinate;
import istanbul.codify.monju.model.*;

import java.util.ArrayList;
import java.util.List;

public final class NewPostRequest {

    public String token;
    public String text;
    public ArrayList<Long> words;
    public PostVisibility visibility;
    public ArrayList<Long> otherUsers;
    public Coordinate coordinates;
    public long activityid;
    public String placeName;
    public String mediaData;
    public PostMediaType mediaType;
    public String videoThumbnailPath;
    public String videoUploadedPath;

    public NewPostRequest() {
        words = new ArrayList<>();
        otherUsers = new ArrayList<>();
    }

    public NewPostRequest(List<Selectable> selecteds) {
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

            if (selected instanceof Place){
                placeName = selected.text();
            }
        }
    }
}
