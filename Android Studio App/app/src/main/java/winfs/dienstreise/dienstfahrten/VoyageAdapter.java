package winfs.dienstreise.dienstfahrten;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class VoyageAdapter extends BaseAdapter {
    LayoutInflater mInflater;
    List<DOSession> voyages;

    public VoyageAdapter(Context c, List<DOSession> voyages) {
        if (voyages != null){
            Collections.sort(voyages, new Comparator<DOSession>() {
                @Override
                public int compare(DOSession o1, DOSession o2) {
                    return o1.startDate.compareTo(o2.startDate);
                }
            });
        }
        else {
            voyages = new LinkedList<>();
        }
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
         DOSession voyage = voyages.get(position);
        View v = mInflater.inflate(R.layout.voyage_details_adapter, null);

        TextView titleTextView = v.findViewById(R.id.occassionTextView);
        TextView tourTextView = v.findViewById(R.id.tourTextView);
        TextView dateTextView =  v.findViewById(R.id.dateTextView);
        TextView totalCostsTextView =  v.findViewById(R.id.totalCostsTextView);

        String title = voyage.title;
        String tour = voyage.startLocation.city + " -> ";
        for (DODestination dest : voyage.getStations()) {
            tour += dest.location.city + " -> ";
        }
        tour = tour.substring(0, tour.length() - 4);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        String date = dateFormat.format(voyage.startDate);
        String totalCosts = String.format(Locale.GERMAN, "%10.2f €", voyage.getFinalCosts());

        titleTextView.setText(title);
        tourTextView.setText(tour);
        dateTextView.setText(date);
        totalCostsTextView.setText(totalCosts);

        return v;
    }
}
