package winfs.dienstreise.dienstfahrten;

import android.support.v4.app.Fragment;

public abstract class TabFragmentBase extends Fragment {
    TabPagerAdapter tpa;

    abstract void removeTab();

    abstract void addTab();

    abstract void loadSession();
}
