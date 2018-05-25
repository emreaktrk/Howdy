package istanbul.codify.muudy.ui.WeeklyTopUser;

import android.support.annotation.MainThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.*;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.account.AccountUtils;
import istanbul.codify.muudy.api.ApiManager;
import istanbul.codify.muudy.api.pojo.ServiceConsumer;
import istanbul.codify.muudy.api.pojo.request.WeeklyTopUsersRequest;
import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.api.pojo.response.WeeklyTopsUsersResponse;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.WeeklyTopUser;
import istanbul.codify.muudy.ui.base.BasePresenter;

import java.util.ArrayList;

public class WeeklyTopUsersPresenter extends BasePresenter<WeeklyTopUsersView> {


    @Override
    public void attachView(WeeklyTopUsersView view, View root) {
        super.attachView(view, root);

       /* mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.givevote_dialog_share))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Share clicked");
                            view.onGivePointClicked(findViewById(R.id.givevote_stars, MaterialRatingBar.class).getRating());
                        }));

        mDisposables.add(
                RxRatingBar
                        .ratingChanges(findViewById(R.id.givevote_stars,MaterialRatingBar.class))
                        .subscribe(point -> {
                            findViewById(R.id.givevote_dialog_point,AppCompatTextView.class).setText(point+"/5.0");
                        }));*/
        findViewById(R.id.weekly_top_recycler, RecyclerView.class).setLayoutManager(new LinearLayoutManager(getContext()));

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.weekly_top_close))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Back clicked");

                            view.onCloseClick();
                        }));

    }

    public void getWeeklyTopUsers() {
        WeeklyTopUsersRequest request = new WeeklyTopUsersRequest();
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .getWeeklyTopUsers(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<WeeklyTopsUsersResponse>() {
                            @Override
                            protected void success(WeeklyTopsUsersResponse response) {
                                mView.onLoaded(response.data);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(ArrayList<WeeklyTopUser> weeklyTopUsers) {
        WeeklyTopUsersAdapter weeklyTopUsersAdapter = new WeeklyTopUsersAdapter(weeklyTopUsers);

        mDisposables.add(
                weeklyTopUsersAdapter
                        .itemClicks()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(user -> {
                                mView.onUserPhotoClicked(user);
                                }
                        )
        );
        findViewById(R.id.weekly_top_recycler, RecyclerView.class).setAdapter(weeklyTopUsersAdapter);
    }
}
