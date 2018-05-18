package istanbul.codify.muudy.navigation;

import com.google.firebase.messaging.RemoteMessage;
import istanbul.codify.muudy.MuudyFragment;
import istanbul.codify.muudy.model.NotificationActionType;

public abstract class NavigationFragment extends MuudyFragment implements Navigation.ISelectable {

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof Navigation.IController) {
            int selection = this.getSelection();

            Navigation.IController controller = (Navigation.IController) getActivity();
            controller.onNavigationSelected(selection);
        }
    }


    public NotificationActionType getNotificationActionType(RemoteMessage message) {
        return NotificationActionType.value(message.getData().get("actiontype"));
    }

    public int getNotificatioItemId(RemoteMessage message) {
        return Integer.parseInt(message.getData().get("itemid"));
    }
}
