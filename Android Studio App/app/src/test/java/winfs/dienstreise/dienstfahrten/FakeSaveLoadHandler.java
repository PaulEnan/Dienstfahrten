package winfs.dienstreise.dienstfahrten;

import java.util.List;

/**
 *
 * @author Paul Enan
 */
public class FakeSaveLoadHandler implements ISaveLoadHandler {

    private List<DOSession> sessions;

    public FakeSaveLoadHandler(List<DOSession> sessions) {
        this.sessions = sessions;
    }

    @Override
    public boolean Save(DOSession session) {
        return false;
    }

    @Override
    public DOSession Load(int index) throws SaveLoadException {
        return null;
    }

    @Override
    public List<DOSession> getAllSessions() throws SaveLoadException {
        return sessions;
    }
}
