package winfs.dienstreise.dienstfahrten;

import java.util.List;

/**
 * @author Paul Enan + Joachim Borgloh
 */
public interface ISaveLoadHandler {

    boolean Save(DOSession session);

    DOSession Load(int index) throws SaveLoadException;

    List<DOSession> getAllSessions() throws SaveLoadException;
}
