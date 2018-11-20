package istanbul.codify.monju.ui.around;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;

import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.GetSinglePostRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.GetSinglePostResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.model.AroundUsers;
import istanbul.codify.monju.model.Post;
import istanbul.codify.monju.ui.base.BasePresenter;

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

    void bind(List<AroundUsers> around, @Nullable Post post) {
        AroundAdapter adapter = new AroundAdapter(around,post);
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

    void addBlurredBackground(Bitmap bitmap){
        findViewById(R.id.around_background_image, AppCompatImageView.class).setBackgroundDrawable(new BitmapDrawable(getContext().getResources(),bitmap));
    }

    void getPostDetail(long postId, List<AroundUsers> around) {

        GetSinglePostRequest request = new GetSinglePostRequest(postId);
        request.token = AccountUtils.tokenLegacy(getContext());

        mDisposables.add(

                ApiManager
                        .getInstance()
                        .getSinglePost(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GetSinglePostResponse>() {
                            @Override
                            protected void success(GetSinglePostResponse response) {
                                mView.onPostLoaded(around, response.data);

                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);
                                mView.onPostLoadedError(around);

                            }
                        }));
    }
}
