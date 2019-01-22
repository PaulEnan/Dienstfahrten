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

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;

public class Overview extends AppCompatActivity {
    public static CentralLogic LOGIC;

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
                startActivity(openNewVoyage);
            }
        });

        ListView voyagesListView = findViewById(R.id.voyagesListView);
        DatabaseHelper db = new DatabaseHelper(this.getApplicationContext());
        db.onUpgrade(db.getWritableDatabase(), 0, 1);
        boolean greatSuccess = db.Save(new DOSession(0, new LinkedList<DODestination>()
        {{
                add(new DODestination(5,1,2,
                        "NotkeStraße 15 22607 Hamburg"));
        }}, "Zu Arbeit", new DOPerson("Felix", "Miertsch"),
                        "Hohe Straße 90 21073 Hamburg",
                new Date(), 5, 7));

        LOGIC = new CentralLogic(new ApiUser(), db);

        final VoyageAdapter voyageAdapter = new VoyageAdapter(this, LOGIC.sessions);
        voyagesListView.setAdapter(voyageAdapter);

        voyagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent openVoyageDetail = new Intent(getApplicationContext(), VoyageDetail.class);
                openVoyageDetail.putExtra(
                        "winfs.dienstreise.dienstfahrten.SESSIONDATA",
                        LOGIC.sessions.get(position).id
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

