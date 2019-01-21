package winfs.dienstreise.dienstfahrten;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.view.ViewGroup;

public class TabPagerAdapter extends FragmentPagerAdapter {
    DOSession session;
    VoyageDetail vd;

    public TabPagerAdapter(FragmentManager fm, DOSession session, VoyageDetail vd) {
        super(fm);
        this.session = session;
        this.vd = vd;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        if (position == 0) {
            StartTab st = new StartTab();
            st.setSession(session);
            return st;
        }

        if (position == getCount() - 1) {
            SummaryTab st = new SummaryTab();
            st.setSession(session);
            return st;
        }
        DestinationTab dt = new DestinationTab();
        dt.setSession(session);
        return dt;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return vd.getString(R.string.fragment_start);
        } else if (position == getCount() - 1) {
            return vd.getString(R.string.fragment_summary);
        } else if (position == getCount() - 2) {
            return vd.getString(R.string.fragment_final_destination);
        } else {
            return vd.getString(R.string.fragment_destination);
        }
    }

    @Override
    public int getCount() {
        // Every Station + the summary tab
        return session.getStations().size() + 2;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        FragmentManager manager = ((Fragment)object).getFragmentManager();
        FragmentTransaction trans = manager.beginTransaction();
        session.removeStation(position);
        trans.remove((Fragment)object);
        trans.commit();
    }
}
