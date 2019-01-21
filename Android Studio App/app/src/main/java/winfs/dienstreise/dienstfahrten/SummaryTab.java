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

    DOSession session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View startTab = inflater.inflate(R.layout.fragment_detail_view_summary, container, false);

        TextView headingTextView = startTab.findViewById(R.id.headingTextView);
        TextView dateTextView = startTab.findViewById(R.id.dateTextView);
        TextView personTextView = startTab.findViewById(R.id.personTextView);
        TextView startTextView = startTab.findViewById(R.id.startTextView);
        TextView intermediateTextView = startTab.findViewById(R.id.intermediateTextView);
        TextView goalTextView = startTab.findViewById(R.id.goalTextView);
        TextView kilometresTextView = startTab.findViewById(R.id.kilometresTextView);
        TextView variableCostsTextView = startTab.findViewById(R.id.variableCostsTextView);
        TextView fixCostsTextView = startTab.findViewById(R.id.fixCostsTextView);
        TextView totalCostsTextView = startTab.findViewById(R.id.totalCostsTextView);

        headingTextView.setText("Zusammenfassung für " + session.title);
        dateTextView.setText(VoyageAdapter.GERMANDATEFORMAT.format(session.startDate));
        personTextView.setText(session.person.toString());
        startTextView.setText(session.startLocation.toString());
        intermediateTextView.setText(session.stations.size() - 1 + " Zwischenziele");
        goalTextView.setText(session.getLastLocation().toString());
        kilometresTextView.setText("aa");
        variableCostsTextView.setText("Zusammenfassung für " + session.title);
        fixCostsTextView.setText("Zusammenfassung für " + session.title);
        totalCostsTextView.setText(String.format("%10.2$f", session.getFinalCosts()));


        return startTab;
    }

    public void setSession(DOSession session) {
        this.session = session;
    }
}
