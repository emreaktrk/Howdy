package istanbul.codify.muudy.ui.around;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.AroundUsers;
import istanbul.codify.muudy.ui.base.BasePresenter;

import java.util.List;

final class AroundPresenter extends BasePresenter<AroundView> {

    @Override
    public void attachView(AroundView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.around_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");

                            view.onCloseClicked();
                        }));
    }

    void bind(List<AroundUsers> around) {
        AroundAdapter adapter = new AroundAdapter(around);
        mDisposables.add(
                adapter
                        .moreClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(users -> {
                            Logcat.v("More clicked");

                            mView.onMoreClicked(users);
                        }));

        mDisposables.add(
                adapter
                        .userClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                            Logcat.v("User clicked");

                            mView.onUserClicked(user);
                        }));
        findViewById(R.id.around_recycler, RecyclerView.class).setAdapter(adapter);
    }
}
