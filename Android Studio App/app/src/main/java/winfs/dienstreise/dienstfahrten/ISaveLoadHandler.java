package winfs.dienstreise.dienstfahrten;
import android.location.Location;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 *
 * @author winf101441 + Joachim Borgloh
 */
public interface ISaveLoadHandler {
    boolean Save(DOSession session);
    DOSession Load(int index) throws SaveLoadException;
    List<DOSession> getAllSessions() throws SaveLoadException;
}
