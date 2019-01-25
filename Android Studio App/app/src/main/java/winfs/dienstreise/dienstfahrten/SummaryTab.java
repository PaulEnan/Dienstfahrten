package winfs.dienstreise.dienstfahrten;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class SummaryTab extends TabFragmentBase {

    DOSession session;
    TextView headingTextView;
    TextView startDateTextView;
    TextView personTextView;
    TextView startTextView;
    TextView intermediateTextView;
    TextView goalTextView;
    TextView kilometresTextView;
    TextView variableCostsTextView;
    TextView fixCostsTextView;
    TextView totalCostsTextView;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View summaryTab = inflater.inflate(R.layout.fragment_detail_view_summary, container, false);

        headingTextView = summaryTab.findViewById(R.id.headingTextView);
        startDateTextView = summaryTab.findViewById(R.id.startDateTextView);
        personTextView = summaryTab.findViewById(R.id.personTextView);
        startTextView = summaryTab.findViewById(R.id.startTextView);
        intermediateTextView = summaryTab.findViewById(R.id.intermediateTextView);
        goalTextView = summaryTab.findViewById(R.id.goalTextView);
        kilometresTextView = summaryTab.findViewById(R.id.kilometresTextView);
        variableCostsTextView = summaryTab.findViewById(R.id.variableCostsTextView);
        fixCostsTextView = summaryTab.findViewById(R.id.fixCostsTextView);
        totalCostsTextView = summaryTab.findViewById(R.id.totalCostsTextView);
        Button saveButton = summaryTab.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Overview.LOGIC.SaveSession();
                    Intent openOverview = new Intent(getContext(), Overview.class);
                    startActivity(openOverview);
                } catch (DienstfahrtenException e) {
                    Toast.makeText(getContext(),
                            "Speichern aktuell nicht möglich",
                            Toast.LENGTH_LONG).show();
                }

            }
        });

        loadSession();

        return summaryTab;
    }

    @Override
    void removeTab() {
        //do nothing
    }

    @Override
    void addTab() {
        //do nothing
    }

    @Override
    void loadSession() {
        if (!(session == null || session.isDummy)) {
            try {
                String[] result = Overview.LOGIC.calculateCosts();
                if (result.length > 1) {
                    throw new DienstfahrtenException(Messages.NotIdentifiable());
                }
            } catch (DienstfahrtenException e) {
                Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }

            headingTextView.setText("Zusammenfassung für " + session.title);
            startDateTextView.setText(DatabaseHelper.GERMANDATEFORMAT.format(session.startDate));
            personTextView.setText(session.person.toString());
            startTextView.setText(session.startLocation);
            intermediateTextView.setText(session.stations.size() - 1 + " Zwischenziele");
            goalTextView.setText(session.getLastLocation());
            kilometresTextView.setText(String.format(Locale.GERMAN, "%10.2f", (session.getVariableCosts() / .3)));
            variableCostsTextView.setText(String.format(Locale.GERMAN, "%10.2f", session.getVariableCosts()));
            fixCostsTextView.setText(String.format(Locale.GERMAN, "%10.2f", session.getFixedCosts()));
            totalCostsTextView.setText(String.format(Locale.GERMAN, "%10.2f", session.getFinalCosts()));
        }

    }
}
