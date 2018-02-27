package com.codify.howdy.helper;

public abstract class Decorator<T> {

    public Decorator(T object) {
        decorate(object);
    }

    protected abstract void decorate(T object);
}
