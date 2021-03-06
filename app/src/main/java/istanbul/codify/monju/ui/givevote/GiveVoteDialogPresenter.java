package istanbul.codify.monju.ui.givevote;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import com.jakewharton.rxbinding2.view.RxView;
import com.jakewharton.rxbinding2.widget.RxRatingBar;
import io.reactivex.android.schedulers.AndroidSchedulers;
import istanbul.codify.monju.R;
import istanbul.codify.monju.account.AccountUtils;
import istanbul.codify.monju.api.ApiManager;
import istanbul.codify.monju.api.pojo.ServiceConsumer;
import istanbul.codify.monju.api.pojo.request.GivePointRequest;
import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.api.pojo.response.GivePointResponse;
import istanbul.codify.monju.logcat.Logcat;
import istanbul.codify.monju.ui.base.BasePresenter;
import me.zhanghai.android.materialratingbar.MaterialRatingBar;

final public class GiveVoteDialogPresenter extends BasePresenter<GiveVoteView> {

    @Override
    public void attachView(GiveVoteView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
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
                        }));


    }


    void givePoint(Long id, Float point){
        GivePointRequest request = new GivePointRequest();
        request.token = AccountUtils.tokenLegacy(getContext());
        request.postId = id;
        request.point = point;

        mDisposables.add(
                ApiManager
                        .getInstance()
                        .givePoint(request)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ServiceConsumer<GivePointResponse>() {
                            @Override
                            protected void success(GivePointResponse response) {
                                mView.onPointGiven(response);
                            }

                            @Override
                            protected void error(ApiError error) {
                                Logcat.e(error);

                                mView.onError(error);
                            }
                        }));
    }

    void bind(String sentence) {
        findViewById(R.id.givevote_dialog_sentence, AppCompatTextView.class).setText(sentence);
        findViewById(R.id.givevote_stars, MaterialRatingBar.class).setProgress(5);

    }
}
