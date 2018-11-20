package istanbul.codify.monju.model;

import java.io.Serializable;

public final class Tutorial implements Serializable {

    public String title;
    public String description;

    public Tutorial(String title, String description) {
        this.title = title;
        this.description = description;
    }
}
