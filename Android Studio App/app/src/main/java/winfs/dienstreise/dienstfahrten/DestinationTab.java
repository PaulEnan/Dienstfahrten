package winfs.dienstreise.dienstfahrten;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

public class DestinationTab extends Fragment {

    boolean isOnly;
    DODestination dest;
    TextView editTextExtraCosts;
    TextView editTextFoodCosts;
    TextView editTextSleepCosts;
    TextView editTextOccassion;
    AutoCompleteTextView autoCompleteDestLocation;
    FloatingActionButton addDest;
    FloatingActionButton removeDest;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View startTab = inflater.inflate(R.layout.fragment_detail_view_destination, container, false);

        editTextExtraCosts = startTab.findViewById(R.id.editTextExtraCosts);
        editTextOccassion = startTab.findViewById(R.id.editTextOccassion);
        editTextFoodCosts = startTab.findViewById(R.id.editTextFoodCosts);
        editTextSleepCosts = startTab.findViewById(R.id.editTextSleepCosts);
        autoCompleteDestLocation = startTab.findViewById(R.id.autoCompleteDestLocation);
        addDest = startTab.findViewById(R.id.addDest);
        removeDest = startTab.findViewById(R.id.removeDest);



        editTextExtraCosts.setText(dest.tripExtraCosts + "");
        editTextOccassion.setText(dest.occasion);
        editTextSleepCosts.setText(dest.sleepCosts + "");
        editTextFoodCosts.setText(dest.foodCosts + "");

        removeDest.setEnabled(isOnly);

        return startTab;
    }

    public void setDest(DODestination dest) {
        this.dest = dest;
    }

    void setIsOnly(boolean isOnly){
        this.isOnly = isOnly;
    }

    @Override
    public void onPause() {
        super.onPause();
        dest.location = autoCompleteDestLocation.getText().toString();
        dest.occasion = editTextOccassion.getText().toString();
        dest.foodCosts = Double.parseDouble(editTextFoodCosts.getText().toString());
        dest.sleepCosts = Double.parseDouble(editTextFoodCosts.getText().toString());
        dest.tripExtraCosts = Double.parseDouble(editTextFoodCosts.getText().toString());
    }
}
