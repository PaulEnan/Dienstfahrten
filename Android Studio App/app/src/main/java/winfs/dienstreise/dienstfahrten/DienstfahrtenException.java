package winfs.dienstreise.dienstfahrten;

/**
 * An exception for app made problems
 */
public class DienstfahrtenException extends Exception {
    /**
     * creates an exception with given message
     * @param msg
     */
    public DienstfahrtenException(String msg) {
        super(msg);
    }
}
