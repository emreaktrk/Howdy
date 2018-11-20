package istanbul.codify.monju.helper;

public abstract class Decorator<T> {

    public Decorator(T object) {
        decorate(object);
    }

    protected abstract void decorate(T object);

    public Decorator(T object, T object2, T object3) {
        decorate(object, object2, object3);
    }

    protected abstract void decorate(T object, T object2, T object3);
}
