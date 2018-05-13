package istanbul.codify.muudy.ui.userprofile.more;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import istanbul.codify.muudy.MuudyBottomSheet;
import istanbul.codify.muudy.R;
import istanbul.codify.muudy.model.User;

public final class MoreBottomSheet extends MuudyBottomSheet implements MoreView {

    private MorePresenter mPresenter = new MorePresenter();

    private OnMoreClickListener.OnUnfollowClickListener mUnfollow;
    private OnMoreClickListener.OnBlockClickListener mBlock;
    private OnMoreClickListener.OnReportClickListener mReport;
    private OnMoreClickListener.OnNotificationClickListener mNotification;


    public static MoreBottomSheet newInstance(@NonNull User user) {
        Bundle args = new Bundle();
        args.putSerializable(User.class.getSimpleName(), user);

        MoreBottomSheet fragment = new MoreBottomSheet();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.layout_more_dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mPresenter.attachView(this, view);

        User user = getSerializable(User.class);
        if (user != null) {
            mPresenter.bind(user);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        mPresenter.detachView();
    }

    public MoreBottomSheet setOnUnfollowClickListener(OnMoreClickListener.OnUnfollowClickListener listener) {
        mUnfollow = listener;

        return this;
    }

    public MoreBottomSheet setOnBlockClickListener(OnMoreClickListener.OnBlockClickListener listener) {
        mBlock = listener;

        return this;
    }

    public MoreBottomSheet setOnReportClickListener(OnMoreClickListener.OnReportClickListener listener) {
        mReport = listener;

        return this;
    }

    public MoreBottomSheet setOnNotificationClickListener(OnMoreClickListener.OnNotificationClickListener listener) {
        mNotification = listener;

        return this;
    }

    @Override
    public void onUnfollowClicked() {
        if (mUnfollow != null) {
            mUnfollow.onUnfollowClick();
        }

        dismiss();
    }

    @Override
    public void onReportClicked() {
        if (mReport != null) {
            mReport.onReportClick();
        }

        dismiss();
    }

    @Override
    public void onBlockChanged(boolean isBlocked) {
        if (mBlock != null) {
            mBlock.onBlockChanged(isBlocked);
        }

        dismiss();
    }

    @Override
    public void onNotificationChanged(boolean isEnabled) {
        if (mNotification != null) {
            mNotification.onNotificationChanged(isEnabled);
        }

        dismiss();
    }

    public interface OnMoreClickListener {

        interface OnUnfollowClickListener {
            void onUnfollowClick();
        }

        interface OnBlockClickListener {
            void onBlockChanged(boolean isBlocked);
        }

        interface OnReportClickListener {
            void onReportClick();
        }

        interface OnNotificationClickListener {
            void onNotificationChanged(boolean isEnable);
        }
    }
}
