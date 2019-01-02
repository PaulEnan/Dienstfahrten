package winfs.dienstreise.dienstfahrten;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.LinkedList;

public class Overview extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (!isNetworkAvailable()) {
            showConnectionAlertDialog();

        }
        else {
            loadVoyages();
        }
    }

    private void loadVoyages() {
        setContentView(R.layout.activity_overview);

        FloatingActionButton addBtn = findViewById(R.id.addBtn);

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openNewVoyage = new Intent(getApplicationContext(), VoyageDetail.class);
                openNewVoyage.putExtra(
                        "winfs.dienstreise.dienstfahrten.SESSIONDATA",
                        -1
                );
                startActivity(openNewVoyage);
            }
        });

        ListView voyagesListView = findViewById(R.id.voyagesListView);

        // TODO Fahrten Laden und nicht statische nehmen
        LinkedList<SessionData> data = new LinkedList<SessionData>() {{
            add(
                    new SessionData(
                            new LinkedList<String>() {
                                {
                                    add("Hamburg");
                                    add("Neuhaus");
                                }
                            },

                            50,
                            60
                    )
            );
            add(
                    new SessionData(
                            new LinkedList<String>() {
                                {
                                    add("Bahrenfeld");
                                    add("Bergedorf");
                                }
                            },

                            50,
                            60
                    )
            );
        }};
        final VoyageAdapter voyageAdapter = new VoyageAdapter(this, data);
        voyagesListView.setAdapter(voyageAdapter);

        voyagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openVoyageDetail = new Intent(getApplicationContext(), VoyageDetail.class);
                openVoyageDetail.putExtra(
                        "winfs.dienstreise.dienstfahrten.SESSIONDATA",
                        voyageAdapter.getItemId(position)
                );
                startActivity(openVoyageDetail);
            }
        });
    }


    private void showConnectionAlertDialog() {
        AlertDialog.Builder alertDialogBuilder =
                new AlertDialog.Builder(this)
                        .setTitle("Keine Internetverbindung")
                        .setMessage("Es ist keine Internetanbindung gegeben. \n" +
                                "Die App wird ohne Internetzugang nicht funktionieren.")
                        .setPositiveButton("Verbindung hergestellt", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (isNetworkAvailable()) {
                                    dialog.cancel();
                                    loadVoyages();
                                } else {
                                    showConnectionAlertDialog();
                                }
                            }
                        })
                        .setNegativeButton("App schließen", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                System.exit(0);
                            }
                        });
        alertDialogBuilder.show();
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}

