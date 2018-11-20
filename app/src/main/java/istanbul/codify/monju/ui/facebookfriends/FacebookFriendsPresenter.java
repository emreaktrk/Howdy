package istanbul.codify.monju.ui.facebookfriends;

import android.view.View;

import istanbul.codify.monju.R;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;

final class FacebookFriendsPresenter extends BasePresenter<FacebookFriendsView> {

    @Override
    public void attachView(FacebookFriendsView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.facebook_friends_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Close clicked");
                            view.onCloseClicked();
                        }));
    }

    void getUsers() {

    }
}
