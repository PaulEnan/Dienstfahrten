package winfs.dienstreise.dienstfahrten;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

public class DestinationTab extends Fragment implements ICustomTab {

    DOSession session;
    boolean isOnly;
    DODestination dest;
    TextView editTextExtraCosts;
    TextView editTextFoodCosts;
    TextView editTextSleepCosts;
    TextView editTextOccassion;
    AutoCompleteTextView autoCompleteDestLocation;
    FloatingActionButton addDest;
    FloatingActionButton removeDest;
    private Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    AutoCompleterAdapter autoCompleterAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View destTab = inflater.inflate(R.layout.fragment_detail_view_destination, container, false);

        editTextExtraCosts = destTab.findViewById(R.id.editTextExtraCosts);
        editTextOccassion = destTab.findViewById(R.id.editTextOccassion);
        editTextFoodCosts = destTab.findViewById(R.id.editTextFoodCosts);
        editTextSleepCosts = destTab.findViewById(R.id.editTextSleepCosts);
        autoCompleteDestLocation = destTab.findViewById(R.id.autoCompleteDestLocation);
        addDest = destTab.findViewById(R.id.addDest);
        removeDest = destTab.findViewById(R.id.removeDest);



        editTextExtraCosts.setText(dest.tripExtraCosts + "");
        editTextOccassion.setText(dest.occasion);
        editTextSleepCosts.setText(dest.sleepCosts + "");
        editTextFoodCosts.setText(dest.foodCosts + "");
        final Context context = this.getContext();
        final AppCompatAutoCompleteTextView autoCompleteTextView = destTab.findViewById
                (R.id.autoCompleteDestLocation);

        //Setting up the adapter for AutoSuggest
        autoCompleterAdapter = new AutoCompleterAdapter(context,
                android.R.layout.simple_dropdown_item_1line);
        autoCompleteTextView.setThreshold(3);
        autoCompleteTextView.setAdapter(autoCompleterAdapter);
        autoCompleteTextView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            int position, long id) {
                        autoCompleteTextView.setText(autoCompleterAdapter.getObject(position));
                    }
                });

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int
                    count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                handler.removeMessages(TRIGGER_AUTO_COMPLETE);
                handler.sendEmptyMessageDelayed(TRIGGER_AUTO_COMPLETE,
                        AUTO_COMPLETE_DELAY);
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        handler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == TRIGGER_AUTO_COMPLETE) {
                    if (!TextUtils.isEmpty(autoCompleteTextView.getText())) {
                        makeApiCall(autoCompleteTextView.getText().toString());
                    }
                }
                return false;
            }
        });

        if (!(session == null || session.isDummy)) {

        }

        removeDest.setEnabled(isOnly);

        return destTab;
    }


    private void makeApiCall(final String text) {
        CentralLogic.make(getContext(), text, new Response.Listener<List<String>>() {
            @Override
            public void onResponse(List<String> response) {
                //IMPORTANT: set data here and notify
                autoCompleterAdapter.setData(response);
                autoCompleterAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
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

    @Override
    public void performTabaction() {

    }
}
