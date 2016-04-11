package greatbone.framework;

/**
 * To indicate a configuration error.
 */
public class GreatboneException extends RuntimeException {

    public GreatboneException() {
        super();
    }

    public GreatboneException(String message) {
        super(message);
    }

}
