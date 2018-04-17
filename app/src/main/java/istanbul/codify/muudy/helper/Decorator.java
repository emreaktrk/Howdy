package istanbul.codify.muudy.helper;

public abstract class Decorator<T> {

    public Decorator(T object) {
        decorate(object);
    }

    protected abstract void decorate(T object);
}
