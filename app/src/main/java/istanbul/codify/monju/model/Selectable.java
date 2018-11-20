package istanbul.codify.monju.model;

public interface Selectable {

    int NO_ID = -1;

    long id();

    String emoji();

    long topCategoryId();

    String text();
}
