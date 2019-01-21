package winfs.dienstreise.dienstfahrten;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class SummaryTab extends Fragment {
    private static final String TAB = "ZUSAMMENFASSUNG";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View startTab = inflater.inflate(R.layout.fragment_detail_view_summary, container, false);

        //TextView headingTextView = findViewById(R.);
        //TextView dateTextView = findViewById(R.);
        //TextView startTextView = findViewById(R.);
        //TextView intermediateTextView = findViewById(R.);
        //TextView goalTextView = findViewById(R.);
        //TextView kilometresTextView = findViewById(R.);
        //TextView variableCostsTextView = findViewById(R.);
        //TextView fixCostsTextView = findViewById(R.);
        //TextView totalCostsTextView = findViewById(R.);

        return startTab;
    }
}
