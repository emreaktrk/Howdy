package istanbul.codify.muudy.model;

import java.io.Serializable;
import java.util.Objects;

public final class Activity implements Selectable, Serializable {

    public long idactivities;
    public String activities_title;
    public String activities_icon_url;
    public static int ID = 9999;

    @Override
    public long id() {
        return idactivities;
    }

    @Override
    public String emoji() {
        return activities_icon_url;
    }

    @Override
    public long topCategoryId() {
        return ID;
    }

    @Override
    public String text() {
        return activities_title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Activity activity = (Activity) o;
        return idactivities == activity.idactivities;
    }

    @Override
    public int hashCode() {
        return Objects.hash(idactivities);
    }
}
