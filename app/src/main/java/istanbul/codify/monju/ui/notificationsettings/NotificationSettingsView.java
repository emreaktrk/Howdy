package istanbul.codify.monju.ui.notificationsettings;

import istanbul.codify.monju.api.pojo.response.ApiError;
import istanbul.codify.monju.model.NotificationSettings;
import istanbul.codify.monju.ui.base.MvpView;

interface NotificationSettingsView extends MvpView {

    void onSettingsChanged(NotificationSettings settings);

    void onLoaded();

    void onError(ApiError error);

    void onBackClicked();
}
