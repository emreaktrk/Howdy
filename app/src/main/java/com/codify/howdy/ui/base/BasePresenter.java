package com.codify.howdy.ui.base;

import android.view.View;

import java.util.ArrayList;

import io.reactivex.disposables.Disposable;

public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    private V mView;
    protected ArrayList<Disposable> mDisposables;

    @Override
    public void attachView(V view, View root) {
        mView = view;

        mDisposables = new ArrayList<>();
    }

    @Override
    public void detachView() {
        mView = null;

        for (Disposable disposable : mDisposables) {
            disposable.dispose();
        }
    }

    public boolean isViewAttached() {
        return mView != null;
    }

    public void assertViewAttached() {
        if (!isViewAttached()) {
            throw new ViewNotAttachedException();
        }
    }

    protected static class ViewNotAttachedException extends RuntimeException {
        ViewNotAttachedException() {
            super("Make sure attach view first.");
        }
    }
}
