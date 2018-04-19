package istanbul.codify.muudy.ui.base;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.View;
import io.reactivex.disposables.CompositeDisposable;
import istanbul.codify.muudy.model.FacebookProfile;

public abstract class BasePresenter<V extends MvpView> implements MvpPresenter<V> {

    protected V mView;
    protected View mRoot;
    protected CompositeDisposable mDisposables;

    @Override
    public void attachView(V view, View root) {
        mView = view;
        mRoot = root;

        mDisposables = new CompositeDisposable();
    }

    public final void attachView(V view, Fragment fragment) {
        attachView(view, fragment.getView());
    }

    public final void attachView(V view, Activity activity) {
        attachView(view, activity.getWindow().getDecorView().getRootView());
    }

    public final void attachView(V view) {
        mView = view;
        mRoot = null;

        mDisposables = new CompositeDisposable();
    }

    @Override
    public void detachView() {
        mView = null;
        mRoot = null;

        mDisposables.clear();
    }

    private boolean isViewAttached() {
        return mView != null;
    }

    private void assertViewAttached() {
        if (!isViewAttached()) {
            throw new ViewNotAttachedException();
        }
    }

    protected final <T extends View> T findViewById(@IdRes int id) {
        assertViewAttached();

        return mRoot.findViewById(id);
    }

    protected final <T extends View> T findViewById(@IdRes int id, Class<? extends T> clazz) {
        assertViewAttached();

        return clazz.cast(mRoot.findViewById(id));
    }

    protected final @NonNull
    Context getContext() {
        assertViewAttached();

        return mRoot.getContext();
    }

    protected static class ViewNotAttachedException extends RuntimeException {
        ViewNotAttachedException() {
            super("Make sure attach view first.");
        }
    }
}
