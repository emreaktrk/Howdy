package istanbul.codify.muudy.ui.userprofile.more;

import istanbul.codify.muudy.ui.base.MvpView;

interface MoreView extends MvpView {
    void onUnfollowClicked();

    void onReportClicked();

    void onBlockChanged(boolean isBlocked);

    void onNotificationChanged(boolean isEnabled);
}
