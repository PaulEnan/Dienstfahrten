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
    public void Save(SessionData session) throws SaveLoadException;
    public SessionData Load(int index) throws SaveLoadException;
    public SessionData[] getAllSessions() throws SaveLoadException;
}
