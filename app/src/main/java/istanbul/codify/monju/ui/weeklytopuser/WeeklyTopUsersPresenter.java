package istanbul.codify.monju.ui.weeklytopuser;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.*;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.WeeklyTopUsersRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.WeeklyTopsUsersResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.WeeklyTopUser;
import istanbul.codify.monju.ui.base.BasePresenter;

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

    void addBlurredBackground(Bitmap bitmap){
        findViewById(R.id.weekly_top_background_image, AppCompatImageView.class).setBackgroundDrawable(new BitmapDrawable(getContext().getResources(),bitmap));
    }
}
