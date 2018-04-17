package istanbul.codify.muudy.navigation;

import istanbul.codify.muudy.HowdyFragment;

public abstract class NavigationFragment extends HowdyFragment implements Navigation.ISelectable {

    @Override
    public void onResume() {
        super.onResume();

        if (getActivity() instanceof Navigation.IController) {
            int selection = this.getSelection();

            Navigation.IController controller = (Navigation.IController) getActivity();
            controller.onNavigationSelected(selection);
        }
    }
}
