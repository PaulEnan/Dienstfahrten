package winfs.dienstreise.dienstfahrten;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class StartTab extends Fragment {

    DOSession session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View startTab = inflater.inflate(R.layout.fragment_detail_view_start, container, false);

        if (!(session == null || session.isDummy)){

        }
        return startTab;
    }

    public void setSession(DOSession session) {
        this.session = session;
    }
}
