package winfs.dienstreise.dienstfahrten;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

public class VoyageDetail extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private TabPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    DOSession session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            session = loadSession();
            setContentView(R.layout.activity_detail_view);
            // Create the adapter that will return a fragment for each of the
            // primary sections of the activity.
            mSectionsPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), session, this);

            // Set up the ViewPager with the sections adapter.
            mViewPager = findViewById(R.id.container);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tabsPanel);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        } catch (DienstfahrtenException e) {
            e.printStackTrace();
            Toast.makeText(this, "Reise konnte nicht geladen werden", Toast.LENGTH_LONG);
        }
    }

    private DOSession loadSession() throws DienstfahrtenException {
        int passedItemId = getIntent().getIntExtra(
                "winfs.dienstreise.dienstfahrten.SESSIONDATA",
                -1);
        return Overview.LOGIC.LoadSession(passedItemId);
    }
}
