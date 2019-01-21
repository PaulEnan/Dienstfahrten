package winfs.dienstreise.dienstfahrten;

/**
 *
 * @author Paul Enan
 */
public class FakeSaveLoadHandler implements ISaveLoadHandler {

    @Override
    public void Save(DOSession session) throws SaveLoadException {

    }

    @Override
    public DOSession Load(int index) throws SaveLoadException {
        return null;
    }

    @Override
    public DOSession[] getAllSessions() throws SaveLoadException {
        return new DOSession[0];
    }
}
