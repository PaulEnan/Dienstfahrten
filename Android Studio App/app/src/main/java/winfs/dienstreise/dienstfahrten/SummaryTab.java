package winfs.dienstreise.dienstfahrten;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
        summaryTab.findViewById(R.id.saveButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateContent()) {
                    Toast.makeText(getContext(), "Speichern nicht möglich durch inkorrekte Werte", Toast.LENGTH_LONG);
                } else {
                    Intent openOverview = new Intent(getContext(), Overview.class);

                    startActivity(openOverview);
                }
            }
        });

        loadSession();

        return summaryTab;
    }

    @Override
    boolean validateContent() {
        boolean valid = true;
        for (TabFragmentBase tab : tpa.mFragmentList) {
            valid = tab.validateContent();
            if (!valid) {
                return valid;
            }
        }
        return valid;
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
                Toast.makeText(this.getContext(), e.getMessage(), Toast.LENGTH_LONG);
            }

            headingTextView.setText("Zusammenfassung für " + session.title);
            startDateTextView.setText(DatabaseHelper.GERMANDATEFORMAT.format(session.startDate));
            personTextView.setText(session.person.toString());
            startTextView.setText(session.startLocation);
            intermediateTextView.setText(session.stations.size() - 1 + " Zwischenziele");
            goalTextView.setText(session.getLastLocation());
            kilometresTextView.setText(String.format(Locale.GERMAN,"%10.2f", (session.getVariableCosts() / .3)));
            variableCostsTextView.setText(String.format(Locale.GERMAN,"%10.2f", session.getVariableCosts()));
            fixCostsTextView.setText(String.format(Locale.GERMAN,"%10.2f", session.getFixedCosts()));
            totalCostsTextView.setText(String.format(Locale.GERMAN,"%10.2f", session.getFinalCosts()));
        }

    }
}
