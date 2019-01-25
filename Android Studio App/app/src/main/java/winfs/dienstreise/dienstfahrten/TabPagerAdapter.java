package winfs.dienstreise.dienstfahrten;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TabPagerAdapter extends FragmentPagerAdapter {
    DOSession session;
    List<TabFragmentBase> mFragmentList;
    TabLayout tabLayout;

    public TabPagerAdapter(FragmentManager fm, DOSession session, TabLayout tabLayout) {
        super(fm);
        this.session = session;
        mFragmentList = fillFragmentList();
        this.tabLayout = tabLayout;
    }

    private List<TabFragmentBase> fillFragmentList() {
        List<TabFragmentBase> list = new ArrayList<>();
        StartTab st = new StartTab();
        st.session = session;
        st.tpa = this;
        list.add(st);
        int pos = 1;
        for (DODestination dest : session.getStations()) {
            DestinationTab destTab = new DestinationTab();
            destTab.setVars(dest, pos);
            destTab.tpa = this;
            list.add(destTab);
            pos++;
        }

        SummaryTab summaryTab = new SummaryTab();
        summaryTab.session = session;
        summaryTab.tpa = this;
        list.add(summaryTab);

        return list;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        return mFragmentList.get(position);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Start";
        } else if (position == (getCount() - 1)) {
            return "Infos";
        } else if (position == (getCount() - 2)) {
            return "Ziel";
        } else {
            return "Zwischenziel";
        }
    }

    @Override
    public int getCount() {
        // Every Station + the summary tab + start tab
        return mFragmentList.size();
    }

    public void addFragment(int position) {
        DestinationTab dt = new DestinationTab();
        session.addStation(new DODestination(), position);
        dt.setVars(session.getStationAt(position), position);
        mFragmentList.add(position, dt);
        tabLayout.addTab(new TabLayout.Tab(), position + 1);
        notifyDataSetChanged();
    }

    public boolean removeFragment(int position) {
        if (!session.onlyOneDestination()) {
            mFragmentList.remove(position);

            notifyDataSetChanged();
            return true;
        }

        return false;
    }
}
