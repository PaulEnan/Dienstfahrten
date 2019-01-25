package winfs.dienstreise.dienstfahrten;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import java.util.List;

public class StartTab extends TabFragmentBase {

    DOSession session;
    private Handler handler;
    private static final int TRIGGER_AUTO_COMPLETE = 100;
    private static final long AUTO_COMPLETE_DELAY = 300;
    AppCompatAutoCompleteTextView autoCompleteTextView;
    AutoCompleterAdapter autoCompleterAdapter;
    EditText editTextTitle;
    EditText editTextPrename;
    EditText editTextSurname;
    EditText editTextStartDate;
    EditText editTextDuration;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View startTab = inflater.inflate(R.layout.fragment_detail_view_start, container, false);


        editTextTitle = startTab.findViewById(R.id.editTextTitle);
        autoCompleteTextView = startTab.findViewById(R.id.autoCompleteStartLocation);
        editTextPrename = startTab.findViewById(R.id.editTextPrename);
        editTextSurname = startTab.findViewById(R.id.editTextSurname);
        editTextStartDate = startTab.findViewById(R.id.editTextStartDate);
        editTextDuration = startTab.findViewById(R.id.editTextDuration);

        editTextPrename.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Overview.LOGIC.changePreName(editTextPrename.getText().toString());
            }
        });

        editTextSurname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Overview.LOGIC.changeSurName(editTextSurname.getText().toString());
            }
        });

        editTextTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Overview.LOGIC.changeTitle(editTextTitle.getText().toString());
            }
        });

        editTextStartDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Overview.LOGIC.changeDate(editTextStartDate.getText().toString());
            }
        });

        editTextDuration.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                Overview.LOGIC.changeDuration(editTextDuration.getText().toString());
            }
        });

        //region AutoCompleter
        final Context context = this.getContext();
        final AppCompatAutoCompleteTextView autoCompleteTextView = startTab.findViewById
                (R.id.autoCompleteStartLocation);

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
                Overview.LOGIC.changeStartingLocation(autoCompleteTextView.getText().toString());
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

        return startTab;
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

    @Override
    void removeTab() {
        //nothing
    }

    @Override
    void addTab() {
        tpa.addFragment(0);
    }

    @Override
    void loadSession() {
        if (!(session == null || session.isDummy)) {
            editTextTitle.setText(session.title);
            editTextPrename.setText(session.person.prename);
            editTextSurname.setText(session.person.surname);
            editTextStartDate.setText(DatabaseHelper.GERMANDATEFORMAT.format(session.startDate));
            editTextDuration.setText(session.duration + "");
            autoCompleteTextView.setText(session.startLocation);
        }
    }
}
