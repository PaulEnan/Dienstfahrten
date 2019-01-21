package winfs.dienstreise.dienstfahrten;

/**
 * @author Paul Enan
 */
public class Messages {
    public static String NotFound(String place) {
        return "ERROR: " + place + " konnte nicht gefunden werden.";
    }

    public static String DidYouMean() {
        return "Meinten Sie";
    }

    public static String NoAdressesWith(Character symbol) {
        return "ERROR: Adressen mit '" + symbol + "' werden nicht akzeptiert.";
    }
}
