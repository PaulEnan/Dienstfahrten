package winfs.dienstreise.dienstfahrten;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;

public class VoyageAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    LinkedList<SessionData> voyages;

    public VoyageAdapter(Context c, LinkedList<SessionData> voyages) {
        Collections.sort(voyages, new Comparator<SessionData>() {
            @Override
            public int compare(SessionData o1, SessionData o2) {
                return o1.getStations().toString().compareTo(o2.getStations().toString());
            }
        });
        this.voyages = voyages;
        mInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return voyages.size();
    }

    @Override
    public Object getItem(int position) {
        return voyages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
         SessionData voyage = voyages.get(position);
        View v = mInflater.inflate(R.layout.voyage_details_adapter, null);

        TextView occassionTextView = v.findViewById(R.id.occassionTextView);
        TextView tourTextView = v.findViewById(R.id.tourTextView);
        TextView dateTextView =  v.findViewById(R.id.dateTextView);
        TextView totalCostsTextView =  v.findViewById(R.id.totalCostsTextView);

        String occassion = "";
        String tour = "";
        for (String s : voyage.getStations()) {
            tour += s + " -> ";
        }
        String date = new Date().toString();
        String totalCosts = String.format("%10.2f €", voyage.getFinalCosts());

        occassionTextView.setText(occassion);
        tourTextView.setText(tour);
        dateTextView.setText(date);
        totalCostsTextView.setText(totalCosts);

        return v;
    }
}
