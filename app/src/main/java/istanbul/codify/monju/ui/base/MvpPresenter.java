package istanbul.codify.monju.ui.base;

import android.view.View;

public interface MvpPresenter<V extends MvpView> {

    void attachView(V view, View root);

    void detachView();
}
