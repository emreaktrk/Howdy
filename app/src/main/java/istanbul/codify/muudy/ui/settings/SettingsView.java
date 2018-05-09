package istanbul.codify.muudy.ui.settings;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.ApiResult;
import istanbul.codify.muudy.model.Settings;
import istanbul.codify.muudy.ui.base.MvpView;

interface SettingsView extends MvpView {

    void onPolicyClicked();

    void onFeedbackClicked();

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
