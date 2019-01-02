package winfs.dienstreise.dienstfahrten;

/**
 *
 * @author Paul Enan
 */
public class FakeSaveLoadHandler implements ISaveLoadHandler {

    @Override
    public void Save(String path, SessionData session) throws SaveLoadException {
    }

    @Override
    public SessionData Load(String path) throws SaveLoadException {
        return null;
    }
    
}
