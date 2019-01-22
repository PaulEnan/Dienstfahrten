package winfs.dienstreise.dienstfahrten;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabPagerAdapter extends FragmentStatePagerAdapter {
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

        if (position == (getCount() - 1)) {
            SummaryTab st = new SummaryTab();
            st.setSession(session);
            return st;
        }
        DestinationTab dt = new DestinationTab();
        dt.setDest(session.getStations().get(position - 1));
        dt.setIsOnly(session.getStations().size() == 1);
        return dt;
    }



    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return vd.getString(R.string.fragment_start);
        } else if (position == (getCount() - 1)) {
            return vd.getString(R.string.fragment_summary);
        } else if (position == (getCount() - 2)) {
            return vd.getString(R.string.fragment_final_destination);
        } else {
            return vd.getString(R.string.fragment_destination);
        }
    }

    @Override
    public int getCount() {
        // Every Station + the summary tab + start tab
        if(!session.isDummy){
            return session.getStations().size() + 2;
        }
        return 3;
    }
}
