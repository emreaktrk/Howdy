package istanbul.codify.muudy.ui.notificationsettings;

import istanbul.codify.muudy.api.pojo.response.ApiError;
import istanbul.codify.muudy.model.NotificationSettings;
import istanbul.codify.muudy.ui.base.MvpView;

interface NotificationSettingsView extends MvpView {

    void onSettingsChanged(NotificationSettings settings);

    void onLoaded();

    void onError(ApiError error);

    void onBackClicked();
}
