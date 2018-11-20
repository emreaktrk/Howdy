package istanbul.codify.monju.ui.userprofile.more;

import istanbul.codify.monju.ui.base.MvpView;

interface MoreView extends MvpView {

    void onUnfollowClicked();

    void onReportClicked();

    void onBlockChanged(boolean isBlocked);

    void onNotificationChanged(boolean isEnabled);
}
