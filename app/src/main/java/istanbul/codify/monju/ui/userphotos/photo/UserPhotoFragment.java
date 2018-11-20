package istanbul.codify.monju.ui.userphotos.photo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.blankj.utilcode.util.StringUtils;

import istanbul.codify.monju.MuudyFragment;
import istanbul.codify.monju.R;

public final class UserPhotoFragment extends MuudyFragment implements UserPhotoView {

    private UserPhotoPresenter mPresenter = new UserPhotoPresenter();

    public static Bundle args(@Nullable String path) {
        Bundle args = new Bundle();
        args.putSerializable(String.class.getSimpleName(), StringUtils.isEmpty(path) ? "" : path);

        return args;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_user_photo;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, this);

        String path = getSerializable(String.class);
        if (path != null) {
            mPresenter.bind(path);
        }else{

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }
}
