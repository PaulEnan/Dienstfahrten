package winfs.dienstreise.dienstfahrten;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class TabPagerAdapter extends FragmentPagerAdapter{
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
            switch (position) {
                case 0:
                    StartTab startTab = new StartTab();
                    return startTab;
                case 1:
                    DestinationTab destinationTab = new DestinationTab();
                    return destinationTab;
                default:
                    SummaryTab summaryTab = new SummaryTab();
                    return summaryTab;
            }
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            if(position == 0) {
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
}
