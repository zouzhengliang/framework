package greatbone.framework;

/**
 * To indicate a configuration error.
 */
public class MyFarmException extends RuntimeException {

    public MyFarmException() {
        super();
    }

    public MyFarmException(String message) {
        super(message);
    }

}
