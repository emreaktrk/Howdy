package istanbul.codify.monju.model;

import java.io.Serializable;

public final class Web implements Serializable {

    public String url;
    public String title;

    public Web(String title, @URL String url) {
        this.url = url;
        this.title = title;
    }
}
