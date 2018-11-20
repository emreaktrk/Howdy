package istanbul.codify.monju.navigation;

import istanbul.codify.monju.MuudyFragment;

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
}
