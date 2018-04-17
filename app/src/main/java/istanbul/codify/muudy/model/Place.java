package istanbul.codify.muudy.model;

import java.io.Serializable;

public final class Place implements Selectable, Serializable {
    @Override
    public long id() {
        return 0;
    }

    @Override
    public String text() {
        return "";
    }
}
