package com.codify.howdy.ui.notification;

import com.codify.howdy.ui.base.MvpView;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;

interface NotificationView extends MvpView {

    FragmentPagerItemAdapter create();
}
