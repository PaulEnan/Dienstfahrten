package winfs.dienstreise.dienstfahrten;

/**
 *
 * @author Paul Enan
 */
public class FakeSaveLoadHandler implements ISaveLoadHandler {

    @Override
    public void Save(SessionData session) throws SaveLoadException {

    }

    @Override
    public SessionData Load(int index) throws SaveLoadException {
        return null;
    }

    @Override
    public SessionData[] getAllSessions() throws SaveLoadException {
        return new SessionData[0];
    }
}
