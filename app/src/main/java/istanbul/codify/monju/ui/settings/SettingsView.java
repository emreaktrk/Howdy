package istanbul.codify.monju.ui.settings;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.ApiResult;
import istanbul.codify.monju.model.Settings;
import istanbul.codify.monju.ui.base.MvpView;

interface SettingsView extends MvpView {

    void onPolicyClicked();

    void onFeedbackClicked();

    void onContactsClicked();

    void onFacebookFriendsClicked();

    void onSocialMediaClicked();

    void onChangePasswordClicked();

    void onNotificationSettingsClicked();

    void onSettingsChanged(Settings settings);

    void onEditClicked();

    void onLogoutClicked();

    void onLogout(ApiResult result);

    void onSettingsUpdated();

    void onError(ApiError error);

    void onCloseClicked();
}
