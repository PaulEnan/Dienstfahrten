package winfs.dienstreise.dienstfahrten;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

public class DestinationTab extends TabFragmentBase {

    DODestination dest;
    TextView editTextExtraCosts;
    TextView editTextFoodCosts;
    TextView editTextSleepCosts;
    TextView editTextOccasion;
    AutoCompleteTextView autoCompleteDestLocation;
    FloatingActionButton addDest;
    FloatingActionButton removeDest;
    private Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    AutoCompleterAdapter autoCompleterAdapter;
    private int pos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View destTab = inflater.inflate(R.layout.fragment_detail_view_destination, container, false);

        editTextExtraCosts = destTab.findViewById(R.id.editTextExtraCosts);
        editTextOccasion = destTab.findViewById(R.id.editTextOccassion);
        editTextFoodCosts = destTab.findViewById(R.id.editTextFoodCosts);
        editTextSleepCosts = destTab.findViewById(R.id.editTextSleepCosts);
        autoCompleteDestLocation = destTab.findViewById(R.id.autoCompleteDestLocation);
        addDest = destTab.findViewById(R.id.addDest);
        removeDest = destTab.findViewById(R.id.removeDest);

        addDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addTab();
            }
        });

        removeDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeTab();
            }
        });

        editTextExtraCosts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Overview.LOGIC.changeDestination(pos - 1,
                        editTextSleepCosts.getText().toString(),
                        editTextFoodCosts.getText().toString(),
                        editTextExtraCosts.getText().toString(),
                        autoCompleteDestLocation.getText().toString(),
                        editTextOccasion.getText().toString());
            }
        });

        editTextFoodCosts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Overview.LOGIC.changeDestination(pos - 1,
                        editTextSleepCosts.getText().toString(),
                        editTextFoodCosts.getText().toString(),
                        editTextExtraCosts.getText().toString(),
                        autoCompleteDestLocation.getText().toString(),
                        editTextOccasion.getText().toString());
            }
        });

        editTextSleepCosts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Overview.LOGIC.changeDestination(pos - 1,
                        editTextSleepCosts.getText().toString(),
                        editTextFoodCosts.getText().toString(),
                        editTextExtraCosts.getText().toString(),
                        autoCompleteDestLocation.getText().toString(),
                        editTextOccasion.getText().toString());
            }
        });

        editTextOccasion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Overview.LOGIC.changeDestination(pos - 1,
                        editTextSleepCosts.getText().toString(),
                        editTextFoodCosts.getText().toString(),
                        editTextExtraCosts.getText().toString(),
                        autoCompleteDestLocation.getText().toString(),
                        editTextOccasion.getText().toString());
            }
        });

        //region AutoCompleter
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
                Overview.LOGIC.changeLocation(pos, autoCompleteDestLocation.getText().toString());
                try {
                    String[] result = Overview.LOGIC.calculateCosts();
                    if (result.length > 1) {
                        throw new DienstfahrtenException(Messages.NotIdentifiable());
                    }
                } catch (DienstfahrtenException e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
                }
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
        //endregion

        loadSession();
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

    void setVars(DODestination dest, int pos) {
        this.pos = pos;
        this.dest = dest;
    }

    @Override
    boolean validateContent() {
        //validate
        return false;
    }

    @Override
    void removeTab() {
        if (!tpa.removeFragment(pos - 1)) {
            Toast.makeText(getContext(), "Du kannst dein einziges Ziel nicht entfernen", Toast.LENGTH_LONG);
        }
        else {
            Overview.LOGIC.removeStation(pos - 1);
            try {
                String[] result = Overview.LOGIC.calculateCosts();
                if (result.length > 1) {
                    throw new DienstfahrtenException(Messages.NotIdentifiable());
                }
            } catch (DienstfahrtenException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG);
            }
        }
    }

    @Override
    void addTab() {
        tpa.addFragment(pos);
        Overview.LOGIC.addEmptyStation(pos);
    }

    @Override
    void loadSession() {
        if (!(dest == null || dest.isDummy)) {
            editTextExtraCosts.setText(dest.tripExtraCosts + "");
            editTextOccasion.setText(dest.occasion);
            editTextSleepCosts.setText(dest.sleepCosts + "");
            editTextFoodCosts.setText(dest.foodCosts + "");
            autoCompleteDestLocation.setText(dest.location);
        }
    }
}
