package winfs.dienstreise.dienstfahrten;

import java.util.List;

/**
 * @author winf101441 + Joachim Borgloh
 */
public interface ISaveLoadHandler {
    void Save(DOSession session) throws SaveLoadException;

    DOSession Load(int index) throws SaveLoadException;

    List<DOSession> getAllSessions() throws SaveLoadException;

    void removeDestination(DODestination stationAt) throws SaveLoadException;

}
