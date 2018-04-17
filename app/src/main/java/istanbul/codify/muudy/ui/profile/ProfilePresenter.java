package istanbul.codify.muudy.ui.profile;

import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import istanbul.codify.muudy.BuildConfig;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.logcat.Logcat;
import istanbul.codify.muudy.model.User;
import istanbul.codify.muudy.ui.base.BasePresenter;
import com.jakewharton.rxbinding2.view.RxView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import io.reactivex.android.schedulers.AndroidSchedulers;

final class ProfilePresenter extends BasePresenter<ProfileView> {

    @Override
    public void attachView(ProfileView view, View root) {
        super.attachView(view, root);

        mDisposables.add(
                RxView
                        .clicks(findViewById(R.id.profile_edit))
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(o -> {
                            Logcat.v("Edit clicked");
                            view.onEditClicked();
                        }));
    }

    void bind(User user) {
        findViewById(R.id.profile_username, AppCompatTextView.class).setText(user.username);
        findViewById(R.id.profile_fullname, AppCompatTextView.class).setText(user.namesurname);

        Picasso
                .with(getContext())
                .load(BuildConfig.URL + user.imgpath1)
                .into(findViewById(R.id.profile_picture, CircleImageView.class));
    }
}
