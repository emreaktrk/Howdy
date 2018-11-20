package istanbul.codify.monju.model;

public final class FacebookProfile {

    public String id;
    public String name;
    public Gender gender;
    public String link;
    public Picture picture;
    public String email;

    public static class Picture {

        public Data data;

        public static class Data {
            public String url;
        }
    }
}
