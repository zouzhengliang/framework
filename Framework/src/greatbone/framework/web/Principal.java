package greatbone.framework.web;

/**
 */
public interface Principal {

    String getName();

    String getCredential();

    int roles();

}
